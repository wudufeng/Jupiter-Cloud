package com.jupiter.cloud.upms.sys.manage;

import com.jupiter.cloud.upms.sys.entity.Role;
import com.jupiter.cloud.upms.sys.pojo.RoleVo;
import com.jupiterframework.manage.GenericManage;

import java.util.List;


/**
 * 角色管理 管理服务类
 *
 * @author jupiter
 * @since 2019-10-24
 */
public interface RoleManage extends GenericManage<Role> {

    List<RoleVo> selecSimpleRoleList(Long tenantId);

}
