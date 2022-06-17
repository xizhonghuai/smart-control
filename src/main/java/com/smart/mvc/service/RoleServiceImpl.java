package com.smart.mvc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smart.mvc.entity.Role;
import com.smart.mvc.mapper.RoleMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * ROLE 服务实现类
 * </p>
 *
 * @author xizhonghuai
 * @since 2022-06-17
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IService<Role> {

}
