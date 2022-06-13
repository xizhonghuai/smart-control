package com.smart.domain.message.c2s;

import com.smart.domain.message.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author: xizhonghuai
 * @description: RegisterMessage
 * @create: 2022-06-09 22:43
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class RegisterMessage extends Message {
    private String deviceId;
    private Body params;
    @Data
    public static class Body {
        String iccid;
        String ver;
    }
}
