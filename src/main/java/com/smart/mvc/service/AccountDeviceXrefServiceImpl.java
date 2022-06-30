package com.smart.mvc.service;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.config.AuthContext;
import com.smart.config.ConstantUnit;
import com.smart.mvc.entity.Account;
import com.smart.mvc.entity.AccountDeviceXref;
import com.smart.mvc.entity.Device;
import com.smart.mvc.mapper.AccountDeviceXrefMapper;
import com.smart.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * ACCOUNT_DEVICE_XREF 服务实现类
 * </p>
 *
 * @author xizhonghuai
 * @since 2022-06-17
 */
@Service
public class AccountDeviceXrefServiceImpl extends ServiceImpl<AccountDeviceXrefMapper, AccountDeviceXref> implements IService<AccountDeviceXref> {

    @Autowired
    private ShareDeviceServiceImpl shareDeviceService;
    @Autowired
    private AccountServiceImpl accountService;

    public void deviceAuthVerify(Long deviceId, boolean containsShared) {
        if (containsShared) {
            shareDeviceService.shareVerify(deviceId);
        }
        deviceAuthVerify(deviceId);
    }

    public void deviceAuthVerify(Long deviceId) {
        Long loginUserId = AuthContext.get().getLoginUserId();
        if (AuthContext.get().isAdmin()) {
            return;
        }
        List<AccountDeviceXref> list = list(Utils.queryWrapper(new AccountDeviceXref().setAccountId(loginUserId)));
        if (list.stream().map(AccountDeviceXref::getDeviceId).anyMatch(v -> Objects.equals(deviceId, v))) {
            return;
        }
        throw new RuntimeException("没有权限操作此设备");
    }

    public List<Device> authFilter(List<Device> src, boolean containsShared) {
        Long loginUserId = AuthContext.get().getLoginUserId();
        if (CollectionUtil.isEmpty(src) || AuthContext.get().isAdmin()) {
            return src;
        }
        List<AccountDeviceXref> list = list(Utils.queryWrapper(new AccountDeviceXref().setAccountId(loginUserId)));
        List<Long> collect = list.stream().map(AccountDeviceXref::getDeviceId).collect(Collectors.toList());
        if (containsShared) {
            collect.addAll(shareDeviceService.shareToMe());
        }
        return src.stream().filter(v -> collect.contains(v.getId())).collect(Collectors.toList());

    }

    public List<Device> authFilter(List<Device> src) {
        return authFilter(src, false);
    }

    public List<Long> shareToMe() {
        return shareDeviceService.shareToMe();
    }

    public Boolean isBind(Long deviceId, Long currentRoleId) {
        List<AccountDeviceXref> list = list(Utils.lambdaQuery(AccountDeviceXref.class).eq(AccountDeviceXref::getDeviceId, deviceId));
        List<Long> accountIds = list.stream().map(AccountDeviceXref::getAccountId).distinct().collect(Collectors.toList());
        if (accountIds.isEmpty()) {
            return false;
        }
        List<Account> accounts = accountService.listByIds(accountIds);
        if (Objects.equals(currentRoleId, ConstantUnit.userRoleId)) {
            return accounts.stream().anyMatch(v -> Objects.equals(v.getRoleId(), ConstantUnit.userRoleId));
        }
        if (Objects.equals(currentRoleId, ConstantUnit.agentRoleId)) {
            return accounts.stream().anyMatch(v -> Objects.equals(v.getRoleId(), ConstantUnit.agentRoleId));
        }
        throw new RuntimeException("currentRoleId 参数错误");
    }

    public Boolean isAccountDeviceExist(Long deviceId, Long accountId) {
        List<AccountDeviceXref> list = list(Utils.lambdaQuery(AccountDeviceXref.class)
                .eq(AccountDeviceXref::getAccountId, accountId)
                .eq(AccountDeviceXref::getDeviceId, deviceId));
        return list.size() > 0;
    }

    public void bind(Long deviceId) {
        if (AuthContext.get().isAdmin()) {
            return;
        }
        List<AccountDeviceXref> list = list(Utils.lambdaQuery(AccountDeviceXref.class).eq(AccountDeviceXref::getDeviceId, deviceId));
        if (list.stream().anyMatch(v -> Objects.equals(v.getAccountId(), AuthContext.get().getLoginUserId()))) {
            return;
        }
        if (isBind(deviceId, AuthContext.get().getLoginUserRoleId())) {
            throw new RuntimeException("已绑定其他账户，请先解绑");
        }
        AccountDeviceXref accountDeviceXref = new AccountDeviceXref().setDeviceId(deviceId).setAccountId(AuthContext.get().getLoginUserId());
        Utils.insertBeforeAction(accountDeviceXref);
        save(accountDeviceXref);
       /* if (list.size() == 0 && AuthContext.get().isAgent()) {
            AccountDeviceXref accountDeviceXref = new AccountDeviceXref().setDeviceId(deviceId).setAccountId(AuthContext.get().getLoginUserId());
            Utils.insertBeforeAction(accountDeviceXref);
            save(accountDeviceXref);
            return;
        }
        if (list.size() ==1 && AuthContext.get().isUser()) {
            AccountDeviceXref accountDeviceXref = new AccountDeviceXref().setDeviceId(deviceId).setAccountId(AuthContext.get().getLoginUserId());
            Utils.insertBeforeAction(accountDeviceXref);
            save(accountDeviceXref);
            return;
        }
        throw new RuntimeException("非法绑定");*/
    }

    public void unBind(Long deviceId) {
        if (AuthContext.get().isAdmin()) {
            return;
        }
        List<AccountDeviceXref> list = list(Utils.lambdaQuery(AccountDeviceXref.class).eq(AccountDeviceXref::getDeviceId, deviceId).eq(AccountDeviceXref::getAccountId, AuthContext.get().getLoginUserId()));
        if (!list.isEmpty()) {
            removeByIds(list.stream().map(AccountDeviceXref::getId).collect(Collectors.toList()));
        }
    }

    public void bindBatch(Long accountId, List<Long> deviceIds) {
        if (!AuthContext.get().isAdmin()) {
            throw new RuntimeException("没有权限");
        }
        Account account = accountService.getById(accountId);
        if (!Objects.equals(account.getRoleId(), ConstantUnit.agentRoleId)) {
            throw new RuntimeException("仅代理商账号使用");
        }
        List<AccountDeviceXref> list = list(Utils.lambdaQuery(AccountDeviceXref.class).in(AccountDeviceXref::getDeviceId, deviceIds).eq(AccountDeviceXref::getAccountId, accountId));
        if (!list.isEmpty()) {
            removeByIds(list.stream().map(AccountDeviceXref::getId).collect(Collectors.toList()));
        }
        List<AccountDeviceXref> collect = deviceIds.stream().distinct().map(deviceId -> new AccountDeviceXref().setAccountId(accountId).setDeviceId(deviceId)).collect(Collectors.toList());
        Utils.insertBeforeAction(collect);
        saveBatch(collect);
    }

    public List<Account> listAgentByDeviceId(List<Long> deviceIds) {
        List<Account> accounts = accountService.list(Utils.queryWrapper(new Account().setRoleId(ConstantUnit.agentRoleId)));
        Map<Long, Account> accountMap = accounts.stream().collect(Collectors.toMap(Account::getId, v -> v));
        List<AccountDeviceXref> xrefs = list(Utils.lambdaQuery(AccountDeviceXref.class).in(AccountDeviceXref::getDeviceId, deviceIds));
        return xrefs.stream().map(AccountDeviceXref::getAccountId).distinct().map(accountMap::get).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public List<Account> listAgentByAccountId(Long accountId) {
        List<AccountDeviceXref> accountDeviceXrefs = list(Utils.queryWrapper(new AccountDeviceXref().setAccountId(accountId)));
        List<Long> deviceIds = accountDeviceXrefs.stream().map(AccountDeviceXref::getDeviceId).collect(Collectors.toList());
        return listAgentByDeviceId(deviceIds);
    }
}
