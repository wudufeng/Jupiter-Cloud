package com.jupiter.upms.user.manage.impl;

import org.springframework.stereotype.Service;

import com.jupiter.upms.user.dao.UserInfoDao;
import com.jupiter.upms.user.entity.UserInfo;
import com.jupiter.upms.user.manage.UserInfoManage;
import com.jupiterframework.manage.impl.GenericManageImpl;


/**
 * 用户信息 管理服务实现类
 *
 * @author WUDUFENG
 * @since 2019-08-12
 */
@Service
public class UserInfoManageImpl extends GenericManageImpl<UserInfoDao, UserInfo> implements UserInfoManage {
    @Override
    public boolean add(UserInfo entity) {

        return super.add(entity);
    }


    @Override
    public boolean updateById(UserInfo entity) {
        entity.setUpdateTime(null);
        entity.setCreateTime(null);
        return super.updateById(entity);
    }
}
