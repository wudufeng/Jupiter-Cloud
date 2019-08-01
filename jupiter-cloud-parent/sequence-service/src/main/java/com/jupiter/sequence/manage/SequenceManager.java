package com.jupiter.sequence.manage;

import com.jupiter.sequence.entity.SequenceDefinition;
import com.jupiter.sequence.vo.CreateSequenceRequest;
import com.jupiter.sequence.vo.GetSequenceResponse;
import com.jupiter.sequence.vo.SequenceOperationRequest;
import com.jupiter.sequence.vo.UpdateSequenceRequest;
import com.jupiterframework.manage.GenericManage;


public interface SequenceManager extends GenericManage<SequenceDefinition> {

    Boolean createSequence(CreateSequenceRequest request);


    Boolean updateSequence(Long id, UpdateSequenceRequest request);


    Boolean dropSequence(SequenceOperationRequest request);


    GetSequenceResponse obtainSequence(SequenceOperationRequest request);

}
