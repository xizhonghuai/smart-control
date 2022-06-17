package com.smart.mvc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smart.mvc.entity.MessageCenter;
import com.smart.mvc.mapper.MessageCenterMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
