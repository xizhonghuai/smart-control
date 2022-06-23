package com.smart.mvc.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author: xizhonghuai
 * @description: MessageCenterDTO
 * @create: 2022-06-23 11:03
 **/
@Data
public class MessageCenterDTO {
    private String fromAccountName;
    private String toAccountName;
    private String message;
    private Integer readFlag;
}
