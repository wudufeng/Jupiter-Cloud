package com.jupiter.upms.sys.manage.impl;

import com.jupiter.upms.sys.entity.Role;
import com.jupiter.upms.sys.dao.RoleDao;
import com.jupiter.upms.sys.manage.RoleManage;
import com.jupiterframework.manage.impl.GenericManageImpl;
import org.springframework.stereotype.Service;

/**
 * 角色管理 管理服务实现类
 *
 * @author WUDUFENG
 * @since 2019-10-24
 */
@Service
public class RoleManageImpl extends GenericManageImpl<RoleDao, Role> implements RoleManage {

}
