package com.smart.domain.message.s2c;

import com.smart.domain.message.MessageAck;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: xizhonghuai
 * @description: EventMessage
 * @create: 2022-07-04 14:10
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class WarningEventMessageAck extends MessageAck implements S2cMessage {
    @Override
    public String toString() {
        return pack();
    }
}
