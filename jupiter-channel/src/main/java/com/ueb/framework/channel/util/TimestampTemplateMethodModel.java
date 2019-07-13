package com.ueb.framework.channel.util;

import java.util.List;

import freemarker.template.SimpleDate;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;


public class TimestampTemplateMethodModel implements TemplateMethodModelEx {

    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        if (arguments == null || arguments.isEmpty() || !(arguments.get(0) instanceof SimpleDate)) {
            throw new TemplateModelException("timestamp(arg1)函数必须有且只能有一个类型为java.util.Date的参数!");
        }

        return ((SimpleDate) arguments.get(0)).getAsDate().getTime();
    }
}
