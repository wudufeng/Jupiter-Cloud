package com.jupiter.cloud.infra.sequence.web;

import com.jupiter.cloud.infra.sequence.entity.SequenceDefinition;
import com.jupiter.cloud.infra.sequence.manage.SequenceManager;
import com.jupiter.cloud.infra.sequence.service.SequenceGenerator;
import com.jupiter.cloud.infra.sequence.vo.CreateSequenceRequest;
import com.jupiter.cloud.infra.sequence.vo.SequenceOperationRequest;
import com.jupiter.cloud.infra.sequence.vo.UpdateSequenceRequest;
import com.jupiterframework.model.PageQuery;
import com.jupiterframework.model.PageResult;
import com.jupiterframework.web.annotation.MicroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


/**
 * 序列定义 前端控制器
 *
 * @author jupiter
 * @since 2019-07-17
 */
@Tag(name = "序列定义")
@MicroService
@RequestMapping("/definition")
public class SequenceDefinitionController {
    @Resource
    private SequenceManager manager;

    @Resource
    private SequenceGenerator generator;


    @Operation(summary = "产生一个序列值", description = "")
    @GetMapping
    public String generator(@RequestParam("TenantId") Long tenantId,
                            @RequestParam("seqName") String seqName) {
        return generator.generator(tenantId, seqName);
    }


    @Operation(summary = "产生多个序列值")
    @GetMapping("/batch")
    public List<String> generators(@RequestParam("TenantId") Long tenantId,
                                   @RequestParam("seqName") String seqName, @RequestParam(defaultValue = "10") int count) {
        List<String> list = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            list.add(generator.generator(tenantId, seqName));
        }
        return list;
    }


    @Operation(summary = "创建序列")
    @PutMapping
    public Boolean createSequence(@Valid CreateSequenceRequest request) {
        return manager.createSequence(request);
    }


    @Operation(summary = "根据ID获取序列信息")
    @GetMapping("/{id}")
    public SequenceDefinition getSequence(@PathVariable Long id) {
        return manager.getById(id);
    }


    @Operation(summary = "根据ID修改序列信息")
    @PostMapping("/{id}")
    public Boolean updateSequence(@PathVariable Long id, UpdateSequenceRequest request) {
        return manager.updateSequence(id, request);
    }


    @Operation(summary = "删除序列")
    @DeleteMapping
    public Boolean dropSequence(@Valid SequenceOperationRequest request) {
        return manager.dropSequence(request);
    }


    @Operation(summary = "分页查询数据")
    @PostMapping
    public PageResult<SequenceDefinition> selectPage(
            @Valid @RequestBody PageQuery<SequenceDefinition> query) {
        return manager.selectPage(query);
    }
}
