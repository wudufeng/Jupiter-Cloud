package com.jupiter.upms.sys.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jupiter.upms.sys.entity.Organization;
import com.jupiter.upms.sys.manage.OrganizationManage;
import com.jupiter.upms.sys.pojo.DataRelativeQo;
import com.jupiter.upms.sys.pojo.OrganizationTreeVo;
import com.jupiter.upms.sys.service.OrganizationService;
import com.jupiterframework.web.GenericController;
import com.jupiterframework.web.annotation.MicroService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 组织架构 前端控制器
 *
 * @author WUDUFENG
 * @since 2019-08-09
 */
@Api(tags = "组织架构")
@MicroService
@RequestMapping("/sys/organization")
public class OrganizationController extends GenericController<OrganizationManage, Organization> {
    @Autowired
    private OrganizationService service;


    @ApiOperation("获取机构树")
    @GetMapping("/trees")
    public List<OrganizationTreeVo> trees(@RequestParam(required = false) Long tenantId) {
        return manage.trees(tenantId);
    }


    @ApiOperation("保存机构默认角色")
    @PostMapping("/role")
    public void saveRole(@RequestParam List<Long> organizationIds, @RequestParam List<Long> roleIds) {
        for (Long org : organizationIds) {
            service.saveRole(new DataRelativeQo(org, roleIds));
        }
    }
}
