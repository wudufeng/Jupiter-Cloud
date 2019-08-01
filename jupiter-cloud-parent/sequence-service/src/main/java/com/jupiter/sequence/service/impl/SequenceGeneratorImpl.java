package com.jupiter.sequence.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.jupiter.sequence.manage.SequenceManager;
import com.jupiter.sequence.service.SequenceGenerator;
import com.jupiter.sequence.vo.GetSequenceResponse;
import com.jupiter.sequence.vo.SequenceOperationRequest;


@Service
public class SequenceGeneratorImpl implements SequenceGenerator {

	@Autowired
	private SequenceManager sequenceManager;

	private Map<String, AtomicSequence> sequenceMap = new HashMap<>();
	private Lock lock = new ReentrantLock();

	@Override
	public String generator(@RequestParam("TenantId") String tenantId, @RequestParam("seqName") String seqName) {
		AtomicSequence seq = sequenceMap.get(seqName);
		if (seq == null) {
			lock.lock();
			try {
				seq = sequenceMap.get(seqName);
				if (seq == null) {
					seq = new AtomicSequence(tenantId, seqName);
					sequenceMap.put(seqName, seq);
				}
			} finally {
				lock.unlock();
			}
		}
		return seq.getAndIncrement();
	}

	private class AtomicSequence {
		private String tenantId;
		private String seqName;
		private AtomicLong currentValue;
		private long cacheMaxValue;
		private GetSequenceResponse sequence;

		private final Lock SEQ_LOCK = new ReentrantLock();

		AtomicSequence(String tenantId, String seqName) {
			this.tenantId = tenantId;
			this.seqName = seqName;
			this.currentValue = new AtomicLong();
			reset();
		}

		private String getAndIncrement() {
			long value = currentValue.getAndIncrement();

			if (value > cacheMaxValue) {
				SEQ_LOCK.lock();
				try {
					value = currentValue.getAndIncrement();
					if (value > cacheMaxValue) {
						reset();
						return getAndIncrement();
					}
				} finally {
					SEQ_LOCK.unlock();
				}
			}

			return String.format(new StringBuilder("%s%s%0").append(sequence.getCharLength()).append("d").toString(),
				sequence.getPrefix(), sequence.getAppendDateFormat() != null
						? DateFormatUtils.format(new Date(), sequence.getAppendDateFormat()) : "",
				value);

		}

		private void reset() {
			SequenceOperationRequest req = new SequenceOperationRequest();
			req.setSeqName(seqName);
			req.setTenantId(tenantId);
			sequence = sequenceManager.obtainSequence(req);
			cacheMaxValue = sequence.getCurrentValue() + sequence.getIncrease() - 1;
			if (cacheMaxValue > sequence.getMaxValue())
				cacheMaxValue = sequence.getMaxValue();
			currentValue.set(sequence.getCurrentValue());
		}
	}
}
