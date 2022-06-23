package com.smart.mvc.mapper;

import com.smart.mvc.dto.MessageCenterDTO;
import com.smart.mvc.entity.MessageCenter;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smart.mvc.vo.MessageCenterVO;

import java.util.List;

/**
 * <p>
 * MESSAGE_CENTER Mapper 接口
 * </p>
 *
 * @author xizhonghuai
 * @since 2022-06-17
 */
public interface MessageCenterMapper extends BaseMapper<MessageCenter> {

    List<MessageCenterVO> list(MessageCenterDTO dto);
}
