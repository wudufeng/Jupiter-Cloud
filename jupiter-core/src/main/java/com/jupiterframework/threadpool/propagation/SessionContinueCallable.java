package com.jupiterframework.threadpool.propagation;

import java.util.concurrent.Callable;

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
public class SessionContinueCallable<V> implements Callable<V> {

    private UserInfo userInfo;
    private BaseInfo requestInfo;
    private String uri;// 异步调用时也能获取到原请求的uri

    private Callable<V> callDelegate;


    public SessionContinueCallable(Callable<V> delegate) {
        userInfo = BeanUtils.deepCopy(SessionUtils.currentUser(), UserInfo.class);
        requestInfo = BeanUtils.deepCopy(SessionUtils.getBaseInfo(), BaseInfo.class);
        this.uri = ServiceContext.getServiceCode();
        this.callDelegate = delegate;
    }


    @Override
    public V call() throws Exception {

        begin();
        try {
            return this.callDelegate.call();
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
