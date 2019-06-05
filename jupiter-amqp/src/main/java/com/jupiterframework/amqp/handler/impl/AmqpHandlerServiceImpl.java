package com.jupiterframework.amqp.handler.impl;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.Span;
import org.springframework.dao.DuplicateKeyException;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jupiterframework.amqp.core.MessageThreadLocalContext;
import com.jupiterframework.amqp.handler.AmqpHandlerService;
import com.jupiterframework.amqp.handler.dao.QueueConsumerRecordDao;
import com.jupiterframework.amqp.handler.entity.QueueConsumerRecord;
import com.jupiterframework.amqp.handler.entity.QueueConsumerRecordStatusEnum;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class AmqpHandlerServiceImpl implements AmqpHandlerService {

	@Value("${spring.rabbitmq.virtualHost:}")
	private String virtualHost;
	@Value("${baseinfo.systemId:}")
	private String systemId;

	@Value("${spring.application.name:}")
	private String applicationName;

	@Autowired
	private QueueConsumerRecordDao queueConsumerRecordDao;

	/**
	 * 用于启动检查表是否存在
	 */
	@PostConstruct
	protected void initQueueConsumerRecord() {
		Wrapper<QueueConsumerRecord> wrapper = new EntityWrapper<>();
		wrapper.eq("id", "-1");
		// 判断字段是否匹配
		queueConsumerRecordDao.selectList(wrapper);
	}

	@Override
	public Long insertMessage(Span span, Message message) {

		String msgStr = null;
		byte[] msgByte = message.getBody();
		QueueConsumerRecordStatusEnum status = QueueConsumerRecordStatusEnum.UNTREATED;
		String note = "";

		if (msgByte == null || msgByte.length == 0) {
			msgStr = "";
			status = QueueConsumerRecordStatusEnum.INVALID;
			note = "Empty message";
		} else {
			msgStr = new String(msgByte);
		}
		QueueConsumerRecord queueConsumerRecord = new QueueConsumerRecord();
		MessageProperties mp = message.getMessageProperties();

		// queueConsumerRecord.setCompanyId(Long.valueOf(SessionUtils.getCompanyId()));
		queueConsumerRecord.setTraceId(span.traceIdString());
		queueConsumerRecord.setSpanId(Span.idToHex(span.getSpanId()));
		queueConsumerRecord.setParentSpanId((String) mp.getHeaders().get(Span.SPAN_ID_NAME));// 直接使用span.getSpanId有可能导致拿到的是上一个线程的ID
		// queueConsumerRecord.setSourceSystemCode(SessionUtils.getSystemId());
		queueConsumerRecord.setTargetSystemCode(systemId);
		queueConsumerRecord.setModule1(mp.getAppId());
		queueConsumerRecord.setModule2(applicationName);
		queueConsumerRecord.setEntityName(mp.getTargetBean() != null ? mp.getTargetBean().toString() : "");
		queueConsumerRecord.setVirtualHost(virtualHost);
		queueConsumerRecord.setExchange(mp.getReceivedExchange());
		queueConsumerRecord.setRouterKey(mp.getReceivedRoutingKey());
		queueConsumerRecord.setQueue(mp.getConsumerQueue());
		queueConsumerRecord.setMsgId(mp.getMessageId());
		queueConsumerRecord.setMsg(msgStr);
		queueConsumerRecord.setMsgMd5(DigestUtils.md5Hex(msgStr));

		queueConsumerRecord.setNote(note);
		queueConsumerRecord.setStatus(status.code);
		queueConsumerRecord.setFailCount(0);

		try {
			queueConsumerRecordDao.insert(queueConsumerRecord);
		} catch (DuplicateKeyException e) {
			log.warn("消息记录{}已存在", queueConsumerRecord, e);
			return 0L;
		}
		// 正常消息才返回messageId
		return status == QueueConsumerRecordStatusEnum.UNTREATED ? queueConsumerRecord.getId() : 0;
	}

	@Override
	public int consumeFail(Long messageId, Exception e) {
		QueueConsumerRecord queueConsumerRecord = new QueueConsumerRecord();

		StringBuilder sb = new StringBuilder();
		sb.append(e.toString());

		queueConsumerRecord.setId(messageId);
		queueConsumerRecord.setStatus(QueueConsumerRecordStatusEnum.FAIL.code);
		queueConsumerRecord.setNote(sb.toString());

		try {
			// 消费失败
			int rowAffected = queueConsumerRecordDao.updateConsumerRecordByFail(queueConsumerRecord);
			if (rowAffected != 1) {
				log.warn("rowAffected[{}, 消息记录[{}]不存在或消息已经已处理成功!", rowAffected, messageId);
			}

		} catch (Exception e2) {
			log.error("消息[{}]消费处理失败，更新状态失败 ", messageId, e2);
		}

		return queueConsumerRecordDao.getFailCount(messageId);
	}

	@Override
	public void consumeSucc(Long messageId) {
		// 消费成功，如果当前无事务，则在此处更新
		Long messageIdAfterTransaction = MessageThreadLocalContext.takeConsumerMessage();
		if (messageIdAfterTransaction != null) {
			// 如果业务处理存在事务，事务管理器提交事务后已将数据清理， 此处获取为空，不需要再执行

			QueueConsumerRecord queueConsumerRecord = new QueueConsumerRecord();
			queueConsumerRecord.setStatus(QueueConsumerRecordStatusEnum.SUCCESS.code);

			Wrapper<QueueConsumerRecord> w = new EntityWrapper<>();
			w.eq("id", messageId);
			w.ne("status", QueueConsumerRecordStatusEnum.SUCCESS.code);

			try {
				int rowAffected = queueConsumerRecordDao.update(queueConsumerRecord, w);
				if (rowAffected != 1) {
					log.warn("rowAffected[{}, 消息记录[{}]不存在或消息已经已处理成功!", rowAffected, messageId);
				}
			} catch (Exception e) {
				// ignore
				log.warn("业务处理已成功，消息[{}]更新状态失败，忽略!", messageIdAfterTransaction, e);
			}
		}

	}

}
