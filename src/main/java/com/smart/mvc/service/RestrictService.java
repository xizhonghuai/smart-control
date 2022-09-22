package com.smart.mvc.service;

import com.smart.cache.CacheService;
import com.transmission.server.core.IotSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author: xizhonghuai
 * @description: RestrictService
 * @create: 2022-07-26 11:21
 **/
@Service
public class RestrictService {
    @Autowired
    private CacheService cacheService;

    public void ipProcess(IotSession session) {
        Object value = cacheService.getValue("ipProcess");
        boolean b = value != null && (Objects.equals(value, session.getRemoteAddress()));
        if (b) {
            session.close();
        }
    }
}
