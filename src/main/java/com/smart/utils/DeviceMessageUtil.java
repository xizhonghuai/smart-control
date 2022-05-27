package com.smart.utils;

import com.smart.domain.Message;
import org.springframework.stereotype.Service;

/**
 * @author xizhonghuai
 * @date 2022/05/23
 */
@Service
public class DeviceMessageUtil {
    public static Message toMessage(String msg) {
        if (msg == null || msg.length() < 8) {
            throw new RuntimeException("无效的报文");
        }
        Message message = new Message(msg.charAt(1), msg.charAt(2), msg.charAt(3), msg.charAt(4));
        message.setPayLoad(msg.substring(6, msg.length() - 1));
        return message;
    }


}
