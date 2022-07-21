package com.smart.domain;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.smart.cache.CacheService;
import com.smart.config.ConstantUnit;
import com.smart.config.SpringUtil;
import com.smart.domain.message.Message;
import com.smart.domain.message.MessageUtil;
import com.smart.domain.message.c2s.*;
import com.smart.domain.message.s2c.ParamsConfMessage;
import com.smart.domain.message.s2c.RegisterMessageAck;
import com.smart.domain.message.s2c.TimingMessageAck;
import com.smart.domain.message.s2c.WarningEventMessageAck;
import com.smart.mvc.service.CommandPoolService;
import com.smart.mvc.service.DeviceServiceImpl;
import com.smart.mvc.service.MessageCenterServiceImpl;
import com.smart.mvc.vo.ConfPlanVO;
import com.smart.utils.Utils;
import com.transmission.server.core.IotSession;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: xizhonghuai
 * @description: MessageProcess
 * @create: 2022-06-11 09:41
 **/
@Slf4j
public class MessageProcess {
    private final CacheService cacheService = CacheService.getCacheService();
    private final MessageCenterServiceImpl messageCenterService = SpringUtil.getObject(MessageCenterServiceImpl.class);
    private final CommandPoolService commandPoolService = CommandPoolService.getCommandPoolService();
    private final DeviceServiceImpl deviceService = DeviceServiceImpl.get();

    public void accept(IotSession iotSession, Object message) {
        Message messageObject = MessageUtil.parse(message);
        if (!StrUtil.isBlank(iotSession.getDeviceId())) {
            String code = messageObject.getCode();
            cacheService.setValue(String.format("%s,%s", iotSession.getDeviceId(), code), messageObject);
            cacheService.setValue(iotSession.getDeviceId(), messageObject);
            commandPoolService.remove(iotSession.getDeviceId(), code);
        }
        if (messageObject instanceof RegisterMessage) {
            RegisterMessage registerMessage = (RegisterMessage) messageObject;
            String deviceId = registerMessage.getDeviceId();
            iotSession.setDeviceId(deviceId);
            deviceService.addDevice(deviceId);
            //ack
            RegisterMessageAck registerMessageAck = new RegisterMessageAck();
            registerMessageAck.setDeviceId(deviceId);
            registerMessageAck.setResult(0);
            registerMessageAck.setCode(MessageUtil.getMessageCode(RegisterMessageAck.class));
            iotSession.sendMsg(registerMessageAck);
            log.info(deviceId + " 注册");
            return;
        }
        if (messageObject instanceof TimingMessage) {
            TimingMessage timingMessage = (TimingMessage) messageObject;
            TimingMessage.Body params = timingMessage.getParams();
            // TODO: 2022/7/4
            cacheService.setValue(ConstantUnit.WORK_STATUS_CACHE_KEY_FUNCTION.apply(iotSession.getDeviceId()), params.getState());
            //ack
            TimingMessageAck timingMessageAck = new TimingMessageAck();
            timingMessageAck.setCode(MessageUtil.getMessageCode(TimingMessageAck.class));
            timingMessageAck.setDeviceId(iotSession.getDeviceId());
            timingMessageAck.setDateTime(Utils.getDateTime());
            timingMessageAck.setResult(0);
            iotSession.sendMsg(timingMessageAck);
            return;
        }
        if (messageObject instanceof ParamsConfMessageAck) {
            ParamsConfMessageAck paramsConfMessageAck = (ParamsConfMessageAck) messageObject;
            log.info(JSON.toJSONString(paramsConfMessageAck));
            return;
        }
        if (messageObject instanceof ParamsQueryMessageAck) {
            ParamsQueryMessageAck paramsQueryMessageAck = (ParamsQueryMessageAck) messageObject;
            paramsQueryMessageAck.process();
            cacheService.setValue(String.format("%s,%s,%s", iotSession.getDeviceId(), paramsQueryMessageAck.getCode(), paramsQueryMessageAck.getData().getId()), paramsQueryMessageAck);
            //config save
            ParamsConfMessage.Body data = paramsQueryMessageAck.getData();
            String planKey = String.format("%s,%s,%s,plan", iotSession.getDeviceId(), paramsQueryMessageAck.getCode(), data.getId());
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
            log.info(JSON.toJSONString(paramsQueryMessageAck));
            return;
        }
        if (messageObject instanceof RevStopMessageAck) {
            RevStopMessageAck revStopMessageAck = (RevStopMessageAck) messageObject;
            log.info(JSON.toJSONString(revStopMessageAck));
            return;
        }
        if (messageObject instanceof KeyEnablingMessageAck) {
            KeyEnablingMessageAck keyEnablingMessageAck = (KeyEnablingMessageAck) messageObject;
            log.info(JSON.toJSONString(keyEnablingMessageAck));
            return;
        }
        if (messageObject instanceof WarningEventMessage) {
            WarningEventMessage warningEventMessage = (WarningEventMessage) messageObject;
            String key = ConstantUnit.WARNING_CACHE_KEY_FUNCTION.apply(iotSession.getDeviceId());
            String msg = warningEventMessage.getParams().getMsg();
            cacheService.setValue(key, msg);
            new Thread(() -> messageCenterService.sendDeviceWarningMessage(iotSession.getDeviceId(), msg)).start();
            WarningEventMessageAck warningEventMessageAck = new WarningEventMessageAck();
            warningEventMessageAck.setDeviceId(iotSession.getDeviceId());
            warningEventMessageAck.setCode(MessageUtil.getMessageCode(WarningEventMessageAck.class));
            warningEventMessageAck.setResult(0);
            iotSession.sendMsg(warningEventMessageAck);
            log.info(JSON.toJSONString(warningEventMessage));
        }
    }
}
