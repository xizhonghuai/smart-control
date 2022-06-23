package com.smart.mvc.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author: xizhonghuai
 * @description: MessageCenterVO
 * @create: 2022-06-20 11:39
 **/
@Data
public class MessageCenterVO {
    private Long id;
    private LocalDateTime createdDate;
    private String fromAccountId;
    private String fromAccountName;
    private String toAccountId;
    private String toAccountName;
    private String message;
    private Integer readFlag;
}
