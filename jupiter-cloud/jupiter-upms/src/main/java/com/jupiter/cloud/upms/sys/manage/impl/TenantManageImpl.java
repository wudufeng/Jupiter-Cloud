package com.jupiter.cloud.upms.sys.manage.impl;

import com.jupiter.cloud.upms.sys.dao.TenantDao;
import com.jupiter.cloud.upms.sys.entity.Tenant;
import com.jupiter.cloud.upms.sys.manage.TenantManage;
import com.jupiter.cloud.upms.sys.pojo.TenantVo;
import com.jupiterframework.manage.impl.GenericManageImpl;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 租户信息 管理服务实现类
 *
 * @author jupiter
 * @since 2019-08-07
 */
@Service
public class TenantManageImpl extends GenericManageImpl<TenantDao, Tenant> implements TenantManage {

    @Override
    public List<TenantVo> selecSimpleTenantList(List<Integer> status) {

        return this.baseMapper.selecSimpleTenantList(status);
    }
}
