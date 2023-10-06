package com.jupiter.cloud.infra.codegen.service;

import com.jupiter.cloud.infra.codegen.pojo.CodeGeneratorVo;
import com.jupiter.cloud.infra.codegen.pojo.GeneratorConfigQo;
import com.jupiter.cloud.infra.codegen.pojo.TableListQo;
import com.jupiterframework.model.PageQuery;
import com.jupiterframework.model.PageResult;


public interface GeneratorService {

    boolean generateCurdCode(GeneratorConfigQo qo);


    PageResult<CodeGeneratorVo> getTableList(PageQuery<TableListQo> pageQuery);
}
