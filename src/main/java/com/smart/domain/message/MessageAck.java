package com.smart.domain.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author: xizhonghuai
 * @description: Message
 * @create: 2022-06-09 21:49
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class MessageAck extends Message {
    private Integer result;
}
