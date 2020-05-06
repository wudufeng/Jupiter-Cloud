package com.jupiter.upms.sys.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jupiter.upms.sys.entity.Tenant;
import com.jupiter.upms.sys.manage.TenantManage;
import com.jupiter.upms.sys.pojo.TenantVo;
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
@RequestMapping("/sys/tenant")
public class TenantController extends GenericController<TenantManage, Tenant> {

    @GetMapping("/dic-data")
    List<TenantVo> selectTenantList(@RequestParam(required = false) List<Integer> status) {
        return this.manage.selecSimpleTenantList(status);
    }
}
