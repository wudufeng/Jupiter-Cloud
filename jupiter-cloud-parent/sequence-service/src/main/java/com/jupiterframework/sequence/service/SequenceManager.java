package com.jupiterframework.sequence.service;

import com.jupiterframework.sequence.vo.CreateSequenceRequest;
import com.jupiterframework.sequence.vo.CreateSequenceResponse;
import com.jupiterframework.sequence.vo.DropSequenceRequest;
import com.jupiterframework.sequence.vo.DropSequenceResponse;
import com.jupiterframework.sequence.vo.GetSequenceRequest;
import com.jupiterframework.sequence.vo.GetSequenceResponse;


public interface SequenceManager {

	CreateSequenceResponse createSequence(CreateSequenceRequest request);

	DropSequenceResponse dropSequence(DropSequenceRequest request);

	GetSequenceResponse obtainSequence(GetSequenceRequest request);

}
