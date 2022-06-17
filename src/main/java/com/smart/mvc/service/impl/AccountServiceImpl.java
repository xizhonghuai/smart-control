package com.smart.mvc.service.impl;

import com.smart.mvc.entity.Account;
import com.smart.mvc.mapper.AccountMapper;
import com.smart.mvc.service.IAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements IAccountService {
    @Override
    public boolean saveOrUpdate(Account entity) {
        return super.saveOrUpdate(entity);
    }
}
