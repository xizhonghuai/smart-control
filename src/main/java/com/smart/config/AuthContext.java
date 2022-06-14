package com.smart.config;

/**
 * @ClassName AuthContext
 * @Description: TODO
 * @Author xizhonghuai
 * @Date 2020/4/9
 * @Version V1.0
 **/
public class AuthContext {

    private static AuthContext instance;

    private AuthContext() {
    }

    public static synchronized AuthContext get() {
        if (instance == null) {
            instance = new AuthContext();
        }
        return instance;
    }

    private final ThreadLocal<Object> objThreadLocal = ThreadLocal.withInitial(() -> null);

    public void setObj(Object obj) {
        this.objThreadLocal.set(obj);
    }

    public Object getObj() {
        return this.objThreadLocal.get();
    }

    public void clear() {
          this.objThreadLocal.remove();
    }


}

