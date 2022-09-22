package com.smart.mvc.controller;

import cn.hutool.core.util.StrUtil;
import com.smart.cache.CacheService;
import com.smart.config.ConstantUnit;
import com.smart.config.RestResponse;
import com.smart.config.SpringUtil;
import com.smart.domain.message.c2s.WarningEventMessage;
import com.smart.mvc.service.MessageCenterServiceImpl;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * @author xizhonghuai
 * @date 2022/05/24
 */
@RestController
@RequestMapping(ConstantUnit.API_PREFIX + "debug")
@Api(tags = "debug")
@Slf4j
public class DebugController {
    @Autowired
    private CacheService cacheService;

    @GetMapping("cache")
    public RestResponse<Map<String, Object>> getCache() {
        return RestResponse.success(cacheService.getCacheMap());
    }

    @GetMapping("delete-cache")
    public RestResponse<Boolean> deleteCache() {
        Set<String> keySet = cacheService.getCacheMap().keySet();
        for (String s : keySet) {
            cacheService.invalid(s);
        }
        return RestResponse.success(Boolean.TRUE);
    }

    @GetMapping("setValue")
    public RestResponse<String> setValue(@RequestParam String key
            , @RequestParam String value) {
        cacheService.setValue(key, value);
        return RestResponse.success(value);
    }

    @GetMapping("warning")
    public RestResponse<Object> warning() {
        WarningEventMessage warningEventMessage = new WarningEventMessage();
        warningEventMessage.setParams(new WarningEventMessage.Body().setEventId(1));
        String key = ConstantUnit.WARNING_CACHE_KEY_FUNCTION.apply("867799053264742");
//            String msg = warningEventMessage.getParams().getMsg();
        String msg = warningEventMessage.getWarningMsg();
        cacheService.setValue(key, msg);
        new Thread(() -> {
            MessageCenterServiceImpl messageCenterService = SpringUtil.getObject(MessageCenterServiceImpl.class);
            messageCenterService.sendDeviceWarningMessage("867799053264742", msg);
        }).start();
        return null;
    }


    @GetMapping()
    public Date index(String str) {
        if (StrUtil.isNotBlank(str)) {
            OAuth2ContextHolder.setApplicationCode(str);
        }
        log.error(OAuth2ContextHolder.getApplicationCode());
        return new Date();
    }

}
