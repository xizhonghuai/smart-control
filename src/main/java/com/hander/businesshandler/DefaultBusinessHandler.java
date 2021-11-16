package com.hander.businesshandler;


import com.transmission.business.BusinessHandler;
import com.transmission.server.core.IotSession;
import lombok.extern.slf4j.Slf4j;

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
