package com.jupiter.cloud.upms.sys.manage.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jupiter.cloud.upms.sys.dao.RoleDao;
import com.jupiter.cloud.upms.sys.entity.Role;
import com.jupiter.cloud.upms.sys.manage.RoleManage;
import com.jupiter.cloud.upms.sys.pojo.RoleVo;
import com.jupiterframework.manage.impl.GenericManageImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 角色管理 管理服务实现类
 *
 * @author jupiter
 * @since 2019-10-24
 */
@Service
public class RoleManageImpl extends GenericManageImpl<RoleDao, Role> implements RoleManage {

    @Override
    public boolean removeById(Serializable id) {

        Role role = new Role();
        role.setId((Long) id);
        role.setDel(Boolean.TRUE);
        return super.updateById(role);
    }


    @Override
    public List<RoleVo> selecSimpleRoleList(Long tenantId) {
        QueryWrapper<Role> wrapper = new QueryWrapper<>(new Role(tenantId), "id,name");
        wrapper.getEntity().setDel(Boolean.FALSE);
        List<RoleVo> result = new ArrayList<>();
        for (Role r : this.list(wrapper)){
            RoleVo vo = new RoleVo();
            BeanUtils.copyProperties(r, vo);
            result.add(vo);
        }
        return result;
    }
}
