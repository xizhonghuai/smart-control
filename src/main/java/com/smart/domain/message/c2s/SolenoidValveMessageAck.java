package com.smart.domain.message.c2s;

import com.smart.domain.message.MessageAck;
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
public class SolenoidValveMessageAck extends MessageAck {
}
