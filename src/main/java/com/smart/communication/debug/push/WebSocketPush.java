package com.smart.communication.debug.push;

import com.alibaba.fastjson.JSON;
import com.smart.communication.debug.WSService;
import com.msgpush.MessagePush;


/**
 * @ClassName WebSocketPush
 * @Description: TODO
 * @Author xizhonghuai
 * @Date 2020/4/16
 * @Version V1.0
 **/
public class WebSocketPush extends MessagePush {
    @Override
    public void init() {

    }

    @Override
    public void push(Object message) {
        WSService.sendMessage(JSON.toJSONString(message));
    }

    @Override
    public void close() {

    }
}
