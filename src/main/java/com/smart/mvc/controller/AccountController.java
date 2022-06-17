package com.smart.mvc.controller;


import com.smart.config.ConstantUnit;
import com.smart.config.RestResponse;
import com.smart.mvc.dto.RegAccountDTO;
import com.smart.mvc.service.AccountServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * ACCOUNT 前端控制器
 * </p>
 *
 * @author xizhonghuai
 * @since 2022-06-17
 */
@RestController
@RequestMapping(ConstantUnit.API_PREFIX + "/account")
@Api(tags = "账号操作")
public class AccountController {
    @Autowired
    private AccountServiceImpl accountService;

    @GetMapping("login")
    @ApiOperation("登录")
    public RestResponse<String> login(@RequestParam("username") String username
            , @RequestParam("password") String password) {
        return RestResponse.success(accountService.login(username, password));
    }

    @PostMapping("reg")
    @ApiOperation("注册")
    public RestResponse<Boolean> regAccount(@RequestBody RegAccountDTO regAccountDTO) {
        return RestResponse.success(accountService.regAccount(regAccountDTO));
    }


}
