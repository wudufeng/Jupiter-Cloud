package com.jupiter.upms.sys.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jupiter.upms.sys.entity.Organization;
import com.jupiter.upms.sys.manage.OrganizationManage;
import com.jupiter.upms.sys.pojo.OrganizationTreeVo;
import com.jupiterframework.web.GenericController;
import com.jupiterframework.web.annotation.MicroService;

import io.swagger.annotations.Api;


/**
 * 组织架构 前端控制器
 *
 * @author WUDUFENG
 * @since 2019-08-09
 */
@Api(tags = "组织架构")
@MicroService
@RequestMapping("/organization")
public class OrganizationController extends GenericController<OrganizationManage, Organization> {

    @GetMapping("/trees")
    public List<OrganizationTreeVo> trees(@RequestParam Long tenantId) {
        return manage.trees(tenantId);
    }
}
