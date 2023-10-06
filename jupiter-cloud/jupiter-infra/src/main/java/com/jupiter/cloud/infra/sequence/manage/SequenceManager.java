package com.jupiter.cloud.infra.sequence.manage;

import com.jupiter.cloud.infra.sequence.entity.SequenceDefinition;
import com.jupiter.cloud.infra.sequence.vo.CreateSequenceRequest;
import com.jupiter.cloud.infra.sequence.vo.GetSequenceResponse;
import com.jupiter.cloud.infra.sequence.vo.SequenceOperationRequest;
import com.jupiter.cloud.infra.sequence.vo.UpdateSequenceRequest;
import com.jupiterframework.manage.GenericManage;


public interface SequenceManager extends GenericManage<SequenceDefinition> {

    Boolean createSequence(CreateSequenceRequest request);


    Boolean updateSequence(Long id, UpdateSequenceRequest request);


    Boolean dropSequence(SequenceOperationRequest request);


    GetSequenceResponse obtainSequence(SequenceOperationRequest request);

}
