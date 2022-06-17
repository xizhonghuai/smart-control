package com.smart.mvc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smart.mvc.entity.ShareDevice;
import com.smart.mvc.mapper.ShareDeviceMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
