package com.smart.mvc.mapper;

import com.smart.mvc.entity.ShareDevice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smart.mvc.vo.ShareDeviceVO;

import java.util.List;

/**
 * <p>
 * SHARE_DEVICE Mapper 接口
 * </p>
 *
 * @author xizhonghuai
 * @since 2022-06-17
 */
public interface ShareDeviceMapper extends BaseMapper<ShareDevice> {
    List<ShareDeviceVO> shareList();
}
