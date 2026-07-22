package com.cloudexpense.common.exception;

/**
 * ClassName: UserNotFoundException
 * Package: com.cloudexpense.common.exception
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/7/22 21:57
 * @Version: v1.0
 */
public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(){
        super("User not found");
    }
}
