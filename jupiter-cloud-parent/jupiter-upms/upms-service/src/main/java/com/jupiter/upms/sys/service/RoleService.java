package com.jupiter.upms.sys.service;

import java.util.List;

import com.jupiter.upms.sys.pojo.RoleQo;


public interface RoleService {
    /**
     * 新增或者修改角色，并且数据权限指定为2-自定义
     * 
     * @return 数据量
     */
    Integer saveRoleOrganization(RoleQo roleQo);


    /**
     * 保存角色菜单权限关系
     * 
     * @return 数据量
     */
    Integer saveRoleMenu(Long roleId, List<Long> menuIds);

}
