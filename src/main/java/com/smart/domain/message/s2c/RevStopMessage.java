package com.smart.domain.message.s2c;

import com.smart.domain.message.Message;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: xizhonghuai
 * @description: RevStopMessage
 * @create: 2022-07-04 13:51
 **/
@Data
@Accessors(chain = true)
public class RevStopMessage extends Message implements S2cMessage {
    private Body params;

    @Data
    @Accessors(chain = true)
    public static class Body {
        private Boolean run;
    }
    @Override
    public String toString() {
        return pack();
    }
}
