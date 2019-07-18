package com.jupiterframework.sequence.dao;

import org.apache.ibatis.annotations.Param;

import com.jupiterframework.dao.GenericDao;
import com.jupiterframework.sequence.entity.SequenceDefinition;


/**
 * <p>
 * 序列定义 Mapper 接口
 * </p>
 *
 * @author WUDUFENG
 * @since 2019-07-17
 */
public interface SequenceDefinitionDao extends GenericDao<SequenceDefinition> {

	SequenceDefinition selectForUpdate(@Param("tenantId") String tenantId, @Param("seqName") String seqName);
}
