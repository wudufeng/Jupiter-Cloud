package com.jupiterframework.workflow.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jupiterframework.web.annotation.MicroService;
import com.jupiterframework.workflow.service.UserTaskService;
import com.jupiterframework.workflow.vo.UserTask;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;


@Api(tags = "工作流用户任务服务")
@RequestMapping(path = "/workflow")
@Slf4j
@MicroService
public class UserTaskServiceImpl implements UserTaskService {

    @Resource
    private TaskService taskService;


    @Override
    @ApiOperation("获取个人任务列表")
    @PostMapping(value = "/getTaskList")
    public List<UserTask> getTaskList(Long userId, Long candidateUser, Long groupId, Long procInstId, @RequestParam(defaultValue = "1") int pageIndex,
            @RequestParam(defaultValue = "10") int pageSize) {
        TaskQuery tq = taskService.createTaskQuery();
        if (userId != null)
            tq = tq.taskAssignee(userId.toString());

        if (candidateUser != null)
            tq = tq.taskCandidateUser(candidateUser.toString());

        if (groupId != null)
            tq = tq.taskCandidateGroup(groupId.toString()).active();

        if (procInstId != null)
            tq = tq.processInstanceId(procInstId.toString());

        List<Task> list = tq.includeTaskLocalVariables().includeProcessVariables().listPage(this.getFirstResult(pageIndex, pageSize), pageSize);

        List<UserTask> result = new ArrayList<>();
        for (Task t : list) {
            result.add(new UserTask(t.getId(), t.getName(), t.getProcessVariables()));
        }
        return result;
    }


    @Override
    @ApiOperation("领取任务")
    @PostMapping(value = "/claimTask")
    public void claimTask(@RequestParam Long taskId, @RequestParam Long assignee) {
        Assert.notNull(taskId, "任务ID不能为空");
        Assert.notNull(assignee, "任务领取人不能为空");
        taskService.claim(taskId.toString(), assignee.toString());
    }


    @Override
    @ApiOperation("完成任务")
    @PostMapping(value = "/completeTask")
    public String completeTask(@Valid @NotBlank @RequestParam Long userId, @NotNull @RequestParam Long procInstId) {

        Map<String, Object> variables = new HashMap<>(2);
        return this.completeTask(userId, procInstId, variables);
    }


    @Override
    @ApiOperation("完成任务")
    @PostMapping(value = "/completeTaskWithVariables")
    public String completeTask(@Valid @NotBlank @RequestParam Long userId, @NotNull @RequestParam Long procInstId, Map<String, Object> variables) {

        Assert.notNull(procInstId, "uniqueId不能为空");

        Task task = taskService.createTaskQuery().processInstanceId(procInstId.toString()).active().singleResult();
        if (task != null) {

            // 当前节点操作人
            variables.put("assignee", userId);
            variables.put("uniqueId", procInstId);
            taskService.complete(task.getId(), variables);
            log.info("流程 {} 完成任务{} ", procInstId, task.getId());
            return task.getId();
        } else {
            log.debug("流程{}无任务需要处理", procInstId);
            // throw new IllegalArgumentException("无任务需要处理");
            return null;
        }
    }


    private int getFirstResult(int pageIndex, int pageSize) {
        int firstResult = (pageIndex - 1) * pageSize;
        if (firstResult < 0)
            return 0;
        return firstResult;
    }
}
