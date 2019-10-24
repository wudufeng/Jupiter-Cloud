package com.jupiter.upms.sys.web;

import org.springframework.web.bind.annotation.RequestMapping;

import com.jupiter.upms.sys.entity.Role;
import com.jupiter.upms.sys.manage.RoleManage;
import com.jupiterframework.web.GenericController;
import com.jupiterframework.web.annotation.MicroService;

import io.swagger.annotations.Api;


/**
 * 角色管理 前端控制器
 *
 * @author WUDUFENG
 * @since 2019-10-24
 */
@Api(tags = "角色管理")
@MicroService
@RequestMapping("/role")
public class RoleController extends GenericController<RoleManage, Role> {

}
