package com.jupiter.cloud.upms.user.service;

import com.jupiter.cloud.upms.user.pojo.UserDto;


/**
 * @author jupiter
 */
public interface UserService {

    /** 保存一个用户信息 */
    Long save(UserDto user);


    void login(UserDto user);
}
