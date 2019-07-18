package com.jupiterframework.sequence.manage.impl;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jupiterframework.exception.BusinessException;
import com.jupiterframework.manage.impl.GenericManageImpl;
import com.jupiterframework.model.PageQuery;
import com.jupiterframework.model.PageResult;
import com.jupiterframework.sequence.dao.SequenceDefinitionDao;
import com.jupiterframework.sequence.entity.SequenceDefinition;
import com.jupiterframework.sequence.exception.SequenceExceptionCode;
import com.jupiterframework.sequence.manage.SequenceManager;
import com.jupiterframework.sequence.vo.CreateSequenceRequest;
import com.jupiterframework.sequence.vo.GetSequenceResponse;
import com.jupiterframework.sequence.vo.SequenceOperationRequest;


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
	public Boolean dropSequence(SequenceOperationRequest request) {

		int rowAffected = sequenceDao.delete(
			new EntityWrapper<SequenceDefinition>(new SequenceDefinition(request.getTenantId(), request.getSeqName())));
		return rowAffected > 0;
	}

	@Override
	public PageResult<SequenceDefinition> selectPage(PageQuery<SequenceDefinition> query) {
		return super.selectPage(query);
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
