package com.jupiter.upms.user.manage.impl;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import com.jupiter.upms.user.dao.UserInfoDao;
import com.jupiter.upms.user.entity.UserInfo;
import com.jupiter.upms.user.manage.UserInfoManage;
import com.jupiterframework.manage.impl.GenericManageImpl;
import com.jupiterframework.util.StringUtils;


/**
 * 用户信息 管理服务实现类
 *
 * @author WUDUFENG
 * @since 2019-08-12
 */
@Service
public class UserInfoManageImpl extends GenericManageImpl<UserInfoDao, UserInfo> implements UserInfoManage {

    @Override
    public boolean insert(UserInfo entity) {
        String password = entity.getPassword();
        if (StringUtils.isBlank(password)) {
            password = "123456";
        }
        entity.setPassword(this.encriptPassword(entity.getUsername(), entity.getPassword()));

        return super.insert(entity);
    }


    private String encriptPassword(String username, String password) {
        return DigestUtils.md5Hex(username + password);
    }
}
