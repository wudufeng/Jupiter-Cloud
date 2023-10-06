package com.jupiter.cloud.upms.user.manage.impl;

import com.jupiter.cloud.upms.user.dao.UserInfoDao;
import com.jupiter.cloud.upms.user.entity.UserInfo;
import com.jupiter.cloud.upms.user.manage.UserInfoManage;
import com.jupiterframework.manage.impl.GenericManageImpl;
import org.springframework.stereotype.Service;


/**
 * 用户信息 管理服务实现类
 *
 * @author jupiter
 * @since 2019-08-12
 */
@Service
public class UserInfoManageImpl extends GenericManageImpl<UserInfoDao, UserInfo> implements UserInfoManage {
    @Override
    public boolean save(UserInfo entity) {

        return super.save(entity);
    }


    @Override
    public boolean updateById(UserInfo entity) {
        entity.setUpdateTime(null);
        entity.setCreateTime(null);
        return super.updateById(entity);
    }
}
