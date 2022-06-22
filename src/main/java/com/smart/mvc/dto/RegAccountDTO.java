package com.smart.mvc.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @author: xizhonghuai
 * @description: RegAccountDTO
 * @create: 2022-06-17 16:23
 **/
@Data
@Accessors(chain = true)
public class RegAccountDTO {
    @NotNull
    private String name;
    private String nickname;
    @NotNull
    private String password;
    @NotNull
    private String confirmPassword;
    private String tel;
    private String email;
}
