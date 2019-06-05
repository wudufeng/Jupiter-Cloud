package com.jupiterframework.amqp.handler.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 消息记录状态
 * @author hesp
 *
 */
@Getter
@AllArgsConstructor
public enum QueueConsumerRecordStatusEnum {

	UNTREATED(0) ,	//未处理
	
	INVALID(1) ,	//无效
	
	FAIL(2) ,		//失败(异常)
	
	SUCCESS(3) ,	//成功
	
	
	;
	
	
	
	
	public Integer code ;
	
	
	
}
