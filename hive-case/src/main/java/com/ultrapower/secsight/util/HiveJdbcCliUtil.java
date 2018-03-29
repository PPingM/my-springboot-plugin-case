package com.ultrapower.secsight.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.UserGroupInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.PrivilegedExceptionAction;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Hive JavaApi jdbc
 */
public class HiveJdbcCliUtil {
	private static Logger LOG = LoggerFactory.getLogger(HiveJdbcCliUtil.class);



	private static String driverName = null;
	private static String sql = null;
	private static ResultSet res;
	private static ThreadLocal<Connection> local = new ThreadLocal<>();

	/**
	 * @param sql
	 * @return String
	 * @throws Exception
	 * @throws
	 * @Title: doSql
	 * @Description: 执行sql返回涉及记录数
	 */
	public static ArrayList<Map<String, String>> doQuerySql(String database, String sql) {
		LOG.info("exec hive sql:" + sql);
		Statement stmt = null;
		ResultSet rs = null;
		Connection conn = getConn();
		try {
			stmt = conn.createStatement();
			if(StringUtils.isNotEmpty(database)){
				stmt.execute("use " + database);
			}
			rs = stmt.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			int fields = rsmd.getColumnCount();
			ArrayList<Map<String, String>> rtlist = new ArrayList<Map<String, String>>();
			while (rs.next()) {
				Map<String, String> dmap = new HashMap<String, String>();
				for (int is = 1; is <= fields; is++) {
					String columnlabel = rsmd.getColumnLabel(is);
					dmap.put(columnlabel.toLowerCase(), rs.getString(columnlabel));
				}
				rtlist.add(dmap);
			}
			return rtlist;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			local.set(null);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					LOG.error(e.getMessage(), e);
				}
			}
		}
		return null;
	}

	/**
	 * @return Connection
	 * @throws Exception
	 * @throws
	 * @Title: getConn
	 * @Description: 获取连接
	 */
	public static Connection getConn() {
		String driverName = "org.apache.hive.jdbc.HiveDriver";
		String url = "jdbc:hive2://192.168.186.60:10000/secsight;principal=hive/secsight-nn01@HADOOP.COM";
		String user = "user";
		String password = "password";

		Connection conn = null;
		try {
			//设置jvm启动时krb5的读取路径参数 E:\rundata\java-workspace\springboot-plugin-case\hive-case\src\test\resources\etc\krb5.conf
			System.setProperty("java.security.krb5.conf", "E:\\rundata\\java-workspace\\springboot-plugin-case\\hive-case\\src\\test\\resources\\etc\\krb5.conf");
			//配置kerberos认证
			Configuration conf = new Configuration();
			conf.setBoolean("hadoop.security.authorization", true);
			conf.set("hadoop.security.authentication", "kerberos");
			UserGroupInformation.setConfiguration(conf);
			UserGroupInformation ugi = UserGroupInformation.loginUserFromKeytabAndReturnUGI("hive/secsight-nn01@HADOOP.COM",
					"E:\\rundata\\java-workspace\\springboot-plugin-case\\hive-case\\src\\test\\resources\\etc\\hive.keytab");
			conn = (Connection) ugi.doAs((PrivilegedExceptionAction<Object>) () -> {
				Connection tcon = null;
				try {
					Class.forName(driverName);
					tcon = DriverManager.getConnection(url, user, password);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return tcon;
			});
			local.set(conn);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			local.set(null);
		}
		return conn;
	}

	/**
	 * @return Connection
	 * @throws Exception
	 * @throws
	 * @Title: closeConn
	 * @Description: 关闭连接
	 */
	public static void closeConn() {
		Connection con = local.get();
		try {
			if (con != null) {
				con.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con = null;
			local.remove();
		}
	}
}