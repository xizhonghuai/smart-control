package com.smart.domain.message.c2s;

import com.smart.domain.message.Message;
import com.smart.domain.message.s2c.ParamsConfMessage;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: xizhonghuai
 * @description: ParamsQueryMessageAck
 * @create: 2022-07-04 14:03
 **/
@Data
@Accessors(chain = true)
public class ParamsQueryMessageAck extends Message {
    private ParamsConfMessage.Body params;
}
