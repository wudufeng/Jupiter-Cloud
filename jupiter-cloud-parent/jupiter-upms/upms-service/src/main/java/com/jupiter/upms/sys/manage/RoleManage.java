package com.jupiter.upms.sys.manage;

import java.util.List;

import com.jupiter.upms.sys.entity.Role;
import com.jupiter.upms.sys.pojo.RoleVo;
import com.jupiterframework.manage.GenericManage;


/**
 * 角色管理 管理服务类
 *
 * @author WUDUFENG
 * @since 2019-10-24
 */
public interface RoleManage extends GenericManage<Role> {

    List<RoleVo> selecSimpleRoleList(Long tenantId);

}
