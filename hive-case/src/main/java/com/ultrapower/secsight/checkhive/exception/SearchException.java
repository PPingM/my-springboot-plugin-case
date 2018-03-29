package com.ultrapower.secsight.checkhive.exception;

/**
 * @author :zhangdan
 * @Description:
 * @Company :
 * @date :2018/1/17 16:39
 */
public class SearchException extends RuntimeException {

    public SearchException(String message, Throwable cause) {
        super(message, cause);
    }

    public SearchException(Throwable cause) {
        super(cause);
    }

    public SearchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {

    }

}
