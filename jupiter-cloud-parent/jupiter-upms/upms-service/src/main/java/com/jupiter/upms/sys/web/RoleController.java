package com.jupiter.upms.sys.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jupiter.upms.sys.entity.Role;
import com.jupiter.upms.sys.manage.RoleManage;
import com.jupiter.upms.sys.pojo.RoleQo;
import com.jupiter.upms.sys.pojo.RoleVo;
import com.jupiter.upms.sys.service.RoleService;
import com.jupiterframework.web.GenericController;
import com.jupiterframework.web.annotation.MicroService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 角色管理 前端控制器
 *
 * @author WUDUFENG
 * @since 2019-10-24
 */
@Api(tags = "角色管理")
@MicroService
@RequestMapping("/sys/role")
public class RoleController extends GenericController<RoleManage, Role> {

    @Autowired
    private RoleService roleService;


    @ApiOperation(value = "新增/修改角色数据权限")
    @RequestMapping(value = "/organization", method = { RequestMethod.POST, RequestMethod.PUT })
    public Integer saveRoleOrganization(RoleQo roleQo) {
        return roleService.saveRoleOrganization(roleQo);
    }


    @ApiOperation(value = "新增/修改角色菜单权限")
    @PostMapping(value = "/menu")
    public Integer saveRoleMenu(@RequestParam Long roleId, @RequestParam List<Long> menuIds) {
        return roleService.saveRoleMenu(roleId, menuIds);
    }


    @ApiOperation(value = "获取租户下可用的角色")
    @GetMapping(value = "/dic-data")
    public List<RoleVo> selectRoleList(Long tenantId) {
        return this.manage.selecSimpleRoleList(tenantId);
    }
}
