package com.jupiterframework.workflow.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.jupiterframework.workflow.vo.ProcessDefinition;


public interface FlowService {

    String deployProcess(MultipartFile processFile);


    List<ProcessDefinition> getProcessDefinitionList(int pageIndex, int pageSize);


    /**
     * 执行流程
     * 
     * @param processDefinitionKey
     * @param uniqueId 指定的唯一ID作为流程实例ID(ProcessInstanceId)
     * @return
     */
    String startProcess(String processDefinitionKey, Long uniqueId);


    void completeListeningTask(Long procInstId, Map<String, Object> processVariables);


    /** 中断流程 */
    void suspend(Long procInstId);


    /** 使流程继续活动 */
    void activate(Long procInstId);


    /** 取消流程 */
    void cancelProcessInstance(Long procInstId, String reason);

}