package com.jupiterframework.sequence.manage;

import com.jupiterframework.manage.GenericManage;
import com.jupiterframework.sequence.entity.SequenceDefinition;
import com.jupiterframework.sequence.vo.CreateSequenceRequest;
import com.jupiterframework.sequence.vo.GetSequenceResponse;
import com.jupiterframework.sequence.vo.SequenceOperationRequest;


public interface SequenceManager extends GenericManage<SequenceDefinition> {

	Boolean createSequence(CreateSequenceRequest request);

	Boolean dropSequence(SequenceOperationRequest request);

	GetSequenceResponse obtainSequence(SequenceOperationRequest request);

}
