package com.smart.config;

import com.smart.mvc.entity.Account;

import java.util.Objects;

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

    private void setObj(Object obj) {
        this.objThreadLocal.set(obj);
    }

    private Object getObj() {
        return this.objThreadLocal.get();
    }

    public void clear() {
        this.objThreadLocal.remove();
    }

    public void setLoginUser(Account account) {
        setObj(account);
    }

    public Account getLoginUser() {
        return (Account) getObj();
    }

    public Long getLoginUserId() {
        Account account = (Account) getObj();
        return account == null ? ConstantUnit.adminId : account.getId();
    }

    public Long getLoginUserRoleId() {
        Account account = (Account) getObj();
        return account == null ? ConstantUnit.adminRoleId : account.getRoleId();
    }

    public Boolean isAdmin() {
      return Objects.equals(getLoginUserRoleId(),ConstantUnit.adminRoleId);
    }
    public Boolean isAgent() {
        return Objects.equals(getLoginUserRoleId(),ConstantUnit.agentRoleId);
    }
    public Boolean isUser() {
        return Objects.equals(getLoginUserRoleId(),ConstantUnit.userRoleId);
    }


}

