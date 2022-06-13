package com.smart.domain.message.s2c;

import com.smart.domain.message.MessageAck;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author: xizhonghuai
 * @description: TimingMessageAck
 * @create: 2022-06-10 10:31
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class TimingMessageAck extends MessageAck implements S2cMessage {
    private String dateTime;

    @Override
    public String toString() {
        return pack();
    }
}
