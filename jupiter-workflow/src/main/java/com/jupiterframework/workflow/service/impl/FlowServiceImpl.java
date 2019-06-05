package com.jupiterframework.workflow.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.image.ProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.jupiterframework.util.BeanUtils;
import com.jupiterframework.web.annotation.MicroService;
import com.jupiterframework.workflow.service.FlowService;
import com.jupiterframework.workflow.service.ProcessInstanceIdGenerator;
import com.jupiterframework.workflow.vo.ProcessDefinition;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@MicroService
@RequestMapping(path = "/workflow")
@Api(tags = { "工作流操作接口" })
public class FlowServiceImpl implements FlowService {

    @Resource
    protected RepositoryService repositoryService;

    @Resource
    protected IdentityService identityService;

    @Resource
    private RuntimeService runtimeService;

    @Autowired
    private ProcessEngineConfiguration processEngineConfiguration;

    @Resource
    private ProcessInstanceIdGenerator idGenerator;


    @Override
    @ApiOperation("发布流程")
    @Async
    @RequestMapping(value = "/deployProcess", method = RequestMethod.POST)
    public String deployProcess(MultipartFile processFile) {
        String processName = processFile.getOriginalFilename();
        InputStream inputStream;
        try {
            inputStream = processFile.getInputStream();
            String fileSuffix = processName.substring(processName.lastIndexOf(".") + 1, processName.length());
            Deployment deployment = null;
            if ("ZIP".equalsIgnoreCase(fileSuffix)) {
                ZipInputStream zis = new ZipInputStream(inputStream);
                deployment = repositoryService.createDeployment().addZipInputStream(zis).deploy();
            } else {
                deployment = repositoryService.createDeployment().addInputStream(processName, inputStream).deploy();
            }
            return deployment.getId();
        } catch (IOException e) {
            throw new IllegalArgumentException("文件上传失败", e);
        }
    }


    @Override
    @ApiOperation("获取流程定义")
    @PostMapping(value = "/getProcessDefinitionList")
    public List<ProcessDefinition> getProcessDefinitionList(@RequestParam(defaultValue = "1") int pageIndex, @RequestParam(defaultValue = "10") int pageSize) {
        List<org.activiti.engine.repository.ProcessDefinition> list =
                repositoryService.createProcessDefinitionQuery().orderByProcessDefinitionVersion().asc().listPage(this.getFirstResult(pageIndex, pageSize), pageSize);
        return BeanUtils.copy(list, ProcessDefinition.class);
    }


    @Override
    @ApiOperation("执行流程")
    @PostMapping(value = "/startProcess")
    public String startProcess(@Valid @NotNull @RequestParam(defaultValue = "orderProcess") String processDefinitionKey, @RequestParam @NotNull Long uniqueId) {
        Map<String, Object> context = Collections.singletonMap("uniqueId", uniqueId);
        idGenerator.setProcInstId(uniqueId);// 将指定的唯一ID作为流程的实例ID
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, context);

        return processInstance.getId();
    }


    @ApiOperation("获取流程图")
    @RequestMapping(value = "/getProcessResource", method = RequestMethod.POST, produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getProcessResource(String processDefinitionId) throws IOException {
        // 流程定义对象
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);

        ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();

        byte[] result = new byte[0];
        byte[] buf = new byte[1024];
        int len;
        try (InputStream in = diagramGenerator.generateDiagram(bpmnModel, "png", processEngineConfiguration.getActivityFontName(), processEngineConfiguration.getLabelFontName(),
            processEngineConfiguration.getAnnotationFontName(), null, 1.0);) {
            while ((len = in.read(buf, 0, buf.length)) != -1) {
                int oriPos = result.length;
                result = Arrays.copyOf(result, result.length + len);
                System.arraycopy(buf, 0, result, oriPos, len);
            }
        }
        return result;
    }


    @ApiOperation("获取流程定义")
    @PostMapping(value = "/completeListeningTask")
    @Override
    public void completeListeningTask(Long procInstId, Map<String, Object> processVariables) {
        Assert.notNull(procInstId, "procInstId不能为空");
        runtimeService.signal(procInstId.toString(), processVariables);
    }


    private int getFirstResult(int pageIndex, int pageSize) {
        int firstResult = (pageIndex - 1) * pageSize;
        if (firstResult < 0)
            return 0;
        return firstResult;
    }


    @ApiOperation("中断流程，处于此状态的流程不再执行message等任务")
    @PostMapping(value = "/suspend")
    @Override
    public void suspend(@RequestParam Long procInstId) {
        if (isExists(procInstId))
            runtimeService.suspendProcessInstanceById(procInstId.toString());

    }


    @ApiOperation("使流程继续活动")
    @PostMapping(value = "/activate")
    @Override
    public void activate(@RequestParam Long procInstId) {
        if (isExists(procInstId))
            runtimeService.activateProcessInstanceById(procInstId.toString());

    }


    @ApiOperation("取消流程")
    @PostMapping(value = "/cancelProcessInstance")
    @Override
    public void cancelProcessInstance(@RequestParam Long procInstId, @RequestParam String reason) {
        if (isExists(procInstId))
            runtimeService.deleteProcessInstance(procInstId.toString(), reason);

    }


    private boolean isExists(Long procInstId) {
        return runtimeService.createProcessInstanceQuery().processInstanceId(procInstId.toString()).singleResult() != null;
    }
}
