package com.cloudexpense.common.exception;

/**
 * ClassName: BusinessException
 * Package: com.cloudexpense.common.exception
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/7/28 21:52
 * @Version: v1.0
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message){
        super(message);
    }

}
