package com.jupiter.upms.sys.manage.impl;

import com.jupiter.upms.sys.dao.TenantDao;
import com.jupiter.upms.sys.entity.Tenant;
import com.jupiter.upms.sys.manage.TenantManage;
import com.jupiterframework.manage.impl.GenericManageImpl;
import org.springframework.stereotype.Service;

/**
 * 租户信息 管理服务实现类
 *
 * @author WUDUFENG
 * @since 2019-08-07
 */
@Service
public class TenantManageImpl extends GenericManageImpl<TenantDao, Tenant> implements TenantManage {

}
