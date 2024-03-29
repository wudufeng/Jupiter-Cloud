package com.jupiterframework.web.apidoc;

import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.jupiterframework.constant.SysRespCodeEnum;
import com.jupiterframework.model.ServiceResponse;

import io.swagger.models.ModelImpl;
import io.swagger.models.Response;
import io.swagger.models.Swagger;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.IntegerProperty;
import io.swagger.models.properties.LongProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;
import io.swagger.models.properties.StringProperty;
import io.swagger.models.refs.RefType;
import springfox.documentation.service.Documentation;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2MapperImpl;


/**
 * 由于web请求统一将接口定义的返回对象包装成ServiceResponse，这里统一将swagger的ModelExample Value重写
 * 
 * @author wudf
 *
 */
@Primary
@Component
public class ServiceModelToSwagger2MapperWrapper extends ServiceModelToSwagger2MapperImpl {

    @SuppressWarnings("deprecation")
    @Override
    public Swagger mapDocumentation(Documentation from) {
        Swagger swagger = super.mapDocumentation(from);

        swagger.getPaths().entrySet().forEach(entry -> {
            entry.getValue().getOperations().forEach(oper -> {

                Response response = oper.getResponses().get(Integer.toString(HttpStatus.OK.value()));
                Property pro = response.getSchema();
                String oriName = pro != null ? pro.getType() : "Void";

                if (pro instanceof ArrayProperty) {
                    oriName = handleArrayProperty(swagger, pro);

                } else if (pro instanceof RefProperty) {
                    RefProperty ref = (RefProperty) pro;
                    ModelImpl model = (ModelImpl) swagger.getDefinitions().get(ref.getSimpleRef());
                    oriName = model.getName();
                    if (oriName.equals(ServiceResponse.class.getSimpleName())) {
                        return;
                    }
                }

                String newName = "ServiceResponse<" + oriName + ">";

                ModelImpl svc = new ModelImpl();
                IntegerProperty code = new IntegerProperty();
                code.setDescription("响应码[表示成功]");
                code.setExample(SysRespCodeEnum.SUCCESS.getCode());
                svc.addProperty("code", code);
                StringProperty msg = new StringProperty();
                msg.setDescription("响应信息");
                msg.setExample("success");
                svc.addProperty("message", msg);
                svc.addProperty("data", pro instanceof LongProperty ? new StringProperty() : pro);
                svc.setType(ModelImpl.OBJECT);
                svc.setName(newName);
                swagger.getDefinitions().put(newName, svc);

                RefProperty newRef = new RefProperty(RefType.DEFINITION.getInternalPrefix() + newName);

                response.setSchema(newRef);
            });
        });

        return swagger;
    }


    private String handleArrayProperty(Swagger swagger, Property pro) {
        ArrayProperty ap = (ArrayProperty) pro;

        String itemType = ap.getItems().getType();
        if (ap.getItems() instanceof RefProperty) {
            ModelImpl model =
                    (ModelImpl) swagger.getDefinitions().get(((RefProperty) ap.getItems()).getSimpleRef());

            if (model != null) {
                itemType = model.getName();
            }
        }
        return "Array<" + itemType + ">";
    }
}
