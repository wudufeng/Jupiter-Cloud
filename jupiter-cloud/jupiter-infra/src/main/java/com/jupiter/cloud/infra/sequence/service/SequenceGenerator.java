package com.jupiter.cloud.infra.sequence.service;

public interface SequenceGenerator {

    String generator(Long tenantId, String seqName);

}
