package com.jupiter.cloud.upms.sys.web;

import com.jupiter.cloud.upms.sys.entity.Organization;
import com.jupiter.cloud.upms.sys.manage.OrganizationManage;
import com.jupiter.cloud.upms.sys.pojo.DataRelativeQo;
import com.jupiter.cloud.upms.sys.pojo.OrganizationTreeVo;
import com.jupiter.cloud.upms.sys.service.OrganizationService;
import com.jupiterframework.web.GenericController;
import com.jupiterframework.web.annotation.MicroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


/**
 * 组织架构 前端控制器
 *
 * @author jupiter
 * @since 2019-08-09
 */
@Tag(name = "组织架构")
@MicroService
@RequestMapping("/sys/organization")
public class OrganizationController extends GenericController<OrganizationManage, Organization> {
    @Resource
    private OrganizationService service;


    @Operation(summary = "获取机构树")
    @GetMapping("/trees")
    public List<OrganizationTreeVo> trees(@RequestParam(required = false) Long tenantId) {
        return manage.trees(tenantId);
    }


    @Operation(summary = "保存机构默认角色")
    @PostMapping("/role")
    public void saveRole(@RequestParam List<Long> organizationIds, @RequestParam List<Long> roleIds) {
        for (Long org : organizationIds) {
            service.saveRole(new DataRelativeQo(org, roleIds));
        }
    }


    @Operation(summary = "获取用户绑定的角色")
    @GetMapping(value = "/role")
    public Long[] getOrganizationRole(@RequestParam Long organizationId) {
        return service.getOrganizationRole(organizationId).toArray(new Long[] {});
    }
}
