package com.jupiterframework.sequence.web;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jupiterframework.model.PageQuery;
import com.jupiterframework.model.PageResult;
import com.jupiterframework.sequence.entity.SequenceDefinition;
import com.jupiterframework.sequence.manage.SequenceManager;
import com.jupiterframework.sequence.service.SequenceGenerator;
import com.jupiterframework.sequence.vo.CreateSequenceRequest;
import com.jupiterframework.sequence.vo.SequenceOperationRequest;
import com.jupiterframework.sequence.vo.UpdateSequenceRequest;
import com.jupiterframework.web.annotation.MicroService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 序列定义 前端控制器
 *
 * @author WUDUFENG
 * @since 2019-07-17
 */
@Api(tags = "序列定义")
@MicroService
@RequestMapping("/definition")
public class SequenceDefinitionController {
    @Autowired
    private SequenceManager manager;

    @Autowired
    private SequenceGenerator generator;


    @ApiOperation("产生一个序列值")
    @GetMapping
    public String generator(@RequestParam("TenantId") String tenantId,
            @RequestParam("seqName") String seqName) {
        return generator.generator(tenantId, seqName);
    }


    @ApiOperation("产生多个序列值")
    @GetMapping("/batch")
    public List<String> generators(@RequestParam("TenantId") String tenantId,
            @RequestParam("seqName") String seqName, @RequestParam(defaultValue = "10") int count) {
        List<String> list = new ArrayList<String>(count);
        for (int i = 0; i < count; i++) {
            list.add(generator.generator(tenantId, seqName));
        }
        return list;
    }


    @ApiOperation("创建序列")
    @PutMapping
    public Boolean createSequence(@Valid CreateSequenceRequest request) {
        return manager.createSequence(request);
    }


    @ApiOperation("根据ID获取序列信息")
    @GetMapping("/{id}")
    public SequenceDefinition getSequence(@PathVariable Long id) {
        return manager.selectById(id);
    }


    @ApiOperation("根据ID修改序列信息")
    @PostMapping("/{id}")
    public Boolean updateSequence(@PathVariable Long id, UpdateSequenceRequest request) {
        return manager.updateSequence(id, request);
    }


    @ApiOperation("删除序列")
    @DeleteMapping
    public Boolean dropSequence(@Valid SequenceOperationRequest request) {
        return manager.dropSequence(request);
    }


    @ApiOperation(value = "分页查询数据")
    @PostMapping
    public PageResult<SequenceDefinition> selectPage(
            @Valid @RequestBody PageQuery<SequenceDefinition> query) {
        return manager.selectPage(query);
    }
}
