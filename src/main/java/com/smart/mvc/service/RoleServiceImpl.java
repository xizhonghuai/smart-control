package com.smart.mvc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.mvc.entity.Role;
import com.smart.mvc.mapper.RoleMapper;
import com.smart.utils.Utils;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Long getRoleId(String roleName) {
        List<Role> list = list(Utils.queryWrapper(new Role().setName(roleName)));
        if (list.isEmpty()) {
            throw new RuntimeException("未找到角色:" + roleName);
        }
        return list.get(0).getId();
    }
}
