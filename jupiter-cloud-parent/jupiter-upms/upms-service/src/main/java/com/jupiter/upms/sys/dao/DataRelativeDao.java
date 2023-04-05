package com.jupiter.upms.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jupiter.upms.sys.entity.DataRelative;
import com.jupiterframework.dao.GenericDao;


/**
 * <p>
 * 一对多关系映射表 Mapper 接口
 * </p>
 *
 * @author WUDUFENG
 * @since 2020-04-27
 */
public interface DataRelativeDao extends GenericDao<DataRelative> {

    int updateEnable(@Param("refType") Integer refType, @Param("scopeId") Long scopeId,
            @Param("instanceIds") List<Long> instanceIds, @Param("enable") Boolean enable);


    int save(@Param("refType") Integer refType, @Param("scopeId") Long scopeId,
            @Param("instanceIds") List<Long> instanceIds);


    List<Long> getDataRelativeList(@Param("refType") Integer refType, @Param("scopeIds") List<Long> scopeIds);
}
