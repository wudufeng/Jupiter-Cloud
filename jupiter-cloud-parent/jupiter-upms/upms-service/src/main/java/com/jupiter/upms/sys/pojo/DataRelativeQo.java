package com.jupiter.upms.sys.pojo;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataRelativeQo {

    @NotNull
    private Long scopeId;

    private List<Long> instanceIds;
}
