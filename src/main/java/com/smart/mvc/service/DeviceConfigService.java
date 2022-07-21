package com.smart.mvc.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.smart.cache.CacheService;
import com.smart.config.ConstantUnit;
import com.smart.domain.message.MessageUtil;
import com.smart.domain.message.c2s.ParamsQueryMessageAck;
import com.smart.domain.message.s2c.ParamsConfMessage;
import com.smart.mvc.dto.ConfPlanAddDTO;
import com.smart.mvc.dto.ConfPlanNameEditDTO;
import com.smart.mvc.vo.ConfPlanVO;
import com.smart.mvc.vo.DeviceWorkStatusVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author: xizhonghuai
 * @description: DeviceConfigService
 * @create: 2022-07-13 17:09
 **/
@Service
public class DeviceConfigService {
    @Autowired
    private CacheService cacheService;
    @Autowired
    private DeviceControlService deviceControlService;

    public List<ConfPlanVO> planList(String deviceId) {
        List<ConfPlanVO> confPlanVOS = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            ConfPlanVO value = (ConfPlanVO) cacheService.getValue(String.format("%s,%s,%s,plan", deviceId, MessageUtil.getMessageCode(ParamsQueryMessageAck.class), i));
            if (value != null) {
                confPlanVOS.add(value);
                if (value.getStatus()) {
                    cacheService.setValue(ConstantUnit.RUNNING_MODEL_DESCRIPTION_CACHE_KEY_FUNCTION.apply(deviceId), String.format("%s(%s-%s)", value.getName(), value.getBody().getStartDate(), value.getBody().getEndDate()));
                }
            }
        }
        return confPlanVOS;
    }

    public Boolean editPlanName(ConfPlanNameEditDTO dto) {
        String key = String.format("%s,%s,%s,plan", dto.getDeviceId(), MessageUtil.getMessageCode(ParamsQueryMessageAck.class), dto.getPlanId());
        Object value = cacheService.getValue(key);
        if (value != null) {
            ConfPlanVO confPlanVO = (ConfPlanVO) value;
            confPlanVO.setName(dto.getName());
            cacheService.setValue(key, confPlanVO);
        }
        return true;
    }

    public Boolean addPlan(ConfPlanAddDTO dto) {
        List<ConfPlanVO> list = planList(dto.getDeviceId());
//        Collections.sort(list, Comparator.comparing(ConfPlanVO::getId));
        if (list.size() >= 6) {
            throw new RuntimeException("最多支持6组计划");
        }
        int newId = list.size() + 1;
        ParamsConfMessage confMessage = MessageUtil.defaultConfig(newId, dto.getDeviceId());
        deviceControlService.deviceParamsConf(1, confMessage);
        deviceControlService.deviceParams(0, dto.getDeviceId(), newId);
        return true;
    }

    public Date immediateEndTime(String deviceId) {
        String paramsKey = ConstantUnit.IMMEDIATE_PARAMS_CACHE_KEY_FUNCTION.apply(deviceId);
        Object value = cacheService.getValue(paramsKey);
        if (value == null) {
            return null;
        }
        DateTime endTime = (DateTime) value;
        if (System.currentTimeMillis() - endTime.getTime() > 0) {
            cacheService.invalid(paramsKey);
            return null;
        }
        return endTime;
    }

    public DeviceWorkStatusVO workStatus(String deviceId) {
        Date endDate = immediateEndTime(deviceId);
        if (endDate != null) {
            Date now = new Date();
            long dif = DateUtil.between(now, endDate, DateUnit.SECOND, true);
            return new DeviceWorkStatusVO().setSecondsRemaining(dif);
        }
        List<ConfPlanVO> planVOS = planList(deviceId);
        Optional<ConfPlanVO> any = planVOS.stream().filter(ConfPlanVO::getStatus).findAny();
        return any.map(confPlanVO -> new DeviceWorkStatusVO().setPlanName(confPlanVO.getName())).orElse(null);
    }
}
