package com.jupiter.cloud.upms.sys.web;

import com.jupiter.cloud.upms.sys.entity.Tenant;
import com.jupiter.cloud.upms.sys.manage.TenantManage;
import com.jupiter.cloud.upms.sys.pojo.TenantVo;
import com.jupiterframework.web.GenericController;
import com.jupiterframework.web.annotation.MicroService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


/**
 * 租户信息 前端控制器
 *
 * @author jupiter
 * @since 2019-08-07
 */
@Tag(name = "租户信息")
@MicroService
@RequestMapping("/sys/tenant")
public class TenantController extends GenericController<TenantManage, Tenant> {

    @GetMapping("/dic-data")
    List<TenantVo> selectTenantList(@RequestParam(required = false) List<Integer> status) {
        return this.manage.selecSimpleTenantList(status);
    }
}
