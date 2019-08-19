package com.jupiter.sequence.vo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;


@Data
public class SequenceOperationRequest {

    @NotNull
    private Long tenantId;

    @NotNull
    @Size(max = 32)
    private String seqName;

}
