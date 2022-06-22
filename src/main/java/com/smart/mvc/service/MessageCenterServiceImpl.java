package com.smart.mvc.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.config.AuthContext;
import com.smart.mvc.entity.MessageCenter;
import com.smart.mvc.mapper.MessageCenterMapper;
import com.smart.mvc.vo.MessageCenterVO;
import com.smart.utils.Utils;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<MessageCenterVO> list(String message) {
        Long loginUserId = AuthContext.get().getLoginUserId();
        List<MessageCenter> list = list(Utils.lambdaQuery(MessageCenter.class)
                .eq(MessageCenter::getAccountId, loginUserId)
                .like(MessageCenter::getMessage, message));
        return BeanUtil.copyToList(list, MessageCenterVO.class);
    }
}
