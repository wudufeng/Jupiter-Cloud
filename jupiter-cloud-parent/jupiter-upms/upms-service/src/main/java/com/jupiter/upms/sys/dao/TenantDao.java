package com.jupiter.upms.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jupiter.upms.sys.entity.Tenant;
import com.jupiter.upms.sys.pojo.TenantVo;
import com.jupiterframework.dao.GenericDao;


/**
 * <p>
 * 租户信息 Mapper 接口
 * </p>
 *
 * @author WUDUFENG
 * @since 2019-08-07
 */
public interface TenantDao extends GenericDao<Tenant> {
    List<TenantVo> selecSimpleTenantList(@Param("status") List<Integer> status);
}
