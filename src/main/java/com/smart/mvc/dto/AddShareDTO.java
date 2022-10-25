package com.smart.mvc.dto;

import lombok.Data;

/**
 * @author: xizhonghuai
 * @description: AddShareDTO
 * @create: 2022-06-20 14:51
 **/
@Data
public class AddShareDTO {
    private String accountName;
    private Long deviceId;
    private Integer shareType;//0-查看与设置 1-仅查看
}
