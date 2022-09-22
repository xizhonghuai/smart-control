package com.smart.domain.message.s2c;

import com.smart.domain.message.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: xizhonghuai
 * @description: ParamsQueryMessage
 * @create: 2022-06-10 13:57
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class ParamsDeleteMessage extends Message implements S2cMessage {

    private Body params;

    @Data
    @Accessors(chain = true)
    public static class Body {
        private Integer id;//模式编号1-6
    }

    @Override
    public String toString() {
        return pack();
    }
}
