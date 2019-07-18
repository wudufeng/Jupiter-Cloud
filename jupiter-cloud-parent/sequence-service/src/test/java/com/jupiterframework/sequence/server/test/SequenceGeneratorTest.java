package com.jupiterframework.sequence.server.test;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jupiterframework.sequence.manage.SequenceManager;
import com.jupiterframework.sequence.vo.CreateSequenceRequest;
import com.jupiterframework.sequence.vo.GetSequenceResponse;
import com.jupiterframework.sequence.vo.SequenceOperationRequest;
import com.jupiterframework.util.BeanUtils;


public class SequenceGeneratorTest {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Resource
	private SequenceManager sequenceGenerator;
	private final static String TENANT_ID = "jupiter";
	private final static String SEQ_NAME = "SEQ";

	@org.junit.Before
	public void before() {
	}

	@org.junit.After
	public void after() {

	}

	@Test
	public void create() {

		CreateSequenceRequest req = new CreateSequenceRequest();
		req.setAppendDateFormat("yyMMdd");
		req.setCharLength(20);
		req.setIncrease(20);
		req.setMaxValue(999999999l);
		req.setMinValue(100000000l);
		req.setCycle(true);
		req.setPrefix("TEST_");
		req.setSeqName(SEQ_NAME);
		req.setTenantId(TENANT_ID);

		sequenceGenerator.createSequence(req);

	}

	@Test
	public void drop() {
		SequenceOperationRequest req = new SequenceOperationRequest();
		req.setTenantId(TENANT_ID);
		req.setSeqName(SEQ_NAME);
		sequenceGenerator.dropSequence(req);
	}

	@Test
	public void testObtain() {
		SequenceOperationRequest req = new SequenceOperationRequest();
		req.setSeqName(SEQ_NAME);
		req.setTenantId(TENANT_ID);

		GetSequenceResponse resp = sequenceGenerator.obtainSequence(req);
		String value =
				String.format(new StringBuilder("%s%s%0").append(resp.getCharLength()).append("d").toString(),
					resp.getPrefix(), resp.getAppendDateFormat() != null
							? DateFormatUtils.format(new Date(), resp.getAppendDateFormat()) : "",
					resp.getCurrentValue());

		logger.debug("seq >> [{}] \r\n {}", value, BeanUtils.toJSONString(resp));
	}
}
