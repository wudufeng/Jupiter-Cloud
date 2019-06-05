package com.jupiterframework.sequence.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class GetSequenceResponse extends SequenceOperationResponse {

	private Long partitionMinValue;
	private Long partitionMaxValue;
	private Integer increase;
	private Long currentValue;
	private Integer charLength;
	private String prefix;
	private String appendDateFormat;
	private Boolean cycle;

}
