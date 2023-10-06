package com.jupiter.cloud.upms.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.jupiter.cloud.upms.exception.UpmsExceptionCodeEnum;
import com.jupiter.cloud.upms.user.entity.UserLogin;
import com.jupiter.cloud.upms.user.manage.UserInfoManage;
import com.jupiter.cloud.upms.user.manage.UserLoginManage;
import com.jupiter.cloud.upms.user.pojo.UserDto;
import com.jupiter.cloud.upms.user.service.UserService;
import com.jupiterframework.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 
 * @author jupiter
 *
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserInfoManage userInfoManage;
    @Autowired
    private UserLoginManage userLoginManage;


    @Transactional
    @Override
    public Long save(UserDto user) {

        long count = userLoginManage.count(new QueryWrapper<>(new UserLogin(user.getLoginName())));
        if (count > 0) {
            throw new BusinessException(UpmsExceptionCodeEnum.USER_EXISTS, user.getLoginName());
        }

        if (StringUtils.isBlank(user.getNickname())) {
            user.setNickname(user.getLoginName());
        }

        Long userId = IdWorker.getId();
        user.setUserId(userId);
        userInfoManage.save(user);

        UserLogin login = new UserLogin();
        login.setUserId(userId);
        login.setLoginName(user.getLoginName());
        login.setPassword(user.getPassword());
        userLoginManage.save(login);

        return user.getUserId();
    }


    @Override
    public void login(UserDto user) {
        // TODO Auto-generated method stub

    }

}
