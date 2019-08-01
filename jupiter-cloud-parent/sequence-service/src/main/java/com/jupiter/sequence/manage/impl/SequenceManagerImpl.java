package com.jupiter.sequence.manage.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jupiter.sequence.dao.SequenceDefinitionDao;
import com.jupiter.sequence.entity.SequenceDefinition;
import com.jupiter.sequence.exception.SequenceExceptionCode;
import com.jupiter.sequence.manage.SequenceManager;
import com.jupiter.sequence.vo.CreateSequenceRequest;
import com.jupiter.sequence.vo.GetSequenceResponse;
import com.jupiter.sequence.vo.SequenceOperationRequest;
import com.jupiter.sequence.vo.UpdateSequenceRequest;
import com.jupiterframework.exception.BusinessException;
import com.jupiterframework.manage.impl.GenericManageImpl;
import com.jupiterframework.util.BeanUtils;


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

        SequenceDefinition sequence = BeanUtils.copy(request, SequenceDefinition.class);
        sequence.setCurrentValue(minValue);
        return sequenceDao.insert(sequence) > 0;

    }


    @Override
    public Boolean updateSequence(Long id, UpdateSequenceRequest request) {
        SequenceDefinition seq = com.jupiterframework.util.BeanUtils.copy(request, SequenceDefinition.class);
        seq.setId(id);
        return this.updateById(seq);
    }


    @Override
    public Boolean dropSequence(SequenceOperationRequest request) {

        SequenceDefinition seq = BeanUtils.copy(request, SequenceDefinition.class);
        int rowAffected = sequenceDao.delete(new EntityWrapper<>(seq));
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

        GetSequenceResponse response =
                com.jupiterframework.util.BeanUtils.copy(sequenceDefinition, GetSequenceResponse.class);

        SequenceDefinition setParameter = new SequenceDefinition();
        setParameter.setCurrentValue(sequenceDefinition.getCurrentValue() + sequenceDefinition.getIncrease());
        if (setParameter.getCurrentValue() > sequenceDefinition.getMaxValue()) {
            if (sequenceDefinition.getCycle())
                setParameter.setCurrentValue(sequenceDefinition.getMinValue());
            else
                setParameter.setCurrentValue(sequenceDefinition.getMaxValue() + 1);
        }
        setParameter.setId(sequenceDefinition.getId());
        sequenceDao.updateById(setParameter);

        return response;
    }
}
