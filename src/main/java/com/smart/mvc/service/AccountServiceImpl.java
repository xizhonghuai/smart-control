package com.smart.mvc.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.communication.AuthenticationService;
import com.smart.enums.SystemRoleTypeEnum;
import com.smart.mvc.dto.RegAccountDTO;
import com.smart.mvc.entity.Account;
import com.smart.mvc.mapper.AccountMapper;
import com.smart.utils.Utils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * ACCOUNT 服务实现类
 * </p>
 *
 * @author xizhonghuai
 * @since 2022-06-17
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> {
    private final AuthenticationService authenticationService = new AuthenticationService();

    public boolean accountIsExist(String username) {
        return getOne(Utils.queryWrapper(new Account().setName(username))) != null;
    }

    public String login(String username, String password) {
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            throw new RuntimeException("用户名或密码不能为空");
        }
        Account account = getOne(Utils.queryWrapper(new Account().setName(username)));
        if (account == null) {
            throw new RuntimeException("用户不存在");
        }
        if (!account.getPassword().equals(password)) {
            throw new RuntimeException("密码错误");
        }
        if (account.getEnabled() == 0) {
            throw new RuntimeException("该账号已禁用");
        }
        return authenticationService.createToken(account.setPassword(null));
    }

    public Boolean regAccount(RegAccountDTO dto) {
        if (StrUtil.isBlank(dto.getName()) || StrUtil.isBlank(dto.getPassword())) {
            throw new RuntimeException("用户名或密码不能为空");
        }
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new RuntimeException("两次输入密码不一致");
        }
        if (accountIsExist(dto.getName())) {
            throw new RuntimeException("用户已存在");
        }
        Account account = BeanUtil.toBean(dto, Account.class);
        account.setRoleId(SystemRoleTypeEnum.USER.getId());
        account.setEnabled(1);
        Utils.insertBeforeAction(account);
        return save(account);
    }
}
