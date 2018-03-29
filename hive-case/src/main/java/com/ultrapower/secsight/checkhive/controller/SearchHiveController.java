package com.ultrapower.secsight.checkhive.controller;

import com.ultrapower.secsight.checkhive.entry.HiveReqParam;
import com.ultrapower.secsight.checkhive.entry.TableInfo;
import com.ultrapower.secsight.checkhive.service.SearchHiveService;
import com.ultrapower.secsight.plugin.ResponseJson;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author :zhangdan
 * @Description:
 * @Company :
 * @date :2018/1/16 14:57
 */
@RestController
@RequestMapping("/hive")
public class SearchHiveController{
    static final Logger LOG = Logger.getLogger(SearchHiveController.class);

    @Autowired
    private SearchHiveService searchHiveService;

    @ResponseJson
    @RequestMapping("/databases")
    public List<String> showDatabases(){
        return searchHiveService.getDatabases();
    }

    @ResponseJson
    @RequestMapping("/tables")
    public List<String>  showTables(HiveReqParam hiveReqParam){
        if(hiveReqParam == null || StringUtils.isEmpty(hiveReqParam.getDatabase())){
            throw new RuntimeException("数据参数不合法");
        }
        return searchHiveService.getTables(hiveReqParam);
    }

    @ResponseJson
    @RequestMapping("/table-info")
    public TableInfo showTableCol(HiveReqParam hiveReqParam){
        if(hiveReqParam == null
                || StringUtils.isEmpty(hiveReqParam.getDatabase())
                || StringUtils.isEmpty(hiveReqParam.getTable())){
            throw new RuntimeException("数据参数不合法");
        }
        return searchHiveService.getTableInfo(hiveReqParam);
    }

    @ResponseJson
    @RequestMapping("/partitions")
    public List<String> showPartitions(HiveReqParam hiveReqParam){
        if(hiveReqParam == null
                || StringUtils.isEmpty(hiveReqParam.getDatabase())
                || StringUtils.isEmpty(hiveReqParam.getTable())){
            throw new RuntimeException("数据参数不合法");
        }
        return searchHiveService.getPartitions(hiveReqParam);
    }


    @RequestMapping("/download")
    public ResponseEntity<byte[]> showDataToFile(HiveReqParam hiveReqParam, HttpServletRequest request) throws UnsupportedEncodingException {
        String notice;
        HttpHeaders headers = new HttpHeaders();
        String agent = request.getHeader("User-Agent");

        if(hiveReqParam == null
                || StringUtils.isEmpty(hiveReqParam.getDatabase())
                || StringUtils.isEmpty(hiveReqParam.getTable())
                || StringUtils.isEmpty(hiveReqParam.getPartition())
                || StringUtils.isEmpty(hiveReqParam.getLimit())){
            notice = "数据参数不合法";
            headers.setContentType(MediaType.TEXT_PLAIN);
            return new ResponseEntity<>(notice.getBytes("utf-8"), headers, HttpStatus.NOT_FOUND);
        }

        String fileName = String.format("%s-%s-%s.txt", hiveReqParam.getDatabase(),
                hiveReqParam.getTable(), hiveReqParam.getPartition().replaceAll("/*\\w*=", "-"));
        try {
            byte[] data = searchHiveService.getDataToFile(hiveReqParam);
            if (agent.contains("Firefox")) {
                fileName = new String(fileName.getBytes("utf-8"), "iso-8859-1");
            } else {
                fileName = URLEncoder.encode(fileName, "utf-8");
            }
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", fileName);
            LOG.debug("下载成功 => fileName : " + fileName);
            return new ResponseEntity<>(data, headers, HttpStatus.OK);

        } catch (Exception e) {
            LOG.error("下载失败！", e);
        }
        notice = "下载失败，请重试";
        headers.setContentType(MediaType.TEXT_PLAIN);
        return new ResponseEntity<>(notice.getBytes("utf-8"), headers, HttpStatus.NOT_FOUND);
    }
}
