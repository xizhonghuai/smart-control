package com.smart.mvc.service;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.config.AuthContext;
import com.smart.config.ConstantUnit;
import com.smart.mvc.dto.ShareDTO;
import com.smart.mvc.entity.ShareDevice;
import com.smart.mvc.mapper.ShareDeviceMapper;
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
    private AccountDeviceXrefServiceImpl accountDeviceXrefService;

    public List<ShareDeviceVO> myShareList() {
        Long accountId = AuthContext.get().getLoginUserId();
        List<ShareDeviceVO> shareDeviceVOS = getBaseMapper().shareList();
        List<ShareDeviceVO> vos = AuthContext.get().isAdmin() ? shareDeviceVOS :
                shareDeviceVOS.stream().filter(v -> Objects.equals(accountId, v.getFromAccountId())).collect(Collectors.toList());
        List<ConnectProperty> connectProperties = deviceControlService.onlineDevice();
        List<String> olineRegIds = connectProperties.stream().map(ConnectProperty::getRegId).collect(Collectors.toList());
        return vos.stream().peek(vo -> vo.setNetStatus(olineRegIds.contains(vo.getDeviceId()) ? 1 : 0)).collect(Collectors.toList());
    }

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
    public Boolean addShare(ShareDTO addShareDTO) {
        List<Long> deviceIds = addShareDTO.getDeviceIds();
        if (Objects.equals(addShareDTO.getToAccountId(), AuthContext.get().getLoginUserId())) {
            throw new RuntimeException("不能共享给自己");
        }
        if (AuthContext.get().isAdmin()) {
            return true;
        }
        deleteShare(addShareDTO);
        Long loginUserId = AuthContext.get().getLoginUserId();
        List<ShareDevice> shareDeviceList = deviceIds.stream().distinct().map(deviceId -> new ShareDevice().setDeviceId(deviceId)
                .setFromAccountId(loginUserId)
                .setToAccountId(addShareDTO.getToAccountId())).collect(Collectors.toList());
        Utils.insertBeforeAction(shareDeviceList);
        saveBatch(shareDeviceList);
        return true;
    }

    public Boolean deleteShare(ShareDTO deleteShareDTO) {
        List<Long> deviceIds = deleteShareDTO.getDeviceIds();
        if (CollectionUtil.isEmpty(deviceIds)) {
            throw new RuntimeException("空列表");
        }
        List<ShareDevice> list = list(Utils.lambdaQuery(ShareDevice.class).eq(ShareDevice::getFromAccountId, AuthContext.get().getLoginUserId())
                .eq(ShareDevice::getToAccountId, deleteShareDTO.getToAccountId())
                .in(ShareDevice::getDeviceId, deviceIds));
        if (!list.isEmpty()) {
            removeByIds(list.stream().map(ShareDevice::getId).collect(Collectors.toList()));
        }
        return true;
    }

    public void shareVerify(Long deviceId){
        List<Long> ids = shareToMe();
        if (!ids.contains(deviceId)){
            throw new RuntimeException("没有权限操作共享设备");
        }



    }


}
