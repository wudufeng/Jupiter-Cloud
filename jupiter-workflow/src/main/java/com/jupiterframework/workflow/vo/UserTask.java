package com.jupiterframework.workflow.vo;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class UserTask {

    private String taskId;

    private String name;

    private Object data;
}
