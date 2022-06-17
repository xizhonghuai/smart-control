package com.smart.mvc.service;

import com.smart.mvc.entity.Account;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * ACCOUNT 服务类
 * </p>
 *
 * @author xizhonghuai
 * @since 2022-06-17
 */
public interface IAccountService extends IService<Account> {
    @Override
    boolean saveOrUpdate(Account entity);
}
