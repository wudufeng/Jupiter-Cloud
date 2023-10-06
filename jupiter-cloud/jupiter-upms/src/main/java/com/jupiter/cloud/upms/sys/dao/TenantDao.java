package com.jupiter.cloud.upms.sys.dao;

import com.jupiter.cloud.upms.sys.entity.Tenant;
import com.jupiter.cloud.upms.sys.pojo.TenantVo;
import com.jupiterframework.dao.GenericDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * <p>
 * 租户信息 Mapper 接口
 * </p>
 *
 * @author jupiter
 * @since 2019-08-07
 */
public interface TenantDao extends GenericDao<Tenant> {
    List<TenantVo> selecSimpleTenantList(@Param("status") List<Integer> status);
}
