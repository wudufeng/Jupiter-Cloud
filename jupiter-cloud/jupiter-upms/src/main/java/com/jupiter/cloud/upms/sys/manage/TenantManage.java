package com.jupiter.cloud.upms.sys.manage;

import com.jupiter.cloud.upms.sys.entity.Tenant;
import com.jupiter.cloud.upms.sys.pojo.TenantVo;
import com.jupiterframework.manage.GenericManage;

import java.util.List;


/**
 * 租户信息 管理服务类
 *
 * @author jupiter
 * @since 2019-08-07
 */
public interface TenantManage extends GenericManage<Tenant> {

    List<TenantVo> selecSimpleTenantList(List<Integer> status);
}
