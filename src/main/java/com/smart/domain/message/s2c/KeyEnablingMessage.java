package com.smart.domain.message.s2c;

import com.alibaba.fastjson.annotation.JSONField;
import com.smart.domain.message.Message;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: xizhonghuai
 * @description: KeyEnablingMessage
 * @create: 2022-07-04 15:01
 **/
@Accessors(chain = true)
@Data
public class KeyEnablingMessage extends Message implements S2cMessage {
    private Body params;

    @Data
    @Accessors(chain = true)
    public static class Body {
        @JSONField(name = "key_en")
        private Boolean keyEn;
    }

    @Override
    public String toString() {
        return pack();
    }

}
