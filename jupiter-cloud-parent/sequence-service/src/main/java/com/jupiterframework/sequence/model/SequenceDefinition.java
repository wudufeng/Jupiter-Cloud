package com.jupiterframework.sequence.model;

import java.util.HashMap;
import java.util.Map;

import com.jupiterframework.model.GenericPo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class SequenceDefinition extends GenericPo {

	private static final long serialVersionUID = 1233722859063738913L;

	public static final String NAMESPACE = SequenceDefinition.class.getName();

	private String tenantId;
	private String seqName;
	private Long minValue;
	private Long maxValue;
	private Long partitionMinValue;
	private Long partitionMaxValue;
	private Integer increase;
	private Long currentValue;
	private Integer charLength;
	private String prefix;
	private String appendDateFormat;
	private Boolean cycle;

	/** 自定义的额外参数 */
	private Map<String, Object> extraParameter;

	public void addExtraParameter(String extraParameterKey, Object parameterValue) {
		if (extraParameter == null) {
			extraParameter = new HashMap<String, Object>();
		}
		extraParameter.put(extraParameterKey, parameterValue);
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
