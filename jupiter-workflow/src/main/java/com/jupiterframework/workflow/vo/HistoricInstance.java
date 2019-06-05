package com.jupiterframework.workflow.vo;

import java.util.Date;

import lombok.Data;


@Data
public class HistoricInstance {
    /** The unique identifier of this historic activity instance. */
    private String id;

    /** The unique identifier of the activity in the process */
    private String activityId;

    /** The display name for the activity */
    private String activityName;

    /** The XML tag of the activity as in the process file */
    private String activityType;

    /** Process definition reference */
    private String processDefinitionId;

    /** Process instance reference */
    private String processInstanceId;

    /** Execution reference */
    private String executionId;

    /** The corresponding task in case of task activity */
    private String taskId;

    /** The called process instance in case of call activity */
    private String calledProcessInstanceId;

    /** Assignee in case of user task activity */
    private String assignee;

    /** Time when the activity instance started */
    private Date startTime;

    /** Time when the activity instance ended */
    private Date endTime;

    /** Difference between {@link #getEndTime()} and {@link #getStartTime()}. */
    private Long durationInMillis;

    /** Returns the tenant identifier for the historic activity */
    private String tenantId;
}
