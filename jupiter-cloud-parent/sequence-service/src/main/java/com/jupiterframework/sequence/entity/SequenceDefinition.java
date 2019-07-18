package com.jupiterframework.sequence.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.jupiterframework.model.GenericPo;

import io.swagger.annotations.ApiModelProperty;
import lombok.NoArgsConstructor;


/**
 * 序列定义
 *
 * @author WUDUFENG
 * @since 2019-07-17
 */
@lombok.Getter
@lombok.Setter
@lombok.ToString
@lombok.experimental.Accessors(chain = true)
@NoArgsConstructor
@TableName("sequence_definition")
public class SequenceDefinition extends GenericPo {

	private static final long serialVersionUID = 1L;

	/** 主键 */
	@ApiModelProperty(value = "主键")
	private Long id;

	/** 租户编码 */
	@ApiModelProperty(value = "租户编码")
	@TableField("tenant_id")
	private String tenantId;

	/** 序列名称 */
	@ApiModelProperty(value = "序列名称")
	@TableField("seq_name")
	private String seqName;

	/** 最小值 */
	@ApiModelProperty(value = "最小值")
	@TableField("min_value")
	private Long minValue;

	/** 最大值 */
	@ApiModelProperty(value = "最大值")
	@TableField("max_value")
	private Long maxValue;

	/** 增长缓冲区 */
	@ApiModelProperty(value = "增长缓冲区")
	private Integer increase;

	/** 当前值 */
	@ApiModelProperty(value = "当前值")
	@TableField("current_value")
	private Long currentValue;

	/** 是否允许循环 0否，1是 */
	@ApiModelProperty(value = "是否允许循环 0否，1是")
	private Boolean cycle;

	/** 字符长度，不足位左补0 */
	@ApiModelProperty(value = "字符长度，不足位左补0")
	@TableField("char_length")
	private Integer charLength;

	/** 序列前缀 */
	@ApiModelProperty(value = "序列前缀")
	private String prefix;

	/** 日期填充格式 */
	@ApiModelProperty(value = "日期填充格式")
	@TableField("append_date_format")
	private String appendDateFormat;

	public SequenceDefinition(String tenantId, String seqName) {
		super();
		this.tenantId = tenantId;
		this.seqName = seqName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((seqName == null) ? 0 : seqName.hashCode());
		result = prime * result + ((tenantId == null) ? 0 : tenantId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SequenceDefinition other = (SequenceDefinition) obj;
		if (seqName == null) {
			if (other.seqName != null)
				return false;
		} else if (!seqName.equals(other.seqName))
			return false;
		if (tenantId == null) {
			if (other.tenantId != null)
				return false;
		} else if (!tenantId.equals(other.tenantId))
			return false;
		return true;
	}

}
