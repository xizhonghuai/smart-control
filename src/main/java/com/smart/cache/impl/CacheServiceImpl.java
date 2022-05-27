package com.smart.cache.impl;

import cn.hutool.json.JSONUtil;
import com.smart.cache.CacheService;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author xizhonghuai
 * @date 2022/05/05
 */
@Service
@Primary
@EnableScheduling
public class CacheServiceImpl implements SchedulingConfigurer, CacheService {
    private final static String CRON = "0 0/5 * * * ?";//每5分钟检查
    private static final long invalidTime = 1000 * 60 * 30;//失效时间30分钟
    private final static Map<String, Object> cacheMap = new ConcurrentHashMap<>();
    private final static Map<Long, String> keyMap = new ConcurrentHashMap<>();

    private Map<String, Object> getCacheMap() {
        return cacheMap;
    }


    @Override
    public void setValue(String key, Object value) {
        if (key != null && value != null) {
            getCacheMap().put(key, value);
            putKey(key);
        }
    }

    @Override
    public <T> void setValues(String key, List<T> values) {
        if (key != null && values != null) {
            getCacheMap().put(key, values);
            putKey(key);
        }

    }

    @Override
    public Object getValue(String key) {
        return getCacheMap().get(key);
    }

    @Override
    public <T> List<T> getValues(String key, Class<T> targetClass) {
        Object value = getValue(key);
        return value == null ? null : JSONUtil.toList(JSONUtil.toJsonStr(value), targetClass);
    }

    @Override
    public void invalid(String key) {
        getCacheMap().remove(key);
    }

    @Override
    public Boolean isEmpty(String key) {
        Object value = getCacheMap().get(key);
        return value == null || value.toString().isEmpty();
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.addTriggerTask(this::invalid, triggerContext -> {
            CronTrigger cronTrigger = new CronTrigger(CRON);
            return cronTrigger.nextExecutionTime(triggerContext);
        });
    }

    private void putKey(String key) {
        if (!keyMap.containsValue(key)) {
            keyMap.put(System.currentTimeMillis(), key);
        }
    }

    private void invalid() {
        long currentTimeMillis = System.currentTimeMillis();
        List<Map.Entry<Long, String>> invalidCollect = keyMap.entrySet().stream()
                .filter(entry -> (currentTimeMillis - entry.getKey()) > invalidTime)
                .collect(Collectors.toList());
        for (Map.Entry<Long, String> entry : invalidCollect) {
            invalid(entry.getValue());
            keyMap.remove(entry.getKey());
        }
    }
}
