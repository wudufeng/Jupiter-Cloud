package com.jupiterframework.workflow.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.image.ProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jupiterframework.util.BeanUtils;
import com.jupiterframework.web.annotation.MicroService;
import com.jupiterframework.workflow.service.HistoryProcessService;
import com.jupiterframework.workflow.vo.HistoricInstance;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@Api(tags = "执行历史")
@MicroService
public class HistoryProcessServiceImpl implements HistoryProcessService {

    @Resource
    protected RepositoryService repositoryService;

    @Resource
    protected HistoryService historyService;

    @Autowired
    private ProcessEngineConfiguration processEngineConfiguration;


    @Override
    @ApiOperation("获取执行历史")
    @PostMapping(value = "/getHistoryActivity")
    public List<HistoricInstance> getHistoryActivity(String processInstanceId) {
        List<HistoricActivityInstance> historicActivityInstances =
                historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).orderByHistoricActivityInstanceStartTime().asc().list();
        // List<HistoricVariableInstance> historicVariableInstances =
        // historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId).list();
        return BeanUtils.copy(historicActivityInstances, HistoricInstance.class);
    }


    @ApiOperation("获取执行流程跟踪图")
    @RequestMapping(value = "/traceProcess", method = RequestMethod.POST, produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] traceProcess(@RequestParam String processInstanceId) throws IOException {
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        if (historicProcessInstance == null)
            throw new IllegalArgumentException("查询无记录");
        // 获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId());

        ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
        ProcessDefinitionEntity definitionEntity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(historicProcessInstance.getProcessDefinitionId());

        List<HistoricActivityInstance> highLightedActivitList = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).list();
        // 高亮环节id集合
        List<String> highLightedActivitis = new ArrayList<>();
        // 高亮线路id集合
        List<String> highLightedFlows = getHighLightedFlows(definitionEntity, highLightedActivitList);

        for (HistoricActivityInstance tempActivity : highLightedActivitList) {
            String activityId = tempActivity.getActivityId();
            highLightedActivitis.add(activityId);
        }
        // 中文显示的是口口口，设置字体就好了
        // 输出资源内容到相应对象
        byte[] result = new byte[0];
        byte[] buf = new byte[1024];
        int len;
        try (InputStream imageStream = diagramGenerator.generateDiagram(bpmnModel, "png", highLightedActivitis, highLightedFlows, processEngineConfiguration.getActivityFontName(),
            processEngineConfiguration.getLabelFontName(), processEngineConfiguration.getAnnotationFontName(), null, 1.0);) {
            while ((len = imageStream.read(buf, 0, buf.length)) != -1) {
                int oriPos = result.length;
                result = Arrays.copyOf(result, result.length + len);
                System.arraycopy(buf, 0, result, oriPos, len);
            }
        }
        return result;
    }


    private List<String> getHighLightedFlows(ProcessDefinitionEntity processDefinitionEntity, List<HistoricActivityInstance> historicActivityInstances) {
        List<String> highFlows = new ArrayList<>();// 用以保存高亮的线flowId
        for (int i = 0; i < historicActivityInstances.size() - 1; i++) {// 对历史流程节点进行遍历
            ActivityImpl activityImpl = processDefinitionEntity.findActivity(historicActivityInstances.get(i).getActivityId());// 得到节点定义的详细信息
            List<ActivityImpl> sameStartTimeNodes = new ArrayList<>();// 用以保存后需开始时间相同的节点
            ActivityImpl sameActivityImpl1 = processDefinitionEntity.findActivity(historicActivityInstances.get(i + 1).getActivityId());
            // 将后面第一个节点放在时间相同节点的集合里
            sameStartTimeNodes.add(sameActivityImpl1);
            for (int j = i + 1; j < historicActivityInstances.size() - 1; j++) {
                HistoricActivityInstance activityImpl1 = historicActivityInstances.get(j);// 后续第一个节点
                HistoricActivityInstance activityImpl2 = historicActivityInstances.get(j + 1);// 后续第二个节点
                if (activityImpl1.getStartTime().equals(activityImpl2.getStartTime())) {
                    // 如果第一个节点和第二个节点开始时间相同保存
                    ActivityImpl sameActivityImpl2 = processDefinitionEntity.findActivity(activityImpl2.getActivityId());
                    sameStartTimeNodes.add(sameActivityImpl2);
                } else {
                    // 有不相同跳出循环
                    break;
                }
            }
            List<PvmTransition> pvmTransitions = activityImpl.getOutgoingTransitions();// 取出节点的所有出去的线
            for (PvmTransition pvmTransition : pvmTransitions) {
                // 对所有的线进行遍历
                ActivityImpl pvmActivityImpl = (ActivityImpl) pvmTransition.getDestination();
                // 如果取出的线的目标节点存在时间相同的节点里，保存该线的id，进行高亮显示
                if (sameStartTimeNodes.contains(pvmActivityImpl)) {
                    highFlows.add(pvmTransition.getId());
                }
            }
        }
        return highFlows;
    }
}
