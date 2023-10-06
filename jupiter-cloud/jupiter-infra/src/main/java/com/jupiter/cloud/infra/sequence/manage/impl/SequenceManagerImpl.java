package com.jupiter.cloud.infra.sequence.manage.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jupiter.cloud.infra.sequence.dao.SequenceDefinitionDao;
import com.jupiter.cloud.infra.sequence.entity.SequenceDefinition;
import com.jupiter.cloud.infra.sequence.exception.SequenceExceptionCode;
import com.jupiter.cloud.infra.sequence.manage.SequenceManager;
import com.jupiter.cloud.infra.sequence.vo.CreateSequenceRequest;
import com.jupiter.cloud.infra.sequence.vo.GetSequenceResponse;
import com.jupiter.cloud.infra.sequence.vo.SequenceOperationRequest;
import com.jupiter.cloud.infra.sequence.vo.UpdateSequenceRequest;
import com.jupiterframework.exception.BusinessException;
import com.jupiterframework.manage.impl.GenericManageImpl;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;



/**
 * @author jupiter
 */
@Service
public class SequenceManagerImpl extends GenericManageImpl<SequenceDefinitionDao, SequenceDefinition>
        implements SequenceManager {
    @Resource
    private SequenceDefinitionDao sequenceDao;


    public SequenceManagerImpl() {
    }


    @Override
    public Boolean createSequence(CreateSequenceRequest request) {
        Long minValue = request.getMinValue();
        Long maxValue = request.getMaxValue();

        if (maxValue - minValue < 10) {
            throw new BusinessException(SequenceExceptionCode.ERR_MAXVALUE, maxValue, minValue);
        }

        SequenceDefinition sequence = new SequenceDefinition();
        BeanUtils.copyProperties(request, sequence);
        sequence.setCurrentValue(minValue);
        return sequenceDao.insert(sequence) > 0;

    }


    @Override
    public Boolean updateSequence(Long id, UpdateSequenceRequest request) {
        SequenceDefinition seq = new SequenceDefinition();
        BeanUtils.copyProperties(request, seq);
        seq.setId(id);
        if (request.getPrefix() == null) {
            seq.setPrefix("");
        }
        if (request.getAppendDateFormat() == null) {
            seq.setAppendDateFormat("");
        }
        return this.updateById(seq);
    }


    @Override
    public Boolean dropSequence(SequenceOperationRequest request) {

        SequenceDefinition seq = new SequenceDefinition();
        BeanUtils.copyProperties(request, seq);
        int rowAffected = sequenceDao.delete(new QueryWrapper<>(seq));
        return rowAffected > 0;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public GetSequenceResponse obtainSequence(SequenceOperationRequest request) {

        SequenceDefinition sequenceDefinition =
                sequenceDao.selectForUpdate(request.getTenantId(), request.getSeqName());
        if (sequenceDefinition == null) {
            throw new BusinessException(SequenceExceptionCode.UNDEFINED, request.getSeqName());
        }

        if (sequenceDefinition.getCurrentValue() > sequenceDefinition.getMaxValue()) {
            throw new BusinessException(SequenceExceptionCode.OVER_MAXVALUE, request.getSeqName());
        }

        GetSequenceResponse response = new GetSequenceResponse();
        BeanUtils.copyProperties(sequenceDefinition, response);

        SequenceDefinition setParameter = new SequenceDefinition();
        setParameter.setCurrentValue(sequenceDefinition.getCurrentValue() + sequenceDefinition.getIncrease());
        if (setParameter.getCurrentValue() > sequenceDefinition.getMaxValue()) {
            if (sequenceDefinition.getCycle()) {
                setParameter.setCurrentValue(sequenceDefinition.getMinValue());
            } else {
                setParameter.setCurrentValue(sequenceDefinition.getMaxValue() + 1);
            }
        }
        setParameter.setId(sequenceDefinition.getId());
        sequenceDao.updateById(setParameter);

        return response;
    }
}
