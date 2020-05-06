package com.jupiterframework.workflow.service.impl;

import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.jupiterframework.workflow.service.ProcessInstanceIdGenerator;


@Component
public class ActivitiIDGenerator
        implements org.activiti.engine.impl.cfg.IdGenerator, ProcessInstanceIdGenerator {

    private static final ThreadLocal<String> PROC_INST_ID_HOLDER = new ThreadLocal<>();


    @Override
    public String getNextId() {
        String id = PROC_INST_ID_HOLDER.get();
        if (id != null) {
            // 取出后就不可以用了
            PROC_INST_ID_HOLDER.set(null);
            return id;
        }
        return IdWorker.getIdStr();
    }


    @Override
    public void setProcInstId(Long procInstId) {
        PROC_INST_ID_HOLDER.set(procInstId.toString());
    }
}
