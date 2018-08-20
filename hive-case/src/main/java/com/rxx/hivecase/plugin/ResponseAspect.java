package com.rxx.hivecase.plugin;

import com.rxx.hivecase.checkhive.entry.ResultData;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author :zhangdan
 * @Description:
 * @Company :
 * @date :2018/1/22 10:51
 */

@Aspect
@Component
public class ResponseAspect {


    @Autowired
    private MappingJackson2HttpMessageConverter converter;

    Logger LOG = Logger.getLogger(ResponseAspect.class);

    @Pointcut("execution(* com.rxx.hivecase.checkhive.controller.*.*(..))")
    public void responseCrossOriginPointcut(){}

    @Pointcut("execution(* com.rxx.hivecase.checkhive.controller.*.*(..)) && @annotation(com.rxx.hivecase.plugin.ResponseJson)")
    public void responseBodyPointcut(){}

    @Before(value = "responseCrossOriginPointcut()")
    public void responseCrossOrigin(JoinPoint jp) throws Throwable {
        /* 解决跨域问题 */
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
    }

    @Around(value = "responseBodyPointcut()")
    public void formatResult2Json(ProceedingJoinPoint pjp) throws Throwable {
        Object ret = pjp.proceed();
        ResultData resultData = new ResultData();
        resultData.setData(ret);

        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        HttpOutputMessage outputMessage = new ServletServerHttpResponse(response);
        converter.write(resultData, MediaType.APPLICATION_JSON, outputMessage);
        shutdownResponse(response);
    }

    @AfterThrowing(pointcut = "responseBodyPointcut()", throwing = "error")
    public void handleForException(JoinPoint jp, Throwable error) throws Throwable{
        String notice = error == null || StringUtils.isEmpty(error.getMessage())? "操作失败":error.getMessage();

        ResultData resultData = new ResultData();
        resultData.setSuccess(false);
        resultData.setNotice(notice);

        LOG.error(jp.getSignature().getName() + "-error!", error);
        HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
        HttpOutputMessage outputMessage = new ServletServerHttpResponse(response);
        converter.write(resultData, MediaType.APPLICATION_JSON, outputMessage);

        shutdownResponse(response);
    }

    private void shutdownResponse(HttpServletResponse response) throws IOException {
        response.getOutputStream().close();
    }
}
