package com.jupiter.codegen.web;

import java.io.File;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.jupiter.codegen.pojo.CodeGeneratorVo;
import com.jupiter.codegen.pojo.GeneratorConfigQo;
import com.jupiter.codegen.pojo.TableListQo;
import com.jupiter.codegen.service.GeneratorService;
import com.jupiterframework.model.PageQuery;
import com.jupiterframework.model.PageResult;
import com.jupiterframework.util.ZipUtil;
import com.jupiterframework.web.annotation.MicroService;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;


/**
 * 根据表生成CRUD代码
 *
 */
@Api(tags = "根据表生成CRUD代码")
@MicroService
@Slf4j
public class GeneratorController {

    @Autowired
    private GeneratorService generator;


    @GetMapping(value = "/code/generate", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void generator(GeneratorConfigQo qo,
            @RequestParam(value = "download", required = false, defaultValue = "true") boolean download,
            HttpServletRequest request, HttpServletResponse response) {
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);

        // 代码生成器
        if (org.apache.commons.lang3.StringUtils.isBlank(qo.getOutputDir()))
            qo.setOutputDir(
                System.getProperty("java.io.tmpdir") + File.separatorChar + UUID.randomUUID().toString());

        generator.generateCurdCode(qo);

        response.setHeader("Content-Disposition",
            "attachment;filename=code-generate-" + qo.getModuleName() + ".zip");
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        try {
            ZipUtil.toZip(qo.getOutputDir(), qo.getModuleName(), response.getOutputStream(), true);
        } catch (Exception e) {
            log.warn("", e);
        }
    }


    @PostMapping(value = "/table/list")
    public PageResult<CodeGeneratorVo> getTableList(@RequestBody @Valid PageQuery<TableListQo> pageQuery) {
        return generator.getTableList(pageQuery);
    }
}
