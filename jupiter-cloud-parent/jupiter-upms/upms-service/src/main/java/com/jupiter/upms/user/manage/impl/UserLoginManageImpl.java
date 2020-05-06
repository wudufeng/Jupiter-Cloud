package com.jupiter.upms.user.manage.impl;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import com.jupiter.upms.user.dao.UserLoginDao;
import com.jupiter.upms.user.entity.UserLogin;
import com.jupiter.upms.user.manage.UserLoginManage;
import com.jupiterframework.manage.impl.GenericManageImpl;
import com.jupiterframework.util.StringUtils;


/**
 * 用户登录信息 管理服务实现类
 *
 * @author WUDUFENG
 * @since 2020-04-25
 */
@Service
public class UserLoginManageImpl extends GenericManageImpl<UserLoginDao, UserLogin>
        implements UserLoginManage {

    @Override
    public boolean add(UserLogin entity) {

        entity.setEmailLoginFlag(Boolean.FALSE);
        entity.setPhoneLoginFlag(Boolean.FALSE);
        entity.setErrorCount(0);

        String password = entity.getPassword();

        if (StringUtils.isBlank(password)) {
            password = "123456";
            entity.setPwdReset(Boolean.TRUE);
        }
        entity.setPassword(this.encriptPassword(entity.getLoginName(), entity.getPassword()));

        return super.add(entity);
    }


    private String encriptPassword(String username, String password) {
        return DigestUtils.md5Hex(username + password);
    }
}
