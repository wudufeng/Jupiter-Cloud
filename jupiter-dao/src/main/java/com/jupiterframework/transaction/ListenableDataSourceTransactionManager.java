package com.jupiterframework.transaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ListenableDataSourceTransactionManager extends DataSourceTransactionManager implements ApplicationContextAware {

    private static final long serialVersionUID = 919815238788649870L;

    private transient List<TransactionActionListener> listeners = new ArrayList<>(1);


    public ListenableDataSourceTransactionManager(DataSource dataSource) {
        super(dataSource);
    }


    @Override
    protected void doCommit(DefaultTransactionStatus status) {

        for (TransactionActionListener listenr : listeners) {
            listenr.doBeforeCommit();
        }

        super.doCommit(status);
        for (TransactionActionListener listenr : listeners) {
            try {
                listenr.doAfterCommit();
            } catch (Exception e) {
                log.error("{}", listenr.getClass().getName(), e);
            }
        }
    }


    @Override
    protected void doRollback(DefaultTransactionStatus status) {
        for (TransactionActionListener listenr : listeners) {
            try {
                listenr.doBeforeRollback();
            } catch (Exception e) {
                log.error("{}", listenr.getClass().getName(), e);
            }
        }
        super.doRollback(status);
    }


    @Override
    protected void doCleanupAfterCompletion(Object transaction) {
        for (TransactionActionListener listenr : listeners) {
            try {
                listenr.doCleanupAfterCompletion();
            } catch (Exception e) {
                log.error("{}", listenr.getClass().getName(), e);
            }
        }
        super.doCleanupAfterCompletion(transaction);
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, TransactionActionListener> map = applicationContext.getBeansOfType(TransactionActionListener.class);

        for (Entry<String, TransactionActionListener> entry : map.entrySet()) {
            log.debug("TransactionActionListener {}", entry.getKey());
            listeners.add(entry.getValue());
        }

        Collections.sort(listeners, new Comparator<TransactionActionListener>() {

            @Override
            public int compare(TransactionActionListener o1, TransactionActionListener o2) {
                return o1.getClass().getAnnotation(Order.class).value() - o2.getClass().getAnnotation(Order.class).value();
            }
        });
    }

}
