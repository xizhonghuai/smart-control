package com.smart.domain.message.s2c;

import com.alibaba.fastjson.annotation.JSONField;
import com.smart.domain.message.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author: xizhonghuai
 * @description: ImmediateParamsConfMessage
 * @create: 2022-07-20 16:51
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class ImmediateParamsConfMessage extends Message implements S2cMessage {
    private Body params;

    @Data
    @Accessors(chain = true)
    public static class Body {
        @JSONField(name = "on_time")
        private Integer onTime;
        @JSONField(name = "on_delay")
        private Integer onDelay;
        @JSONField(name = "off_delay")
        private Integer offDelay;
        private List<String> relays;
    }
}
