package com.smart.hander.businesshandler;


import com.smart.cache.CacheService;
import com.smart.config.SpringUtil;
import com.smart.domain.Message;
import com.transmission.business.BusinessHandler;
import com.transmission.server.core.IotSession;
import com.smart.utils.DeviceMessageUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @ClassName DefaultBusinessHandler
 * @Description: TODO
 * @Author xizhonghuai
 * @Date 2020/4/18
 * @Version V1.0
 **/
@Slf4j
public class DefaultBusinessHandler implements BusinessHandler {

    @Override
    public Boolean init() {
        return true;
    }

    @Override
    public void destroy() {

    }


    @Override
    public void messageReceived(IotSession iotSession, Object message) {
        log.info("rec : {}", message.toString());
        Message messageObj = DeviceMessageUtil.toMessage(message.toString());
        String payLoad = messageObj.getPayLoad();
        CacheService cacheService = SpringUtil.getObject(CacheService.class);
        cacheService.setValue(iotSession.getDeviceId(), payLoad + "\n" + new Date());
        char functionCode = messageObj.getFunctionCode();
        //注册
        if (functionCode == 0x74) {
            iotSession.setDeviceId(messageObj.getPayLoad());
            messageObj.setPayLoad("ok");
            iotSession.sendMsg(messageObj);
            return;
        }
        if (functionCode == 0x72) {
            if (messageObj.getSensor() == 0x01) {//继电器
            }
            if (messageObj.getSensor() == 0x02) {//传感器

            }
            iotSession.sendMsg(messageObj);
            return;
        }

        if (functionCode == 0x71) {
            if (messageObj.getSensor() == 0x01) {//继电器

            }
            if (messageObj.getSensor() == 0x02) {//传感器

            }
        }


    }

    @Override
    public void sessionOpened(IotSession iotSession) {

    }

    @Override
    public void sessionClosed(IotSession iotSession) {
        iotSession.close();

    }

    @Override
    public void sessionIdle(IotSession iotSession) {
        iotSession.close();
    }


}
