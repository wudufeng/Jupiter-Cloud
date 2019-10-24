package com.jupiter.upms.sys.manage.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.enums.SqlLike;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jupiter.upms.exception.UpmsExceptionCodeEnum;
import com.jupiter.upms.sys.dao.MenuDao;
import com.jupiter.upms.sys.entity.Menu;
import com.jupiter.upms.sys.manage.MenuManage;
import com.jupiter.upms.sys.pojo.MenuTreeVo;
import com.jupiterframework.exception.BusinessException;
import com.jupiterframework.manage.impl.GenericManageImpl;
import com.jupiterframework.util.StringUtils;


/**
 * 菜单管理 管理服务实现类
 *
 * @author WUDUFENG
 * @since 2019-08-22
 */
@Service
public class MenuManageImpl extends GenericManageImpl<MenuDao, Menu> implements MenuManage {

    @Override
    public boolean insert(Menu menu) {

        if (StringUtils.isNotBlank(menu.getParentCode())) {
            Menu parent = this.selectOne(new EntityWrapper<>(new Menu(null, menu.getParentCode())));
            if (parent == null)
                throw new BusinessException(UpmsExceptionCodeEnum.MENU_NOT_EXISTS, menu.getParentCode());

        } else {
            menu.setParentCode("");
        }

        Wrapper<Menu> wrapper = new EntityWrapper<>(new Menu(menu.getParentCode(), null));
        int count = this.selectCount(wrapper);
        menu.setCode(String.format("%s%02x", menu.getParentCode(), count + 1));

        if (menu.getSort() == null)
            menu.setSort(1);

        return super.insert(menu);
    }


    @Override
    public boolean updateById(Menu entity) {
        Menu updater = new Menu();
        updater.setId(entity.getId());

        // 仅允许修改名称和排序
        boolean changed = false;
        if (entity.getName() != null) {
            updater.setName(entity.getName());
            changed = true;
        }
        if (entity.getSort() != null) {
            updater.setSort(entity.getSort());
            changed = true;

        }
        if (changed)
            return super.updateById(entity);

        return false;
    }


    @Override
    public boolean deleteById(Serializable id) {
        Menu org = super.selectById(id);
        if (org == null || org.getDel())
            return true;

        Wrapper<Menu> wrapper = new EntityWrapper<>(new Menu());
        wrapper.getEntity().setDel(Boolean.FALSE);
        wrapper.like("parent_code", org.getCode(), SqlLike.RIGHT);
        wrapper.ne("id", id);

        int count = this.selectCount(wrapper);

        if (count > 0) {
            throw new BusinessException(UpmsExceptionCodeEnum.MENU_EXIST_CHILD);
        }

        Menu delete = new Menu();
        delete.setId((Long) id);
        delete.setDel(Boolean.TRUE);

        return super.updateById(delete);
    }


    @Override
    public List<MenuTreeVo> trees(String parentCode) {
        Wrapper<Menu> wrapper = new EntityWrapper<>(new Menu());
        wrapper.getEntity().setDel(Boolean.FALSE);
        if (StringUtils.isNotBlank(parentCode)) {
            wrapper.like("parent_code", parentCode, SqlLike.RIGHT);
        }
        wrapper.orderBy("sort");
        List<Menu> all = super.selectList(wrapper);

        List<MenuTreeVo> result = new ArrayList<>();

        Map<String, List<Menu>> map = all.stream().collect(Collectors.groupingBy(Menu::getParentCode));
        // 筛选出顶级节点
        List<Menu> top = all.stream()
            .filter(a -> StringUtils.isBlank(a.getParentCode()) || a.getParentCode().equals(parentCode))
            .collect(Collectors.toList());
        // 将每个节点递归设置子节点
        top.forEach(o -> result.add(this.convert(o, map)));

        return result;
    }


    /** 设置子节点 */
    private MenuTreeVo convert(Menu o, Map<String, List<Menu>> map) {
        MenuTreeVo treeVo = new MenuTreeVo(o.getId(), o.getCode(), o.getName(), o.getParentCode());
        List<Menu> childrens = map.get(o.getCode());
        if (childrens == null)
            return treeVo;

        List<MenuTreeVo> menuTreeVos = new ArrayList<>(childrens.size());
        childrens.forEach(child -> menuTreeVos.add(this.convert(child, map)));

        treeVo.setChildren(menuTreeVos);
        return treeVo;
    }
}
