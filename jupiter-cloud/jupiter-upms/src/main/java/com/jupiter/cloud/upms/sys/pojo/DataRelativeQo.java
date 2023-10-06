package com.jupiter.cloud.upms.sys.pojo;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataRelativeQo {

    @NotNull
    private Long scopeId;

    private List<Long> instanceIds;
}
