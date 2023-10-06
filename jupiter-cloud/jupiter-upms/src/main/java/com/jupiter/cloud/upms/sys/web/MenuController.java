package com.jupiter.cloud.upms.sys.web;

import com.jupiter.cloud.upms.sys.entity.Menu;
import com.jupiter.cloud.upms.sys.manage.MenuManage;
import com.jupiter.cloud.upms.sys.pojo.MenuTreeVo;
import com.jupiter.cloud.upms.sys.pojo.MenuVo;
import com.jupiterframework.web.GenericController;
import com.jupiterframework.web.annotation.MicroService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


/**
 * 菜单管理 前端控制器
 *
 * @author jupiter
 * @since 2019-08-22
 */
@Tag(name = "菜单管理")
@MicroService
@RequestMapping("/sys/menu")
public class MenuController extends GenericController<MenuManage, Menu> {

    @GetMapping("/top")
    public List<Menu> getTopMenuList() {
        return manage.getTopMenuList();
    }


    @GetMapping("/trees")
    public List<MenuTreeVo> trees(@RequestParam(required = false) String parentCode) {
        return manage.trees(parentCode);
    }


    @GetMapping("/list")
    public List<MenuVo> getMenuList(@RequestParam String parentCode) {
        return this.manage.getMenuList(parentCode);
    }
}
