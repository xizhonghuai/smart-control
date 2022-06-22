package com.smart.mvc.vo;

import java.time.LocalDateTime;

/**
 * @author: xizhonghuai
 * @description: MessageCenterVO
 * @create: 2022-06-20 11:39
 **/
public class MessageCenterVO {
    private Long id;
    private LocalDateTime createdDate;
    private String accountId;
    private String message;
    private Integer unread;
}
