package com.jupiterframework.threadpool.propagation;

import com.jupiterframework.context.ServiceContext;
import com.jupiterframework.model.BaseInfo;
import com.jupiterframework.model.UserInfo;
import com.jupiterframework.util.BeanUtils;
import com.jupiterframework.util.SessionUtils;


/**
 * 支持线程调用时候传递session
 * 
 * @author wudf
 *
 * @param <V>
 */

// @org.apache.skywalking.apm.toolkit.trace.TraceCrossThread
public class SessionContinueRunnable implements Runnable {

    private UserInfo userInfo;
    private BaseInfo requestInfo;
    private String uri;

    private Runnable runDelegate;


    public SessionContinueRunnable(Runnable delegate) {
        userInfo = BeanUtils.deepCopy(SessionUtils.currentUser(), UserInfo.class);
        requestInfo = BeanUtils.deepCopy(SessionUtils.getBaseInfo(), BaseInfo.class);
        this.uri = ServiceContext.getServiceCode();
        this.runDelegate = delegate;
    }


    @Override
    public void run() {

        begin();
        try {
            this.runDelegate.run();
        } finally {
            close();
        }
    }


    protected void begin() {

        ServiceContext.setServiceCode(uri);
        SessionUtils.setUserInfo(this.userInfo);
        SessionUtils.setBaseInfo(this.requestInfo);
    }


    protected void close() {
        ServiceContext.setServiceCode(null);
        SessionUtils.setUserInfo(null);
        SessionUtils.setBaseInfo(null);

    }
}
