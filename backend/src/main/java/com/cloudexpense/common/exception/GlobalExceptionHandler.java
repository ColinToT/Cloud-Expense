package com.cloudexpense.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * ClassName: GlobalExceptionHandler
 * Package: com.cloudexpense.common.exception
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/7/26 21:08
 * @Version: v1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e){

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFound(ResourceNotFoundException e){
        return e.getMessage();
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBusinessException(BusinessException e){
        return e.getMessage();
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> handleFileTooLarge(MaxUploadSizeExceededException e){
        return ResponseEntity
                .badRequest()
                .body("File size exceeds limit");
    }

}
