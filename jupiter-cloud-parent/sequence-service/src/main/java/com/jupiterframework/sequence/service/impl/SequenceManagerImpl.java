package com.jupiterframework.sequence.service.impl;

import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Resource;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;

import com.jupiterframework.exception.BusinessException;
import com.jupiterframework.sequence.exception.SequenceExceptionCode;
import com.jupiterframework.sequence.model.SequenceDefinition;
import com.jupiterframework.sequence.service.SequenceManager;
import com.jupiterframework.sequence.vo.CreateSequenceRequest;
import com.jupiterframework.sequence.vo.CreateSequenceResponse;
import com.jupiterframework.sequence.vo.DropSequenceRequest;
import com.jupiterframework.sequence.vo.DropSequenceResponse;
import com.jupiterframework.sequence.vo.GetSequenceRequest;
import com.jupiterframework.sequence.vo.GetSequenceResponse;
import com.jupiterframework.web.annotation.MicroService;


@MicroService
public class SequenceManagerImpl implements SequenceManager {
	@Resource
	private SqlSessionTemplate daosupport;

	private int tableCount = 10;

	public SequenceManagerImpl() {
	}

	public void init() {
		for (int i = 0; i < tableCount; i++) {
			SequenceDefinition sequence = new SequenceDefinition();
			this.setTableIndex(sequence, i);
			try {
				daosupport.selectList(SequenceDefinition.NAMESPACE + ".selectList", sequence, new RowBounds(0, 1));
			} catch (BadSqlGrammarException e) {
				daosupport.update(SequenceDefinition.NAMESPACE + ".createTable", sequence);
			}
		}
	}

	private void setTableIndex(SequenceDefinition sequence, int tableIndex) {
		sequence.addExtraParameter("tableIndex", tableIndex);
	}

	@Override
	@PostMapping("/create")
	public CreateSequenceResponse createSequence(CreateSequenceRequest request) {
		CreateSequenceResponse response = new CreateSequenceResponse();

		SequenceDefinition sequence = new SequenceDefinition();
		BeanUtils.copyProperties(request, sequence);

		Long minValue = sequence.getMinValue();
		Long maxValue = sequence.getMaxValue();

		Long partitionValue = (maxValue - minValue + 1) / tableCount;// 区间值

		for (int i = 0; i < tableCount; i++) {
			Long partitionMinValue = minValue + i * partitionValue;// 区间最小
			Long partitionMaxValue = tableCount == i + 1 ? maxValue : partitionMinValue + partitionValue - 1;// 区间最大

			sequence.setPartitionMinValue(partitionMinValue);
			sequence.setPartitionMaxValue(partitionMaxValue);
			sequence.setCurrentValue(partitionMinValue);
			this.setTableIndex(sequence, i);

			daosupport.insert(SequenceDefinition.NAMESPACE + ".insert", sequence);

		}
		return response;
	}

	@Override
	@PostMapping("/drop")
	public DropSequenceResponse dropSequence(DropSequenceRequest request) {

		DropSequenceResponse response = new DropSequenceResponse();

		SequenceDefinition parameter = new SequenceDefinition();
		parameter.setTenantId(request.getTenantId());
		parameter.setSeqName(request.getSeqName());

		for (int i = 0; i < tableCount; i++) {
			this.setTableIndex(parameter, i);
			daosupport.delete(SequenceDefinition.NAMESPACE + ".delete", parameter);
		}
		return response;
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
	public GetSequenceResponse obtainSequence(GetSequenceRequest request) {

		SequenceDefinition parameter = new SequenceDefinition();
		parameter.setTenantId(request.getTenantId());
		parameter.setSeqName(request.getSeqName());
		this.setTableIndex(parameter, ThreadLocalRandom.current().nextInt(tableCount));

		SequenceDefinition sequenceDefinition =
				daosupport.selectOne(SequenceDefinition.NAMESPACE + ".selectForUpdate", parameter);
		if (sequenceDefinition == null) {
			throw new BusinessException(SequenceExceptionCode.UNDEFINED, request.getSeqName());
		}

		if (sequenceDefinition.getCurrentValue() > sequenceDefinition.getPartitionMaxValue()) {
			throw new BusinessException(SequenceExceptionCode.OVER_MAXVALUE, request.getSeqName());
		}

		GetSequenceResponse response =
				com.jupiterframework.util.BeanUtils.copy(sequenceDefinition, GetSequenceResponse.class);

		SequenceDefinition setParameter = new SequenceDefinition();
		setParameter.setCurrentValue(sequenceDefinition.getCurrentValue() + sequenceDefinition.getIncrease());
		if (setParameter.getCurrentValue() > sequenceDefinition.getPartitionMaxValue()) {
			if (sequenceDefinition.getCycle())
				setParameter.setCurrentValue(sequenceDefinition.getPartitionMinValue());
			else
				setParameter.setCurrentValue(sequenceDefinition.getPartitionMaxValue() + 1);
		}

		parameter.addExtraParameter("setParameter", setParameter);

		daosupport.update(SequenceDefinition.NAMESPACE + ".updateByEntity", parameter);

		return response;
	}
}
