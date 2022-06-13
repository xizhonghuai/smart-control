package com.smart.domain.message.s2c;

import com.smart.domain.message.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author: xizhonghuai
 * @description: ParamsQueryMessage
 * @create: 2022-06-10 13:57
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class ParamsQueryMessage extends Message implements S2cMessage {
    private String deviceId;

    @Override
    public String toString() {
        return pack();
    }
}
