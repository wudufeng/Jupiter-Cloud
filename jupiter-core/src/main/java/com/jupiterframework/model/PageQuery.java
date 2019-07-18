package com.jupiterframework.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import javax.validation.constraints.Min;

import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 分页查询参数
 */
@Data
@NoArgsConstructor
public class PageQuery<T> implements Serializable {

	private static final long serialVersionUID = -5237453367597292495L;

	@io.swagger.annotations.ApiModelProperty(value = "当前页", example = "1")
	@Min(1)
	private int current = 1;

	@io.swagger.annotations.ApiModelProperty(value = "每页显示条数", example = "10")
	@Min(1)
	private int size = 10;

	@io.swagger.annotations.ApiModelProperty(value = "查询开始时间", example = "10")
	private Date queryBeginTime;

	@io.swagger.annotations.ApiModelProperty(value = "查询结束时间", example = "10")
	private Date queryEndTime;

	/** 实体查询参数 */
	private T condition;

	/** 额外的查询参数, 在xml中使用 */
	@io.swagger.annotations.ApiModelProperty(value = " 额外查询参数")
	private Map<String, Object> extra;
}
