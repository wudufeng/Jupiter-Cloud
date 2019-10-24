package com.jupiter.upms.sys.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jupiter.upms.sys.entity.Menu;
import com.jupiter.upms.sys.manage.MenuManage;
import com.jupiter.upms.sys.pojo.MenuTreeVo;
import com.jupiterframework.web.GenericController;
import com.jupiterframework.web.annotation.MicroService;

import io.swagger.annotations.Api;


/**
 * 菜单管理 前端控制器
 *
 * @author WUDUFENG
 * @since 2019-08-22
 */
@Api(tags = "菜单管理")
@MicroService
@RequestMapping("/menu")
public class MenuController extends GenericController<MenuManage, Menu> {

    @GetMapping("/trees")
    public List<MenuTreeVo> trees(@RequestParam(required = false) String parentCode) {
        return manage.trees(parentCode);
    }
}
