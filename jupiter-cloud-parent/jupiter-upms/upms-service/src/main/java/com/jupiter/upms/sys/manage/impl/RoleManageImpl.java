package com.jupiter.upms.sys.manage.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jupiter.upms.sys.dao.RoleDao;
import com.jupiter.upms.sys.entity.Role;
import com.jupiter.upms.sys.manage.RoleManage;
import com.jupiter.upms.sys.pojo.RoleVo;
import com.jupiterframework.manage.impl.GenericManageImpl;
import com.jupiterframework.util.BeanUtils;


/**
 * 角色管理 管理服务实现类
 *
 * @author WUDUFENG
 * @since 2019-10-24
 */
@Service
public class RoleManageImpl extends GenericManageImpl<RoleDao, Role> implements RoleManage {

    @Override
    public boolean remove(Long id) {

        Role role = new Role();
        role.setId(id);
        role.setDel(Boolean.TRUE);
        return super.updateById(role);
    }


    @Override
    public List<RoleVo> selecSimpleRoleList(Long tenantId) {
        QueryWrapper<Role> wrapper = new QueryWrapper<>(new Role(tenantId), "id,name");
        wrapper.getEntity().setDel(Boolean.FALSE);

        return BeanUtils.copy(this.list(wrapper), RoleVo.class);
    }
}
