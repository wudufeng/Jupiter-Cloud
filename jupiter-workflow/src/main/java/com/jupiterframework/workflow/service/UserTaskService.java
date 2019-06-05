package com.jupiterframework.workflow.service;

import java.util.List;
import java.util.Map;

import com.jupiterframework.workflow.vo.UserTask;


public interface UserTaskService {

    List<UserTask> getTaskList(Long userId, Long candidateUser, Long groupId, Long procInstId, int pageIndex, int pageSize);


    /**
     * 领取任务
     * 
     * @param taskId 任务ID
     * @param assignee 任务领取人
     */
    void claimTask(Long taskId, Long assignee);


    /**
     * 完成任务
     * 
     * @param userId
     * @param procInstId
     */
    String completeTask(Long userId, Long procInstId);


    /**
     * 完成任务，指定参数到下一流程
     * 
     * @param userId
     * @param procInstId
     * @param variables
     */

    String completeTask(Long userId, Long procInstId, Map<String, Object> variables);

}