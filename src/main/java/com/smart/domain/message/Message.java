package com.smart.domain.message;

import lombok.Data;

/**
 * @author: xizhonghuai
 * @description: Message
 * @create: 2022-06-09 21:49
 **/
@Data
public abstract class Message {
    private String deviceId;
    private String code;
}
