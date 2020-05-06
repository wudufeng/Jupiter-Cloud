package com.jupiter.upms.user.service;

import com.jupiter.upms.user.pojo.UserDto;


public interface UserService {

    /** 保存一个用户信息 */
    Long save(UserDto user);


    void login(UserDto user);
}
