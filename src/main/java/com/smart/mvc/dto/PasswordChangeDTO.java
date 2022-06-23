package com.smart.mvc.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author: xizhonghuai
 * @description: PasswordChangeDTO
 * @create: 2022-06-22 15:31
 **/
@Data
public class PasswordChangeDTO {
    @NotNull
    private Long id;
    private String password;
    private String newPassword;
}
