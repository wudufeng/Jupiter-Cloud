package com.jupiter.cloud.infra.sequence.vo;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


/**
 * @author jupiter
 */
@Data
public class SequenceOperationRequest {

    @NotNull
    private Long tenantId;

    @NotNull
    @Size(max = 32)
    private String seqName;

}
