package com.jupiter.upms.sys.manage.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jupiter.upms.exception.UpmsExceptionCodeEnum;
import com.jupiter.upms.sys.dao.MenuDao;
import com.jupiter.upms.sys.entity.Menu;
import com.jupiter.upms.sys.manage.MenuManage;
import com.jupiter.upms.sys.pojo.MenuTreeVo;
import com.jupiter.upms.sys.pojo.MenuVo;
import com.jupiterframework.exception.BusinessException;
import com.jupiterframework.manage.impl.GenericManageImpl;
import com.jupiterframework.util.BeanUtils;
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
    public List<Menu> getTopMenuList() {
        Menu example = new Menu();
        example.setParentCode(Menu.TOP_LEVEL_PARENT_CODE);
        example.setDel(Boolean.FALSE);
        return this.list(new QueryWrapper<>(example));
    }


    @Override
    public boolean add(Menu menu) {

        if (StringUtils.isNotBlank(menu.getParentCode())) {
            Menu parent = this.getOne(new QueryWrapper<>(new Menu(null, menu.getParentCode())));
            if (parent == null)
                throw new BusinessException(UpmsExceptionCodeEnum.MENU_NOT_EXISTS, menu.getParentCode());

        } else {
            menu.setParentCode(Menu.TOP_LEVEL_PARENT_CODE);
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
        MenuTreeVo treeVo = BeanUtils.copy(o, MenuTreeVo.class);
        List<Menu> childrens = map.get(o.getCode());
        if (childrens == null)
            return treeVo;

        List<MenuTreeVo> menuTreeVos = new ArrayList<>(childrens.size());
        childrens.forEach(child -> menuTreeVos.add(this.convert(child, map)));

        treeVo.setChildren(menuTreeVos);
        return treeVo;
    }


    @Override
    public List<MenuVo> getMenuList(String parentCode) {
        if (parentCode == null)
            parentCode = Menu.TOP_LEVEL_PARENT_CODE;// 最顶层菜单
        QueryWrapper<Menu> query = new QueryWrapper<>(new Menu(parentCode, null));
        query.getEntity().setDel(Boolean.FALSE);
        List<MenuVo> menus = BeanUtils.copy(this.list(query), MenuVo.class);

        if (!menus.isEmpty()) {
            Set<String> codes = menus.stream().collect(Collectors.toMap(Menu::getCode, a -> a)).keySet();
            Map<String/* parentCode */, Long> childrenCount = getChildrenCount(codes);
            menus.forEach(x -> x.setHasChildren(
                childrenCount.containsKey(x.getCode()) && childrenCount.get(x.getCode()) > 0));
        }
        return menus;
    }


    private Map<String/* parentCode */, Long> getChildrenCount(Collection<String> parentCodes) {
        QueryWrapper<Menu> countQuery = new QueryWrapper<>(new Menu());
        countQuery.getEntity().setDel(Boolean.FALSE);
        countQuery.in("parent_code", parentCodes);
        countQuery.select("parent_code parentCode,count(id) count");
        countQuery.groupBy("parent_code");

        Map<String, Long> result = new HashMap<>(parentCodes.size());
        this.baseMapper.selectMaps(countQuery)
            .forEach(x -> result.put((String) x.get("parentCode"), (Long) x.get("count")));

        return result;
    }
}
