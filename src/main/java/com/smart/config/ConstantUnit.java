package com.smart.config;

import java.util.function.Function;

/**
 * @ClassName SystemConstantUnit
 * @Description: TODO
 * @Author xizhonghuai
 * @Date 2020/4/24
 * @Version V1.0
 **/
public class ConstantUnit {

    public static final String PERMISSION = "no permission";
    public static final String API_PREFIX = "api/v1/";
    public static final Long adminId = 1L;
    public static final Long adminRoleId = 1L;
    public static final Long agentRoleId = 2L;
    public static final Long userRoleId = 3L;

    public static final String on = "on";
    public static final String off = "off";

    public static Function<String, String> WARNING_CACHE_KEY_FUNCTION = deviceId -> String.format("%s,WARNING_CACHE", deviceId);
    public static Function<String, String> WORK_STATUS_CACHE_KEY_FUNCTION = deviceId -> String.format("%s,WORK_STATUS_CACHE", deviceId);
    public static Function<String, String> RUNNING_MODEL_DESCRIPTION_CACHE_KEY_FUNCTION = deviceId -> String.format("%s,RUNNING_MODEL_DESCRIPTION_CACHE", deviceId);
    public static Function<String,String> IMMEDIATE_PARAMS_CACHE_KEY_FUNCTION = deviceId -> String.format("%s,IMMEDIATE_PARAMS_CACHE", deviceId);
}
