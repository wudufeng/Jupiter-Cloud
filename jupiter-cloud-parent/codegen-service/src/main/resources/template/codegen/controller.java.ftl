package ${package.Controller};

import org.springframework.web.bind.annotation.RequestMapping;

<#if restControllerStyle>
import com.jupiterframework.web.annotation.MicroService;
<#else>import org.springframework.stereotype.Controller;
</#if><#if superControllerClassPackage??>
import ${superControllerClassPackage};</#if>
import ${package.Entity}.${entity};
import ${package.Service}.${table.serviceName};

import io.swagger.annotations.Api;

/**
 * ${table.comment} 前端控制器
 *
 * @author ${author}
 * @since ${date}
 */
@Api(tags = "${table.comment}")
<#if restControllerStyle>
@MicroService
<#else>
@Controller
</#if>
@RequestMapping("<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass}<${table.serviceName}, ${entity}> {
<#else>
public class ${table.controllerName} {
</#if>

}
</#if>
