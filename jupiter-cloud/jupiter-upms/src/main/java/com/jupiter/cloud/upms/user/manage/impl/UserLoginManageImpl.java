package com.jupiter.cloud.upms.user.manage.impl;

import com.jupiter.cloud.upms.user.dao.UserLoginDao;
import com.jupiter.cloud.upms.user.entity.UserLogin;
import com.jupiter.cloud.upms.user.manage.UserLoginManage;
import com.jupiterframework.manage.impl.GenericManageImpl;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;


/**
 * 用户登录信息 管理服务实现类
 *
 * @author jupiter
 * @since 2020-04-25
 */
@Service
public class UserLoginManageImpl extends GenericManageImpl<UserLoginDao, UserLogin>
        implements UserLoginManage {

    @Override
    public boolean save(UserLogin entity) {

        entity.setEmailLoginFlag(Boolean.FALSE);
        entity.setPhoneLoginFlag(Boolean.FALSE);
        entity.setErrorCount(0);

        String password = entity.getPassword();

        if (StringUtils.isBlank(password)) {
            password = "123456";
            entity.setPwdReset(Boolean.TRUE);
        }
        entity.setPassword(this.encriptPassword(entity.getLoginName(), entity.getPassword()));

        return super.save(entity);
    }


    private String encriptPassword(String username, String password) {
        return DigestUtils.md5Hex(username + password);
    }
}
