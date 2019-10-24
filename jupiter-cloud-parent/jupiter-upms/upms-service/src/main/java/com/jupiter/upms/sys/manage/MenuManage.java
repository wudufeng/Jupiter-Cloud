package com.jupiter.upms.sys.manage;

import java.util.List;

import com.jupiter.upms.sys.entity.Menu;
import com.jupiter.upms.sys.pojo.MenuTreeVo;
import com.jupiterframework.manage.GenericManage;


/**
 * 菜单管理 管理服务类
 *
 * @author WUDUFENG
 * @since 2019-08-22
 */
public interface MenuManage extends GenericManage<Menu> {

    /**
     * 以树状结构返回
     * 
     * @param tenantId
     * @return
     */
    List<MenuTreeVo> trees(String parentCode);
}
