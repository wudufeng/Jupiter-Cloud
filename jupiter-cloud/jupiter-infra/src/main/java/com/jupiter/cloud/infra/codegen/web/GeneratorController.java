package com.jupiter.cloud.infra.codegen.web;

import com.jupiter.cloud.infra.codegen.pojo.CodeGeneratorVo;
import com.jupiter.cloud.infra.codegen.pojo.GeneratorConfigQo;
import com.jupiter.cloud.infra.codegen.pojo.TableListQo;
import com.jupiter.cloud.infra.codegen.service.GeneratorService;
import com.jupiterframework.model.PageQuery;
import com.jupiterframework.model.PageResult;
import com.jupiterframework.util.ZipUtil;
import com.jupiterframework.web.annotation.MicroService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.util.UUID;


/**
 * 根据表生成CRUD代码
 *
 * @author jupiter
 */
@Tag(name = "根据表生成CRUD代码")
@MicroService
@Slf4j
public class GeneratorController {

//    @Autowired
    private GeneratorService generator;


    @GetMapping(value = "/code/generate", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void generator(GeneratorConfigQo qo,
                          @RequestParam(value = "download", required = false, defaultValue = "true") boolean download,
                          HttpServletRequest request, HttpServletResponse response) {
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);

        // 代码生成器
        if (org.apache.commons.lang3.StringUtils.isBlank(qo.getOutputDir())) {
            qo.setOutputDir(
                System.getProperty("java.io.tmpdir") + File.separatorChar + UUID.randomUUID());
        }

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
