package com.smart.mvc.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: xizhonghuai
 * @description: AddDeviceDTO
 * @create: 2022-06-20 10:37
 **/
@Data
@Accessors(chain = true)
public class AccountRoleEditDTO {
    private Long accountId;
    private String role;
}
