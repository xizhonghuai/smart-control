package com.smart.mvc.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.smart.cache.CacheService;
import com.smart.config.ConstantUnit;
import com.smart.domain.message.MessageUtil;
import com.smart.domain.message.c2s.ParamsQueryMessageAck;
import com.smart.domain.message.s2c.ParamsConfMessage;
import com.smart.domain.message.s2c.ParamsDeleteMessage;
import com.smart.mvc.dto.ConfPlanAddDTO;
import com.smart.mvc.dto.ConfPlanNameEditDTO;
import com.smart.mvc.vo.ConfPlanVO;
import com.smart.mvc.vo.DeviceWorkStatusVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
        for (int i = 1; i <= 6; i++) {
            ConfPlanVO value = (ConfPlanVO) cacheService.getValue(String.format("%s,%s,%s,plan", deviceId, MessageUtil.getMessageCode(ParamsQueryMessageAck.class), i));
            if (value != null) {
                value.process();
                confPlanVOS.add(value);
                Object planName = cacheService.getValue(String.format("%s,%s,planName", deviceId, i));
                if (planName != null) {
                    value.setName(planName.toString());
                }
                if (value.getStatus()) {
                    cacheService.setValue(ConstantUnit.RUNNING_MODEL_DESCRIPTION_CACHE_KEY_FUNCTION.apply(deviceId)
                            , value.getName());
                    String workStatusKey = ConstantUnit.WORK_STATUS_CACHE_KEY_FUNCTION.apply(deviceId);
                    Object workStatus = cacheService.getValue(workStatusKey);
                    if (workStatus != null && "stop".equals(workStatus.toString())) {
                        value.setStatus(false);
                    }
                }
            }
        }
        return confPlanVOS;
    }

    public Date createNewPlanDate(String deviceId) {
        List<ConfPlanVO> planVOS = planList(deviceId);
        if (CollectionUtil.isEmpty(planVOS)) {
            return new Date();
        }
        Optional<DateTime> optionalDateTime = planVOS.stream().map(v -> DateUtil.parse(v.getBody().getEndDate(), "yyyy-MM-dd")).max(Date::compareTo);
        if (optionalDateTime.isPresent()) {
            return DateUtil.offsetDay(optionalDateTime.get(), 1);
        }
        return new Date();
    }

    public Boolean editPlanName(ConfPlanNameEditDTO dto) {
        String key = String.format("%s,%s,%s,plan", dto.getDeviceId(), MessageUtil.getMessageCode(ParamsQueryMessageAck.class), dto.getPlanId());
        Object value = cacheService.getValue(key);
        if (value != null) {
            ConfPlanVO confPlanVO = (ConfPlanVO) value;
            confPlanVO.setName(dto.getName());
            cacheService.setValue(key, confPlanVO);
            cacheService.setValueForever(String.format("%s,%s,planName", dto.getDeviceId(), dto.getPlanId()), dto.getName());
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
        ParamsConfMessage confMessage = MessageUtil.defaultConfig(newId, dto.getDeviceId(), createNewPlanDate(dto.getDeviceId()));
        deviceControlService.deviceParamsConf(1, confMessage);

        // 同步缓存数据
        ParamsConfMessage.Body data = confMessage.getParams();
        String planKey = String.format("%s,%s,%s,plan", dto.getDeviceId(), MessageUtil.getMessageCode(ParamsQueryMessageAck.class), newId);
        Object value = cacheService.getValue(planKey);
        if (value == null) {
            ConfPlanVO confPlanVO = new ConfPlanVO();
            confPlanVO.setId(newId);
            confPlanVO.setName("计划" + newId);
            confPlanVO.setBody(data);
            confPlanVO.process();
            cacheService.setValue(planKey, confPlanVO);
        } else {
            ConfPlanVO confPlanVO = (ConfPlanVO) value;
            confPlanVO.setBody(data);
            confPlanVO.process();
            cacheService.setValue(planKey, confPlanVO);
        }
//        deviceControlService.deviceParams(0, dto.getDeviceId(), newId);
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
        Object model = cacheService.getValue(ConstantUnit.SZ_CACHE_KEY_FUNCTION.apply(deviceId));
        Object status = cacheService.getValue(ConstantUnit.WORK_STATUS_CACHE_KEY_FUNCTION.apply(deviceId));
        if (status == null) {
            return new DeviceWorkStatusVO().setEx(true);
        }
        if (Objects.equals(status.toString(), "stop")) {
            return new DeviceWorkStatusVO().setEx(false).setStatus("stop");
        }
        if (model == null) {
            return new DeviceWorkStatusVO().setEx(true);
        }
        if (Objects.equals(model.toString(), "0")) {
            List<ConfPlanVO> planVOS = planList(deviceId);
            Optional<ConfPlanVO> any = planVOS.stream().filter(ConfPlanVO::getStatus).findAny();
            return any.map(confPlanVO -> new DeviceWorkStatusVO()
                            .setModel("0")
                            .setPlanName(confPlanVO.getName()))
                    .orElse(new DeviceWorkStatusVO().setEx(true));
        }
        Date endDate = immediateEndTime(deviceId);
        if (endDate != null) {
            Date now = new Date();
            long dif = DateUtil.between(now, endDate, DateUnit.SECOND, true);
            return new DeviceWorkStatusVO().setSecondsRemaining(dif);
        }
        return new DeviceWorkStatusVO().setEx(true);
    }

    public Boolean deletePlan(ParamsDeleteMessage paramsDeleteMessage) {
        deviceControlService.deleteParamsSync(paramsDeleteMessage);
        return true;
    }
}
