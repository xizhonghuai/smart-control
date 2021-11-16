package com.cache;

import com.model.DeviceMsg;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName DeviceMsgCache
 * @Description: TODO
 * @Author xizhonghuai
 * @Date 2020/4/24
 * @Version V1.0
 **/
@Component
public class DeviceMsgCache {

    private static final Map<String, DeviceMsg> cache = new ConcurrentHashMap<>();

    public void update(String deviceId, DeviceMsg deviceMsg) {
        cache.put(deviceId, deviceMsg);
    }

    public Map<String, DeviceMsg> getCache() {
        return cache;
    }
}
