package com.jupiterframework.workflow.service;

import java.util.List;

import com.jupiterframework.workflow.vo.HistoricInstance;


/**
 * 执行历史
 * 
 * @author wudf
 *
 */
public interface HistoryProcessService {

    List<HistoricInstance> getHistoryActivity(String processInstanceId);
}
