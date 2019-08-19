package com.jupiter.sequence.service;

public interface SequenceGenerator {

    String generator(Long tenantId, String seqName);

}
