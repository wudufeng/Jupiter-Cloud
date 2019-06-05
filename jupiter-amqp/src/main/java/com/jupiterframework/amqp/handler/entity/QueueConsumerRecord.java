package com.jupiterframework.amqp.handler.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * system_queue_record
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class QueueConsumerRecord {

	@TableId
	/** id */
	private Long id;

	/** 公司ID */
	private Long companyId;

	/** 链路跟踪号 */
	private String traceId;

	/** 执行当前消息跟踪号 */
	private String spanId;

	/** 发送消息跟踪号 */
	private String parentSpanId;

	/** 来源系统 */
	private String sourceSystemCode;

	/** 目标系统 */
	private String targetSystemCode;

	/** 一级模块 */
	private String module1;

	/** 二级模块 */
	private String module2;

	/** 实体名 */
	private String entityName;

	/** 虚拟主机 */
	private String virtualHost;

	/** 交换机 */
	private String exchange;

	/** 路由键 */
	private String routerKey;

	/** 队列名 */
	private String queue;

	/** 消息ID */
	private String msgId;

	/** 消息内容(Json) */
	private String msg;

	/** 消息md5(json转md5,用于校验消息重复) */
	private String msgMd5;

	/** 备注 */
	private String note;

	/** 处理状态 0-未处理 1-无效消息 2-处理异常 3-处理成功 */
	private Integer status;

	/** 失败次数 */
	private Integer failCount;

	private Date createTime;

	private Date updateTime;
}