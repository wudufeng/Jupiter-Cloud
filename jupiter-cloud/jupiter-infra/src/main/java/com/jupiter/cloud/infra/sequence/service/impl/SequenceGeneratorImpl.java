package com.jupiter.cloud.infra.sequence.service.impl;

import com.jupiter.cloud.infra.sequence.manage.SequenceManager;
import com.jupiter.cloud.infra.sequence.service.SequenceGenerator;
import com.jupiter.cloud.infra.sequence.vo.GetSequenceResponse;
import com.jupiter.cloud.infra.sequence.vo.SequenceOperationRequest;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


@Service
public class SequenceGeneratorImpl implements SequenceGenerator {

    @Resource
    private SequenceManager sequenceManager;

    private final Map<String, AtomicSequence> sequenceMap = new HashMap<>();
    private final Lock lock = new ReentrantLock();


    @Override
    public String generator(Long tenantId,
                            String seqName) {
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
        private final Long tenantId;
        private final String seqName;
        private final AtomicLong currentValue;
        private long cacheMaxValue;
        private GetSequenceResponse sequence;

        private final Lock SEQ_LOCK = new ReentrantLock();


        AtomicSequence(Long tenantId, String seqName) {
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

            return String.format(
                    "%s%s%0" + sequence.getCharLength() + "d",
                    sequence.getPrefix(),
                    sequence.getAppendDateFormat() != null
                            ? DateFormatUtils.format(new Date(), sequence.getAppendDateFormat())
                            : "",
                    value);

        }


        private void reset() {
            SequenceOperationRequest req = new SequenceOperationRequest();
            req.setSeqName(seqName);
            req.setTenantId(tenantId);
            sequence = sequenceManager.obtainSequence(req);
            cacheMaxValue = sequence.getCurrentValue() + sequence.getIncrease() - 1;
            if (cacheMaxValue > sequence.getMaxValue()) {
                cacheMaxValue = sequence.getMaxValue();
            }
            currentValue.set(sequence.getCurrentValue());
        }
    }
}
