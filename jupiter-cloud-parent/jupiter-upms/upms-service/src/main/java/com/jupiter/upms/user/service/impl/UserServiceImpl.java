package com.jupiter.upms.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.jupiter.upms.exception.UpmsExceptionCodeEnum;
import com.jupiter.upms.user.entity.UserLogin;
import com.jupiter.upms.user.manage.UserInfoManage;
import com.jupiter.upms.user.manage.UserLoginManage;
import com.jupiter.upms.user.pojo.UserDto;
import com.jupiter.upms.user.service.UserService;
import com.jupiterframework.exception.BusinessException;
import com.jupiterframework.util.StringUtils;


/**
 * 
 * @author WUDUFENG
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

        int count = userLoginManage.count(new QueryWrapper<>(new UserLogin(user.getLoginName())));
        if (count > 0) {
            throw new BusinessException(UpmsExceptionCodeEnum.USER_EXISTS, user.getLoginName());
        }

        if (StringUtils.isBlank(user.getNickname())) {
            user.setNickname(user.getLoginName());
        }

        Long userId = IdWorker.getId();
        user.setUserId(userId);
        userInfoManage.add(user);

        UserLogin login = new UserLogin();
        login.setUserId(userId);
        login.setLoginName(user.getLoginName());
        login.setPassword(user.getPassword());
        userLoginManage.add(login);

        return user.getUserId();
    }


    @Override
    public void login(UserDto user) {
        // TODO Auto-generated method stub

    }

}
