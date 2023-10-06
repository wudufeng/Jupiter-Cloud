package com.jupiter.cloud.infra.sequence.dao;

import com.jupiter.cloud.infra.sequence.entity.SequenceDefinition;
import com.jupiterframework.dao.GenericDao;
import org.apache.ibatis.annotations.Param;


/**
 * <p>
 * 序列定义 Mapper 接口
 * </p>
 *
 * @author jupiter
 * @since 2019-07-17
 */
public interface SequenceDefinitionDao extends GenericDao<SequenceDefinition> {

    SequenceDefinition selectForUpdate(@Param("tenantId") Long tenantId, @Param("seqName") String seqName);
}
