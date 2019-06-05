package com.jupiterframework.sequence.service;

public interface SequenceGenerator {

	String generator(String tenantId, String seqName);

}
