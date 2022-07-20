package com.smart.domain.message.c2s;

import com.alibaba.fastjson.annotation.JSONField;
import com.smart.domain.message.Message;
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
public class WarningEventMessage extends Message {
    private Body params;

    @Data
    @Accessors(chain = true)
    public static class Body {
        @JSONField(name = "event_id")
        private Integer eventId;
        private String msg;
    }
}
