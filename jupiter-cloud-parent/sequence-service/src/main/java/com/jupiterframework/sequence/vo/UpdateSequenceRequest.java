package com.jupiterframework.sequence.vo;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;


@Data
public class UpdateSequenceRequest {

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
