package com.smart.mvc.controller;


import com.smart.config.ConstantUnit;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * ROLE 前端控制器
 * </p>
 *
 * @author xizhonghuai
 * @since 2022-06-17
 */
@RestController
@RequestMapping(ConstantUnit.API_PREFIX + "/role")
@Api(tags = "账户角色控制")
public class RoleController {


}
