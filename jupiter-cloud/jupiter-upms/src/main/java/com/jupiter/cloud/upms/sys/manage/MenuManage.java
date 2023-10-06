package com.jupiter.cloud.upms.sys.manage;

import com.jupiter.cloud.upms.sys.entity.Menu;
import com.jupiter.cloud.upms.sys.pojo.MenuTreeVo;
import com.jupiter.cloud.upms.sys.pojo.MenuVo;
import com.jupiterframework.manage.GenericManage;

import java.util.List;


/**
 * 菜单管理 管理服务类
 *
 * @author jupiter
 * @since 2019-08-22
 */
public interface MenuManage extends GenericManage<Menu> {

    List<Menu> getTopMenuList();


    /**
     * 以树状结构返回
     * 
     * @param tenantId
     * @return
     */
    List<MenuTreeVo> trees(String parentCode);


    /**
     * 根据父级code查询子菜单，
     * 
     * @param parentCode
     * @return menu增加扩展属性
     */
    List<MenuVo> getMenuList(String parentCode);

}
