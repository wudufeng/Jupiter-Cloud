package com.jupiter.upms.web;

import org.springframework.web.bind.annotation.RequestMapping;

import com.jupiter.upms.entity.Tenant;
import com.jupiter.upms.manage.TenantManage;
import com.jupiterframework.web.GenericController;
import com.jupiterframework.web.annotation.MicroService;

import io.swagger.annotations.Api;


/**
 * 租户信息 前端控制器
 *
 * @author WUDUFENG
 * @since 2019-08-07
 */
@Api(tags = "租户信息")
@MicroService
@RequestMapping("/tenant")
public class TenantController extends GenericController<TenantManage, Tenant> {

}
