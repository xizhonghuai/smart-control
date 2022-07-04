package com.smart.domain.message.s2c;

import com.smart.domain.message.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: xizhonghuai
 * @description: SolenoidValveMessage
 * @create: 2022-07-04 14:33
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class SolenoidValveMessage extends Message implements S2cMessage {
    private Body params;

    @Data
    @Accessors(chain = true)
    public static class Body {
        private Integer ch;
        private String sta;
    }

    @Override
    public String toString() {
        return pack();
    }
}
