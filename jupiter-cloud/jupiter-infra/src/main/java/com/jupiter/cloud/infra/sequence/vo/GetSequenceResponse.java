package com.jupiter.cloud.infra.sequence.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class GetSequenceResponse extends SequenceOperationResponse {

	private Integer increase;
	private Long currentValue;
	private Long minValue;
	private Long maxValue;
	private Integer charLength;
	private String prefix;
	private String appendDateFormat;
	private Boolean cycle;

}
