package com.smart.mvc.controller;

import com.smart.cache.CacheService;
import com.smart.config.ConstantUnit;
import com.smart.config.RestResponse;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author xizhonghuai
 * @date 2022/05/24
 */
@RestController
@RequestMapping(ConstantUnit.API_PREFIX + "debug")
@Api(tags = "debug")
public class DebugController {
    @Autowired
    private CacheService cacheService;

    @GetMapping("cache")
    public RestResponse<Map<String, Object>> getCache() {
        return RestResponse.success(cacheService.getCacheMap());
    }
}
