package com.smart.mvc.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.config.AuthContext;
import com.smart.config.ConstantUnit;
import com.smart.mvc.dto.AddShareDTO;
import com.smart.mvc.dto.DeleteShareDTO;
import com.smart.mvc.entity.Account;
import com.smart.mvc.entity.ShareDevice;
import com.smart.mvc.mapper.ShareDeviceMapper;
import com.smart.mvc.vo.AccountVO;
import com.smart.mvc.vo.ShareDeviceVO;
import com.smart.utils.Utils;
import com.transmission.server.core.ConnectProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * SHARE_DEVICE 服务实现类
 * </p>
 *
 * @author xizhonghuai
 * @since 2022-06-17
 */
@Service
public class ShareDeviceServiceImpl extends ServiceImpl<ShareDeviceMapper, ShareDevice> implements IService<ShareDevice> {

    @Autowired
    private DeviceControlService deviceControlService;
    @Autowired
    private AccountServiceImpl accountService;


    public List<ShareDeviceVO> shareListToMe() {
        Long accountId = AuthContext.get().getLoginUserId();
        List<ShareDeviceVO> shareDeviceVOS = getBaseMapper().shareList();
        List<ShareDeviceVO> vos = AuthContext.get().isAdmin() ? shareDeviceVOS :
                shareDeviceVOS.stream().filter(v -> Objects.equals(accountId, v.getToAccountId())).collect(Collectors.toList());
        List<ConnectProperty> connectProperties = deviceControlService.onlineDevice();
        List<String> olineRegIds = connectProperties.stream().map(ConnectProperty::getRegId).collect(Collectors.toList());
        return vos.stream().peek(vo -> vo.setNetStatus(olineRegIds.contains(vo.getDeviceId()) ? 1 : 0)).collect(Collectors.toList());
    }

    public List<Long> shareToMe() {
        List<ShareDeviceVO> shareDeviceVOS = shareListToMe();
        return shareDeviceVOS.stream().map(ShareDeviceVO::getDeviceTableId).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean addShare(AddShareDTO addShareDTO) {
        String accountName = addShareDTO.getAccountName();
        if (StrUtil.isBlank(accountName) || addShareDTO.getDeviceId() == null) {
            throw new RuntimeException("参数错误");
        }
        if (Objects.equals(accountName, AuthContext.get().getLoginUser().getName())) {
            throw new RuntimeException("不能共享给自己");
        }
        List<Account> accounts = accountService.list(Utils.queryWrapper(new Account().setName(accountName)));
        if (accounts.isEmpty()) {
            throw new RuntimeException("用户不存在");
        }
        if (AuthContext.get().isAdmin()) {
            return true;
        }
        shareVerify(addShareDTO.getDeviceId());
        Account account = accounts.get(0);
        if (!Objects.equals(account.getRoleId(), ConstantUnit.userRoleId)) {
            throw new RuntimeException("不能共享给该类账户");
        }
        Long toAccountId = account.getId();
        List<ShareDevice> list = list(Utils.lambdaQuery(ShareDevice.class).eq(ShareDevice::getFromAccountId, AuthContext.get().getLoginUserId())
                .eq(ShareDevice::getToAccountId, toAccountId)
                .in(ShareDevice::getDeviceId, addShareDTO.getDeviceId()));
        if (list.isEmpty()) {
            ShareDevice shareDevice = new ShareDevice().setDeviceId(addShareDTO.getDeviceId())
                    .setToAccountId(toAccountId)
                    .setFromAccountId(AuthContext.get().getLoginUserId());
            Utils.insertBeforeAction(shareDevice);
            save(shareDevice);
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteShare(DeleteShareDTO deleteAddShareDTO) {
        if (deleteAddShareDTO.getDeviceId() == null || deleteAddShareDTO.getAccountId() == null) {
            throw new RuntimeException("参数错误");
        }
        List<ShareDevice> list = list(Utils.lambdaQuery(ShareDevice.class).eq(ShareDevice::getFromAccountId, AuthContext.get().getLoginUserId())
                .eq(ShareDevice::getToAccountId, deleteAddShareDTO.getAccountId())
                .in(ShareDevice::getDeviceId, deleteAddShareDTO.getDeviceId()));
        if (!list.isEmpty()) {
            removeByIds(list.stream().map(ShareDevice::getId).collect(Collectors.toList()));
        }
        return true;
    }

    public void shareVerify(Long deviceId) {
        List<Long> ids = shareToMe();
        if (ids.isEmpty()) {
            return;
        }
        if (ids.contains(deviceId)) {
            throw new RuntimeException("没有权限操作共享设备");
        }
    }


    public List<AccountVO> shareUserList(Long deviceId) {
        Long accountId = AuthContext.get().getLoginUserId();
        List<ShareDeviceVO> shareDeviceVOS = getBaseMapper().shareList();
        List<ShareDeviceVO> vos = AuthContext.get().isAdmin() ? shareDeviceVOS :
                shareDeviceVOS.stream().filter(v -> Objects.equals(deviceId, v.getDeviceTableId())).collect(Collectors.toList());
        return vos.stream().filter(v -> Objects.equals(accountId, v.getFromAccountId()))
                .map(shareDeviceVO -> new AccountVO().setId(shareDeviceVO.getToAccountId())
                        .setName(shareDeviceVO.getToAccountName()))
                .collect(Collectors.toList());
    }
}
