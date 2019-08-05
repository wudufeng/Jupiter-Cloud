package com.jupiter.codegen.service;

import com.jupiter.codegen.pojo.CodeGeneratorVo;
import com.jupiter.codegen.pojo.GeneratorConfigQo;
import com.jupiter.codegen.pojo.TableListQo;
import com.jupiterframework.model.PageQuery;
import com.jupiterframework.model.PageResult;


public interface GeneratorService {

    void generateCurdCode(GeneratorConfigQo qo);


    PageResult<CodeGeneratorVo> getTableList(PageQuery<TableListQo> pageQuery);
}
