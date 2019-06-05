package com.jupiterframework.sequence.vo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;


@Data
public class SequenceOperationRequest {

	@NotNull
	@Size(max = 32)
	private String tenantId;

	@NotNull
	@Size(max = 32)
	private String seqName;

}
