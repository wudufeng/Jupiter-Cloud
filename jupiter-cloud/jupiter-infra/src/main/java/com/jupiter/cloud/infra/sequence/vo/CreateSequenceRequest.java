package com.jupiter.cloud.infra.sequence.vo;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author jupiter
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CreateSequenceRequest extends SequenceOperationRequest {

	@NotNull
	@Max(value = Long.MAX_VALUE)
	@Min(value = 0)
	private Long minValue;

	@NotNull
	@Min(value = 1)
	@Max(value = Long.MAX_VALUE)
	private Long maxValue;

	@NotNull
	@Min(value = 1)
	private Integer increase;

	@Min(value = 1)
	private Integer charLength;

	private String prefix;

	private String appendDateFormat;

	@NotNull
	private Boolean cycle;

}
