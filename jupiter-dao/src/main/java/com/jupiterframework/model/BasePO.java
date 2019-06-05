package com.jupiterframework.model;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class BasePO implements Serializable {
	private static final long serialVersionUID = 3513396245165567182L;

	/** 创建时间 */
	private java.util.Date createTime;

	/** 修改时间 */
	private java.util.Date updateTime;

}
