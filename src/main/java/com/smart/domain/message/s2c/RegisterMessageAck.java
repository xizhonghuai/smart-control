package com.smart.domain.message.s2c;

import com.smart.domain.message.MessageAck;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: xizhonghuai
 * @description: RegisterMessageAck
 * @create: 2022-06-10 10:28
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class RegisterMessageAck extends MessageAck implements S2cMessage {
    @Override
    public String toString() {
        return pack();
    }
}
