package com.smart.mvc.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author: xizhonghuai
 * @description: MessageCenterVO
 * @create: 2022-06-20 11:39
 **/
@Data
public class MessageCenterVO {
    private Long id;
    private Date createdDate;
    private Long fromAccountId;
    private String fromAccountName;
    private Long toAccountId;
    private String toAccountName;
    private String message;
    private Integer readFlag;
}
