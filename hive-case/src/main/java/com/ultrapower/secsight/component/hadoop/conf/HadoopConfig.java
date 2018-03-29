package com.ultrapower.secsight.component.hadoop.conf;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.CommonConfigurationKeysPublic;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.UserGroupInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Created by Dare on 2017/12/22.
 */
@Component
public class HadoopConfig {
	private static Logger LOG = LoggerFactory.getLogger(HadoopConfig.class);

	@Autowired
	private Environment env;

	/**
	 * 获取当前hadoop配置
	 *
	 * @return
	 */
	@Bean(name = "hadoopConf")
	public Configuration getHaoopConfig() {
		String hdfsPath = env.getProperty("ultra.hdfs.path");
		String corePath = env.getProperty("ultra.core.path");
		String mapredPath = env.getProperty("ultra.mapred.path");
		String yarnPath = env.getProperty("ultra.yarn.path");

		Configuration conf = new Configuration();
		conf.addResource(new Path(hdfsPath));
		conf.addResource(new Path(corePath));
		conf.addResource(new Path(mapredPath));
		conf.addResource(new Path(yarnPath));

		String authType = conf.get(CommonConfigurationKeysPublic.HADOOP_SECURITY_AUTHENTICATION);
		if (StringUtils.equalsIgnoreCase("kerberos", authType)) {
			conf.set("dfs.client.kerberos.principal", env.getProperty("ultra.dfs.client.kerberos.principal"));
			conf.set("dfs.client.keytab.file", env.getProperty("ultra.dfs.client.keytab.file"));
			System.setProperty("java.security.krb5.conf", env.getProperty("ultra.krb5.conf.path"));
			UserGroupInformation.setConfiguration(conf);
			try {
				UserGroupInformation.loginUserFromKeytab(conf.get("dfs.client.kerberos.principal"),
						conf.get("dfs.client.keytab.file"));
				LOG.info("kerberos认证成功");
			} catch (IOException e) {
				LOG.error("kerberos认证失败", e);
			}
		} else {
			LOG.warn("hadoop配置文件中未找到kerberos相关配置，未启用kerberos认证。");
			System.setProperty("HADOOP_USER_NAME", env.getProperty("ultra.hdfs.user"));
		}
		return conf;
	}
}
