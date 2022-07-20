package com.smart.mvc.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.config.AuthContext;
import com.smart.config.ConstantUnit;
import com.smart.mvc.dto.MessageCenterDTO;
import com.smart.mvc.entity.Account;
import com.smart.mvc.entity.AccountDeviceXref;
import com.smart.mvc.entity.Device;
import com.smart.mvc.entity.MessageCenter;
import com.smart.mvc.mapper.MessageCenterMapper;
import com.smart.mvc.vo.MessageCenterVO;
import com.smart.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * MESSAGE_CENTER 服务实现类
 * </p>
 *
 * @author xizhonghuai
 * @since 2022-06-17
 */
@Service
public class MessageCenterServiceImpl extends ServiceImpl<MessageCenterMapper, MessageCenter> implements IService<MessageCenter> {

    @Autowired
    private DeviceServiceImpl deviceService;
    @Autowired
    private AccountDeviceXrefServiceImpl accountDeviceXrefService;
    @Autowired
    private AccountServiceImpl accountService;

    public List<MessageCenter> myMessage(String message) {
        Long loginUserId = AuthContext.get().getLoginUserId();
        return StrUtil.isBlank(message) ? list(Utils.lambdaQuery(MessageCenter.class)
                .eq(MessageCenter::getToAccountId, loginUserId)) : list(Utils.lambdaQuery(MessageCenter.class)
                .eq(MessageCenter::getToAccountId, loginUserId)
                .like(MessageCenter::getMessage, message));
    }

    public List<MessageCenterVO> list(MessageCenterDTO dto) {
        if (!AuthContext.get().isAdmin()) {
            return Collections.emptyList();
        }
        return getBaseMapper().list(dto);
    }

    public Boolean feedback(MessageCenterDTO dto) {
        if (AuthContext.get().isAdmin()) {
            throw new RuntimeException("无效操作");
        }
        Long userId = AuthContext.get().getLoginUserId();
        List<Account> agentAccounts = accountDeviceXrefService.listAgentByAccountId(userId);
        List<Account> accounts = accountService.list(Utils.queryWrapper(new Account().setRoleId(ConstantUnit.adminRoleId)));
        accounts.addAll(agentAccounts);
        List<MessageCenter> collect = accounts.stream().map(account -> {
            MessageCenter messageCenter = new MessageCenter();
            messageCenter.setMessage(dto.getMessage());
            messageCenter.setFromAccountId(userId);
            messageCenter.setToAccountId(account.getId());
            messageCenter.setReadFlag(0);
            Utils.insertBeforeAction(messageCenter);
            return messageCenter;
        }).collect(Collectors.toList());
        return saveBatch(collect);
    }

    public void sendDeviceWarningMessage(String deviceId, String message) {
        List<Device> list = deviceService.list(Utils.queryWrapper(new Device().setDeviceId(deviceId)));
        if (list.isEmpty()) {
            return;
        }
        Device device = list.get(0);
        List<AccountDeviceXref> xrefs = accountDeviceXrefService.list(Utils.queryWrapper(new AccountDeviceXref().setDeviceId(device.getId())));
        List<Long> accountIds = xrefs.stream().map(AccountDeviceXref::getAccountId).collect(Collectors.toList());
        List<MessageCenter> collect = accountIds.stream().map(accountId -> new MessageCenter()
                .setMessage(message)
                .setReadFlag(0)
                .setFromAccountId(ConstantUnit.adminId)
                .setToAccountId(accountId)).peek(Utils::insertBeforeAction).collect(Collectors.toList());
        saveBatch(collect);
    }

    public Boolean updateReadFlag(Long id) {
        MessageCenter messageCenter = getById(id);
        if (Objects.equals(AuthContext.get().getLoginUserId(), messageCenter.getToAccountId())) {
            messageCenter.setReadFlag(1);
            return updateById(messageCenter);
        }
        return false;
    }


}
