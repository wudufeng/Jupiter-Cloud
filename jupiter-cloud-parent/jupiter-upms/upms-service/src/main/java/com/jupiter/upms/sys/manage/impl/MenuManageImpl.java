package com.jupiter.upms.sys.manage.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
    public boolean add(Menu menu) {

        if (StringUtils.isNotBlank(menu.getParentCode())) {
            Menu parent = this.getOne(new QueryWrapper<>(new Menu(null, menu.getParentCode())));
            if (parent == null)
                throw new BusinessException(UpmsExceptionCodeEnum.MENU_NOT_EXISTS, menu.getParentCode());

        } else {
            menu.setParentCode("");
        }

        Wrapper<Menu> wrapper = new QueryWrapper<>(new Menu(menu.getParentCode(), null));
        int count = this.count(wrapper);
        menu.setCode(String.format("%s%02x", menu.getParentCode(), count + 1));

        if (menu.getSort() == null)
            menu.setSort(1);

        return super.save(menu);
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
    public boolean remove(Long id) {
        Menu org = super.get(id);
        if (org == null || org.getDel())
            return true;

        QueryWrapper<Menu> wrapper = new QueryWrapper<>(new Menu());
        wrapper.getEntity().setDel(Boolean.FALSE);
        wrapper.likeRight("parent_code", org.getCode());
        wrapper.ne("id", id);

        int count = this.count(wrapper);

        if (count > 0) {
            throw new BusinessException(UpmsExceptionCodeEnum.MENU_EXIST_CHILD);
        }

        Menu delete = new Menu();
        delete.setId(id);
        delete.setDel(Boolean.TRUE);

        return super.updateById(delete);
    }


    @Override
    public List<MenuTreeVo> trees(String parentCode) {
        QueryWrapper<Menu> wrapper = new QueryWrapper<>(new Menu());
        wrapper.getEntity().setDel(Boolean.FALSE);
        if (StringUtils.isNotBlank(parentCode)) {
            wrapper.likeRight("parent_code", parentCode);
        }
        wrapper.orderByAsc("sort");
        List<Menu> all = super.list(wrapper);

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
