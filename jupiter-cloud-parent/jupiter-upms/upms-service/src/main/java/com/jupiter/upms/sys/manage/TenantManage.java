package com.jupiter.upms.sys.manage;

import java.util.List;

import com.jupiter.upms.sys.entity.Tenant;
import com.jupiter.upms.sys.pojo.TenantVo;
import com.jupiterframework.manage.GenericManage;


/**
 * 租户信息 管理服务类
 *
 * @author WUDUFENG
 * @since 2019-08-07
 */
public interface TenantManage extends GenericManage<Tenant> {

    List<TenantVo> selecSimpleTenantList(List<Integer> status);
}
