package com.smart.mvc.service;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.config.AuthContext;
import com.smart.config.ConstantUnit;
import com.smart.mvc.entity.AccountDeviceXref;
import com.smart.mvc.entity.Device;
import com.smart.mvc.mapper.AccountDeviceXrefMapper;
import com.smart.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public void deviceAuthVerify(List<Long> deviceIds) {
        Long loginUserId = AuthContext.get().getLoginUserId();
        if (AuthContext.get().isAdmin()) {
            return;
        }
        List<AccountDeviceXref> list = list(Utils.queryWrapper(new AccountDeviceXref().setAccountId(loginUserId)));
        List<Long> collect = list.stream().map(AccountDeviceXref::getId).collect(Collectors.toList());
        deviceIds.forEach(v -> {
            if (!collect.contains(v)) {
                throw new RuntimeException("没有权限操作" + v + "设备");
            }
        });
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
}
