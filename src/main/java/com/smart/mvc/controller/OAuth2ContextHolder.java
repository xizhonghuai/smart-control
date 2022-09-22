package com.smart.mvc.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OAuth2ContextHolder {
    private static ThreadLocal<Map<String, Object>> threadLocal = new InheritableThreadLocal();

    public OAuth2ContextHolder() {
    }

    public static void set(String key, Object value) {
        Map<String, Object> map = (Map)threadLocal.get();
        if (map == null) {
            map = new HashMap();
            threadLocal.set(map);
        }

        ((Map)map).put(key, value);
    }

    public static Object get(String key) {
        Map<String, Object> map = (Map)threadLocal.get();
        if (map == null) {
            map = new HashMap();
            threadLocal.set(map);
        }

        return ((Map)map).get(key);
    }

    public static Map<String, Object> getAll() {
        return (Map)threadLocal.get();
    }

    public static void setAccessToken(String token) {
        set("currentAccessToken", token);
    }

    public static String getAccessToken() {
        return StringUtil.getObjectValue(get("currentAccessToken"));
    }

    public static void setUserId(Long userId) {
        set("currentUserId", userId);
    }

    public static Long getUserId() {
        return (Long)get("currentUserId");
    }

    public static void setNickName(String nickName) {
        set("currentNickName", nickName);
    }

    public static String getNickName() {
        return StringUtil.getObjectValue(get("currentNickName"));
    }

    public static void setUsername(String username) {
        set("currentUser", username);
    }

    public static String getUsername() {
        return StringUtil.getObjectValue(get("currentUser"));
    }

    public static void setApplicationId(Long applicationId) {
        set("currentApplicationId", applicationId);
    }

    public static Long getApplicationId() {
        return (Long)get("currentApplicationId");
    }

    public static void setApplicationCode(String applicationCode) {
        set("currentApplicationCode", applicationCode);
    }

    public static String getApplicationCode() {
        return StringUtil.getObjectValue(get("currentApplicationCode"));
    }

    public static void setExpiration(Date expiration) {
        set("currentExpiration", expiration);
    }

    public static Date getExpiration() {
        return (Date)get("currentExpiration");
    }

    public static Map<String, String> getAdditional() {
        return (Map)get("currentAdditional");
    }

    public static void setAdditional(Map<String, String> additional) {
        set("currentAdditional", additional);
    }

    public static void remove() {
        threadLocal.remove();
    }

    static class StringUtil {
        StringUtil() {
        }

        static String getObjectValue(Object obj) {
            return obj == null ? "" : obj.toString();
        }
    }

    private static class CommonConstants {
        static final String CONTEXT_KEY_USER_ID = "currentUserId";
        static final String CONTEXT_KEY_TENANT_ID = "currentTenantId";
        static final String CONTEXT_KEY_NICK_NAME = "currentNickName";
        static final String CONTEXT_KEY_USER_NAME = "currentUser";
        static final String CONTEXT_KEY_ACCESS_TOKEN = "currentAccessToken";
        static final String CONTEXT_KEY_APPLICATION_ID = "currentApplicationId";
        static final String CONTEXT_KEY_APPLICATION_CODE = "currentApplicationCode";
        static final String CONTEXT_KEY_EXPIRATION = "currentExpiration";
        static final String CONTEXT_ADDITIONAL = "currentAdditional";

        private CommonConstants() {
        }
    }
}