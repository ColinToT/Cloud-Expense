package com.cloudexpense.user.service;

import com.cloudexpense.user.entity.User;

/**
 * ClassName: UserService
 * Package: com.cloudexpense.user.service
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/7/22 21:45
 * @Version: v1.0
 */
public interface UserService {

    User findByEmail(String email);

}
