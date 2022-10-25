package com.smart.mvc.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.smart.cache.CacheService;
import com.smart.communication.DeviceAPI;
import com.smart.config.ConstantUnit;
import com.smart.domain.message.Message;
import com.smart.domain.message.MessageUtil;
import com.smart.domain.message.c2s.*;
import com.smart.domain.message.s2c.*;
import com.smart.mvc.dto.KeyEnDTO;
import com.smart.mvc.dto.RevStopDTO;
import com.smart.mvc.dto.SolenoidValveSwitchDTO;
import com.smart.mvc.vo.ConfPlanVO;
import com.smart.utils.Utils;
import com.smart.utils.VerificationUtils;
import com.transmission.server.core.ConnectProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author xizhonghuai
 * @date 2022/05/24
 */
@Service
public class DeviceControlService {
    private final DeviceAPI deviceAPI = new DeviceAPI();
    @Autowired
    private CacheService cacheService;
    @Autowired
    private CommandPoolService commandPoolService;

    public List<ConnectProperty> onlineDevice() {
        return deviceAPI.getOnlineDeviceList();
    }

    public Message deviceMessage(String deviceId, String code, Object... params) {
        String paramsCollect = Arrays.stream(params).filter(Objects::nonNull).map(Object::toString).collect(Collectors.joining(","));
        if (StrUtil.isBlank(paramsCollect)) {
            String key = StrUtil.isBlank(code) ? deviceId : String.format("%s,%s", deviceId, code);
            return (Message) cacheService.getValue(key);
        }
        String key = StrUtil.isBlank(code) ? deviceId : String.format("%s,%s,%s", deviceId, code, paramsCollect);
        return (Message) cacheService.getValue(key);

    }

    public String deviceNetStatus(String deviceId) {
        if (deviceId == null) {
            throw new RuntimeException("device is not null");
        }
        List<ConnectProperty> connectProperties = onlineDevice();
        boolean b = connectProperties.stream().map(ConnectProperty::getRegId).anyMatch(v -> Objects.equals(deviceId, v));
        return b ? "online" : "off-line";
    }

    public Boolean isDeviceOnline(String deviceId) {
        return Objects.equals(deviceNetStatus(deviceId), "online");
    }

    private void checkDevice(String deviceId) {
        if (Objects.equals(deviceNetStatus(deviceId), "off-line")) {
            throw new RuntimeException("device is off-line");
        }
    }

    public Message deviceParamsConf(int isSync, ParamsConfMessage paramsConfMessage) {
        Integer id = paramsConfMessage.getParams().getId();
        if (id == null) {
            throw new RuntimeException("参数错误,请重试");
        }
        String deviceId = paramsConfMessage.getDeviceId();
        checkDevice(deviceId);
        paramsConfMessage.setCode(MessageUtil.getMessageCode(ParamsConfMessage.class));
        VerificationUtils.planDateCheck(deviceId
                , DateUtil.parse(paramsConfMessage.getParams().getStartDate(), "yyyy-MM-dd")
                , DateUtil.parse(paramsConfMessage.getParams().getEndDate(), "yyyy-MM-dd")
                , id);
        VerificationUtils.planTimeCheck(paramsConfMessage.getParams().getTimeList());
        paramsConfMessage.process();
        deviceAPI.sendCmd(deviceId, paramsConfMessage);
        commandPoolService.add(deviceId, paramsConfMessage);
        if (isSync <= 0) {
            return null;
        }
        cacheService.invalid(String.format("%s,%s", deviceId, MessageUtil.getMessageCode(ParamsConfMessageAck.class)));
        Message message = new Utils.SyncResult<Message>().get(() -> deviceMessage(deviceId, MessageUtil.getMessageCode(ParamsConfMessageAck.class)));

        // 同步缓存数据
        ParamsConfMessage.Body data = paramsConfMessage.getParams();
        MessageUtil.c2sMsgProcess(data);
        String planKey = String.format("%s,%s,%s,plan", paramsConfMessage.getDeviceId(), MessageUtil.getMessageCode(ParamsQueryMessageAck.class), data.getId());
        Object value = cacheService.getValue(planKey);
        if (value == null) {
            ConfPlanVO confPlanVO = new ConfPlanVO();
            confPlanVO.setId(data.getId());
            confPlanVO.setName("计划" + data.getId());
            confPlanVO.setBody(data);
            confPlanVO.process();
            cacheService.setValue(planKey, confPlanVO);
        } else {
            ConfPlanVO confPlanVO = (ConfPlanVO) value;
            confPlanVO.setBody(data);
            confPlanVO.process();
            cacheService.setValue(planKey, confPlanVO);
        }
        //        deviceParams(0, deviceId, paramsConfMessage.getParams().getId());
        return message;
    }

    public Message immediateDeviceParamsConf(int isSync, ImmediateParamsConfMessage immediateParamsConfMessage) {
        String deviceId = immediateParamsConfMessage.getDeviceId();
        checkDevice(deviceId);
        immediateParamsConfMessage.setCode(MessageUtil.getMessageCode(ImmediateParamsConfMessage.class));
        deviceAPI.sendCmd(deviceId, immediateParamsConfMessage);
        commandPoolService.add(deviceId, immediateParamsConfMessage);
        if (isSync <= 0) {
            return null;
        }
        cacheService.invalid(String.format("%s,%s", deviceId, MessageUtil.getMessageCode(ImmediateParamsConfMessageAck.class)));
        Message message = new Utils.SyncResult<Message>().get(() -> deviceMessage(deviceId, MessageUtil.getMessageCode(ImmediateParamsConfMessageAck.class)));
        String paramsKey = ConstantUnit.IMMEDIATE_PARAMS_CACHE_KEY_FUNCTION.apply(deviceId);
        ImmediateParamsConfMessage.Body params = immediateParamsConfMessage.getParams();
        Integer onTime = params.getOnTime();
        Date now = new Date();
        DateTime endTime = DateUtil.offsetSecond(now, onTime);
        cacheService.setValue(paramsKey, endTime);
        return message;
    }


    public Message deviceParams(int isSync, String deviceId, Integer id) {
        checkDevice(deviceId);
        ParamsQueryMessage paramsQueryMessage = new ParamsQueryMessage();
        paramsQueryMessage.setDeviceId(deviceId);
        paramsQueryMessage.setCode(MessageUtil.getMessageCode(ParamsQueryMessage.class));
        paramsQueryMessage.setParams(new ParamsQueryMessage.Body().setId(id));
        deviceAPI.sendCmd(deviceId, paramsQueryMessage);
        commandPoolService.add(deviceId, paramsQueryMessage);
        if (isSync == 0) {
            return null;
        }
        cacheService.invalid(String.format("%s,%s,%s", deviceId, MessageUtil.getMessageCode(ParamsConfMessageAck.class), id));
        return new Utils.SyncResult<Message>().get(() -> deviceMessage(deviceId, MessageUtil.getMessageCode(ParamsQueryMessageAck.class), id.toString()));
    }

    public Message power(int isSync, RevStopDTO dto) {
        String deviceId = dto.getDeviceId();
        checkDevice(deviceId);
        RevStopMessage revStopMessage = new RevStopMessage();
        revStopMessage.setCode(MessageUtil.getMessageCode(RevStopMessage.class));
        revStopMessage.setParams(new RevStopMessage.Body().setRun(dto.getPower()));
        deviceAPI.sendCmd(deviceId, revStopMessage);
        commandPoolService.add(deviceId, revStopMessage);
        if (isSync == 0) {
            return null;
        }
        cacheService.invalid(String.format("%s,%s", deviceId, MessageUtil.getMessageCode(RevStopMessageAck.class)));
        Message message = new Utils.SyncResult<Message>().get(() -> deviceMessage(deviceId, MessageUtil.getMessageCode(RevStopMessageAck.class)));

        //change running state
        TimingMessage timingMessage = deviceData(deviceId);
        if (timingMessage != null) {
            timingMessage.getParams().setState(dto.getPower() ? "running" : "stop");
            cacheService.setValue(deviceId, MessageUtil.getMessageCode(TimingMessage.class));
        }

        return message;
    }

    public Message keyEn(int isSync, KeyEnDTO dto) {
        String deviceId = dto.getDeviceId();
        checkDevice(deviceId);
        KeyEnablingMessage keyEnablingMessage = new KeyEnablingMessage();
        keyEnablingMessage.setCode(MessageUtil.getMessageCode(KeyEnablingMessage.class));
        keyEnablingMessage.setParams(new KeyEnablingMessage.Body().setKeyEn(dto.getEnable()));
        deviceAPI.sendCmd(deviceId, keyEnablingMessage);
        commandPoolService.add(deviceId, keyEnablingMessage);
        if (isSync == 0) {
            return null;
        }
        cacheService.invalid(String.format("%s,%s", deviceId, MessageUtil.getMessageCode(KeyEnablingMessageAck.class)));
        return new Utils.SyncResult<Message>().get(() -> deviceMessage(deviceId, MessageUtil.getMessageCode(KeyEnablingMessageAck.class)));
    }

    public Message solenoidValveSwitch(int isSync, SolenoidValveSwitchDTO dto) {
        String deviceId = dto.getDeviceId();
        checkDevice(deviceId);
        SolenoidValveMessage solenoidValveMessage = new SolenoidValveMessage();
        solenoidValveMessage.setCode(MessageUtil.getMessageCode(KeyEnablingMessage.class));
        solenoidValveMessage.setParams(new SolenoidValveMessage.Body().setCh(dto.getCh()).setSta(dto.getEnable() ? ConstantUnit.on : ConstantUnit.off));
        deviceAPI.sendCmd(deviceId, solenoidValveMessage);
        commandPoolService.add(deviceId, solenoidValveMessage);
        if (isSync == 0) {
            return null;
        }
        cacheService.invalid(deviceId);
        return new Utils.SyncResult<Message>().get(() -> deviceMessage(deviceId, MessageUtil.getMessageCode(SolenoidValveMessageAck.class)));
    }

    public List<CommandPoolService.Command> commandPoolList() {
        return commandPoolService.getCommands();
    }

    public Message deviceParamsConfV2(ParamsConfMessage paramsConfMessage) {
        String deviceId = paramsConfMessage.getDeviceId();
        checkDevice(deviceId);
        paramsConfMessage.setCode(MessageUtil.getMessageCode(ParamsConfMessage.class));
        //todo check params
        return deviceParamsConf(1, paramsConfMessage);
    }

    public ParamsQueryMessageAck deviceParamsV2(String deviceId, Integer id) {
        checkDevice(deviceId);
        deviceParams(0, deviceId, id);
        Message message = deviceMessage(deviceId, MessageUtil.getMessageCode(ParamsQueryMessageAck.class), id.toString());
        return (ParamsQueryMessageAck) message;
    }

    public Message deleteParamsSync(ParamsDeleteMessage paramsDeleteMessage) {
        String deviceId = paramsDeleteMessage.getDeviceId();
        checkDevice(deviceId);
        paramsDeleteMessage.setCode(MessageUtil.getMessageCode(ParamsDeleteMessage.class));
        deviceAPI.sendCmd(deviceId, paramsDeleteMessage);
        commandPoolService.add(deviceId, paramsDeleteMessage);

        cacheService.invalid(String.format("%s,%s", deviceId, MessageUtil.getMessageCode(ParamsDeleteMessageAck.class)));
        Message message = new Utils.SyncResult<Message>().get(() -> deviceMessage(deviceId, MessageUtil.getMessageCode(ParamsDeleteMessageAck.class)));
        // 同步缓存数据
        String planKey = String.format("%s,%s,%s,plan", deviceId, MessageUtil.getMessageCode(ParamsQueryMessageAck.class), paramsDeleteMessage.getParams().getId());
        cacheService.invalid(planKey);
        return message;
    }

    public TimingMessage deviceData(String deviceId) {
        checkDevice(deviceId);
        Message message = deviceMessage(deviceId, MessageUtil.getMessageCode(TimingMessage.class));
        TimingMessage timingMessage = (TimingMessage) message;
//        cacheService.invalid(String.format("%s,%s", deviceId, MessageUtil.getMessageCode(TimingMessage.class)));
        String key = ConstantUnit.WARNING_CACHE_KEY_FUNCTION.apply(deviceId);
        Object value = cacheService.getValue(key);
        if (value != null && timingMessage != null) {
            if (value.toString().equals("低水位告警，请检查水位！")) {
                timingMessage.getParams().setWaterLevel(0);
            }
            if (value.toString().equals("水位已恢复正常")) {
                timingMessage.getParams().setWaterLevel(1);
            }
        }

        return timingMessage;
    }


    public WarningEventMessageAck deviceWarningEvent(String deviceId) {
        checkDevice(deviceId);
        Message message = deviceMessage(deviceId, MessageUtil.getMessageCode(WarningEventMessageAck.class));
        return (WarningEventMessageAck) message;
    }


}
