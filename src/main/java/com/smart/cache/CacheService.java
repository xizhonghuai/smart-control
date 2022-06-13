package com.smart.cache;

import com.smart.config.SpringUtil;

import java.util.List;

/**
 * @author xizhonghuai
 * @date 2022/05/05
 */
public interface CacheService {

    void setValue(String key, Object value);

    <T> void setValues(String key, List<T> values);

    Object getValue(String key);

    <T> List<T> getValues(String key, Class<T> targetClass);

    void invalid(String key);

    Boolean isEmpty(String key);

    static CacheService getCacheService() {
        return SpringUtil.getObject(CacheService.class);
    }
}
