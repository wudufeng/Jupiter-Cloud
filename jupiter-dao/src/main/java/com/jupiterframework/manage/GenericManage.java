package com.jupiterframework.manage;

import com.baomidou.mybatisplus.service.IService;
import com.jupiterframework.model.PageQuery;
import com.jupiterframework.model.PageResult;


/**
 * 通用的增删改查 接口
 * 
 * @author WUDUFENG
 *
 * @param <T>
 */
public interface GenericManage<T> extends IService<T> {

	PageResult<T> selectPage(PageQuery<T> query);
}
