package com.smart.domain.message;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author: xizhonghuai
 * @description: Message
 * @create: 2022-06-09 21:49
 **/
@Data
public abstract class Message {
    @JSONField(name="device_id")
    private String deviceId;
    private String code;
}
