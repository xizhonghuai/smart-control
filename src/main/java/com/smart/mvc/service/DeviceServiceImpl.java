package com.smart.mvc.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.cache.CacheService;
import com.smart.config.AuthContext;
import com.smart.config.ConstantUnit;
import com.smart.config.SpringUtil;
import com.smart.mvc.dto.AddDeviceDTO;
import com.smart.mvc.dto.DeviceBaseInfoDTO;
import com.smart.mvc.dto.EditDeviceDTO;
import com.smart.mvc.dto.QueryDeviceDTO;
import com.smart.mvc.entity.Account;
import com.smart.mvc.entity.AccountDeviceXref;
import com.smart.mvc.entity.Device;
import com.smart.mvc.entity.ShareDevice;
import com.smart.mvc.mapper.DeviceMapper;
import com.smart.mvc.vo.DeviceBaseInfoVO;
import com.smart.mvc.vo.DeviceVO;
import com.smart.utils.Utils;
import com.transmission.server.core.ConnectProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * DEVICE 服务实现类
 * </p>
 *
 * @author xizhonghuai
 * @since 2022-06-17
 */
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements IService<Device> {
    @Autowired
    private AccountDeviceXrefServiceImpl accountDeviceXrefService;
    @Autowired
    private DeviceControlService deviceControlService;
    @Autowired
    private ShareDeviceServiceImpl shareDeviceService;
    @Autowired
    private AccountServiceImpl accountService;
    @Autowired
    private CacheService cacheService;

    public static DeviceServiceImpl get() {
        return SpringUtil.getObject(DeviceServiceImpl.class);
    }

    @Transactional(rollbackFor = Exception.class)
    public Long addDevice(AddDeviceDTO addDeviceDTO) {
        Device device = BeanUtil.toBean(addDeviceDTO, Device.class);
        List<Device> oldList = list(Utils.lambdaQuery(Device.class).eq(Device::getDeviceId, addDeviceDTO.getDeviceId()));
        if (oldList.isEmpty()) {
            throw new RuntimeException("该设备未注册");
        }
        device.setId(oldList.get(0).getId());
        updateById(device);
        accountDeviceXrefService.bind(device.getId());
        return device.getId();
    }


    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteDevice(Long id) {
        accountDeviceXrefService.deviceAuthVerify(id, true);
        accountDeviceXrefService.unBind(id);
        removeDevice(id);
        return true;
    }

    public Boolean editDevice(EditDeviceDTO editDeviceDTO) {
        Long id = editDeviceDTO.getId();
        accountDeviceXrefService.deviceAuthVerify(id, true);
        Device device = BeanUtil.toBean(editDeviceDTO, Device.class);
        Utils.updateBeforeAction(device);
        return updateById(device);
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeDevice(Long id) {
        if (AuthContext.get().isAdmin()) {
            removeById(id);
            accountDeviceXrefService.remove(Utils.queryWrapper(new AccountDeviceXref().setDeviceId(id)));
            shareDeviceService.remove(Utils.queryWrapper(new ShareDevice().setDeviceId(id)));
        }
    }


    public List<DeviceVO> list(QueryDeviceDTO queryDeviceDTO) {
        List<Device> list = list(Utils.queryWrapper(BeanUtil.toBean(queryDeviceDTO, Device.class)));
        List<Device> devices = accountDeviceXrefService.authFilter(list, true);
        List<DeviceVO> deviceVOS = BeanUtil.copyToList(devices, DeviceVO.class);
        List<ConnectProperty> connectProperties = deviceControlService.onlineDevice();
        List<String> olineRegIds = connectProperties.stream().map(ConnectProperty::getRegId).collect(Collectors.toList());
        List<Long> shareIds = accountDeviceXrefService.shareToMe();
        return deviceVOS.stream().peek(deviceVO -> {
            deviceVO.setNetStatus(olineRegIds.contains(deviceVO.getDeviceId()) ? 1 : 0);
            deviceVO.setIsShare(shareIds.contains(deviceVO.getId()) ? 1 : 0);

            String key = ConstantUnit.WARNING_CACHE_KEY_FUNCTION.apply(deviceVO.getDeviceId());
            Object warningValue = cacheService.getValue(key);
            deviceVO.setIsWarning(warningValue == null ? 0 : 1);

            String workStatusKey = ConstantUnit.WORK_STATUS_CACHE_KEY_FUNCTION.apply(deviceVO.getDeviceId());
            Object workStatus = cacheService.getValue(workStatusKey);
            deviceVO.setWorkStatus(workStatus == null ? null : workStatus.toString());

            String runModeKey = ConstantUnit.RUNNING_MODEL_DESCRIPTION_CACHE_KEY_FUNCTION.apply(deviceVO.getDeviceId());
            Object runModeValue = cacheService.getValue(runModeKey);
            deviceVO.setWorkStatus(runModeValue == null ? null : runModeValue.toString());

        }).collect(Collectors.toList());
    }


    public void addDevice(String deviceId) {
        List<Device> oldList = list(Utils.lambdaQuery(Device.class).in(Device::getDeviceId, deviceId));
        if (!oldList.isEmpty()) {
            return;
        }
        Device device = new Device().setDeviceId(deviceId).setId(Utils.createSnowflakeId()).setName("未命名的设备").setCreatedBy(AuthContext.get().getLoginUserId()).setUpdatedBy(AuthContext.get().getLoginUserId());
        save(device);
    }

    public void bindBatch(Long accountId, List<Long> deviceIds) {
        accountDeviceXrefService.bindBatch(accountId, deviceIds);
    }

    public List<Device> listNotBind() {
        if (Boolean.FALSE.equals(AuthContext.get().isAdmin())) {
            return Collections.emptyList();
        }
        List<Device> list = list();
        List<AccountDeviceXref> deviceXrefs = accountDeviceXrefService.list();
        List<Long> bindDeviceIds = deviceXrefs.stream().map(AccountDeviceXref::getDeviceId).collect(Collectors.toList());
        return list.stream().filter(v -> !bindDeviceIds.contains(v.getId())).collect(Collectors.toList());
    }

    public List<DeviceBaseInfoVO> listDeviceBaseInfo() {
        return listDeviceBaseInfo(new DeviceBaseInfoDTO());
    }

    public List<DeviceBaseInfoVO> listDeviceBaseInfo(DeviceBaseInfoDTO dto) {
        if (Boolean.FALSE.equals(AuthContext.get().isAdmin())) {
            return Collections.emptyList();
        }
        List<Device> deviceList = list();
        List<Account> accountList = accountService.list();
        Map<Long, Account> accountMap = accountList.stream().collect(Collectors.toMap(Account::getId, v -> v));
        List<AccountDeviceXref> accountDeviceXrefs = accountDeviceXrefService.list();
        Map<Long, List<AccountDeviceXref>> deviceAccountMap = accountDeviceXrefs.stream().collect(Collectors.groupingBy(AccountDeviceXref::getDeviceId));
        List<ConnectProperty> connectProperties = deviceControlService.onlineDevice();
        List<String> olineRegIds = connectProperties.stream().map(ConnectProperty::getRegId).collect(Collectors.toList());
        return deviceList.stream().map(device -> {
            DeviceBaseInfoVO infoVO = BeanUtil.toBean(device, DeviceBaseInfoVO.class);
            List<AccountDeviceXref> deviceXrefs = deviceAccountMap.get(device.getId());
            if (deviceXrefs != null) {
                List<Account> accounts = deviceXrefs.stream().map(v -> accountMap.get(v.getAccountId())).collect(Collectors.toList());
                accounts.forEach(v -> {
                    if (Objects.equals(v.getRoleId(), ConstantUnit.agentRoleId)) {
                        infoVO.setAgentId(v.getId());
                        infoVO.setAgentName(v.getName());
                    }
                    if (Objects.equals(v.getRoleId(), ConstantUnit.userRoleId)) {
                        infoVO.setOwnerId(v.getId());
                        infoVO.setOwnerName(v.getName());
                    }
                });
            }
            infoVO.setNetStatus(olineRegIds.contains(device.getDeviceId()) ? 1 : 0);
            return infoVO;
        }).filter(v -> {
            boolean result = true;
            if (StrUtil.isBlank(dto.getDeviceName())) {
                result = v.getDeviceName().contains(dto.getDeviceName());
            }
            if (StrUtil.isBlank(dto.getDeviceId())) {
                result = result && v.getDeviceId().contains(dto.getDeviceId());
            }
            if (Objects.equals(dto.getIsBind(), 0)) {
                result = result && (v.getAgentId() == null && v.getOwnerId() == null);
            }
            if (StrUtil.isBlank(dto.getAgentName())) {
                result = result && v.getAgentName().contains(dto.getAgentName());
            }
            if (StrUtil.isBlank(dto.getOwnerName())) {
                result = result && v.getOwnerName().contains(dto.getOwnerName());
            }
            if (dto.getNetStatus() != null) {
                result = result && v.getNetStatus() == dto.getNetStatus();
            }
            return result;
        }).collect(Collectors.toList());
    }


    public Boolean isAccountDeviceExist(String deviceId) {
        if (StrUtil.isBlank(deviceId)) {
            return false;
        }
        List<Device> list = list(Utils.queryWrapper(new Device().setDeviceId(deviceId)));
        if (list.size() > 0) {
            Long id = list.get(0).getId();
            return accountDeviceXrefService.isAccountDeviceExist(id, AuthContext.get().getLoginUserId());
        }
        return false;
    }

    public DeviceVO findByDeviceId(String deviceId) {
        List<DeviceVO> list = list(new QueryDeviceDTO().setDeviceId(deviceId));
        return CollectionUtil.isEmpty(list) ? null : list.get(0);

    }
}
