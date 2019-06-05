package com.jupiterframework.amqp.handler.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jupiterframework.amqp.handler.entity.QueueConsumerRecord;


public interface QueueConsumerRecordDao extends BaseMapper<QueueConsumerRecord> {

    @Update("UPDATE queue_consumer_record SET status = #{status} , note = #{note} , update_time = #{updateTime} ,fail_count = fail_count +1 WHERE id = #{id} and status in(0,1,2)")
    public int updateConsumerRecordByFail(QueueConsumerRecord entity);


    @Select("select fail_count from queue_consumer_record where id = #{id}")
    public int getFailCount(@Param("id") Long id);
}