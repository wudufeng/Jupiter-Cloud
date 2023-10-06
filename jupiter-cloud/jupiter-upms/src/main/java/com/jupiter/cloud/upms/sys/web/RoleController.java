package com.jupiter.cloud.upms.sys.web;

import com.jupiter.cloud.upms.sys.entity.Role;
import com.jupiter.cloud.upms.sys.manage.RoleManage;
import com.jupiter.cloud.upms.sys.pojo.RoleQo;
import com.jupiter.cloud.upms.sys.pojo.RoleVo;
import com.jupiter.cloud.upms.sys.service.RoleService;
import com.jupiterframework.web.GenericController;
import com.jupiterframework.web.annotation.MicroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 角色管理 前端控制器
 *
 * @author jupiter
 * @since 2019-10-24
 */
@Tag(name = "角色管理")
@MicroService
@RequestMapping("/sys/role")
public class RoleController extends GenericController<RoleManage, Role> {

    @Resource
    private RoleService roleService;


    @Operation(summary = "新增/修改角色数据权限")
    @RequestMapping(value = "/organization", method = { RequestMethod.POST, RequestMethod.PUT })
    public Integer saveRoleOrganization(RoleQo roleQo) {
        return roleService.saveRoleOrganization(roleQo);
    }


    @Operation(summary = "新增/修改角色菜单权限")
    @PostMapping(value = "/menu")
    public Integer saveRoleMenu(@RequestParam Long roleId, @RequestParam List<Long> menuIds) {
        return roleService.saveRoleMenu(roleId, menuIds);
    }


    @Operation(summary = "获取角色菜单权限")
    @GetMapping(value = "/menu")
    public Long[] getRoleMenus(@RequestParam Long roleId) {
        return roleService.getRoleMenus(roleId).toArray(new Long[] {});
    }


    @Operation(summary = "获取租户下可用的角色")
    @GetMapping(value = "/dic-data")
    public List<RoleVo> selectRoleList(Long tenantId) {
        return this.manage.selecSimpleRoleList(tenantId);
    }


    @Operation(summary = "获取角色绑定的数据权限")
    @GetMapping(value = "/organization")
    public Long[] getRoleOrganizations(@RequestParam Long roleId) {
        return this.roleService.getRoleOrganizations(roleId).toArray(new Long[] {});
    }
}
