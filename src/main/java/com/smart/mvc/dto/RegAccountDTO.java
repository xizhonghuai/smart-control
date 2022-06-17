package com.smart.mvc.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: xizhonghuai
 * @description: RegAccountDTO
 * @create: 2022-06-17 16:23
 **/
@Data
@Accessors(chain = true)
public class RegAccountDTO {
    private String name;
    private String nickname;
    private String password;
    private String confirmPassword;
    private String tel;
    private String email;
}
