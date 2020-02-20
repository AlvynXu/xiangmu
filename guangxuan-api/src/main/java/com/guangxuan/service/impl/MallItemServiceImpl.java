package com.guangxuan.service.impl;

import com.guangxuan.enumration.BusinessFailEnum;
import com.guangxuan.exception.BusinessException;
import com.guangxuan.model.MallItem;
import com.guangxuan.mapper.MallItemMapper;
import com.guangxuan.service.MallItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 项目信息 服务实现类
 * </p>
 *
 * @author zhuolin
 * @since 2019-11-27
 */
@Service
public class MallItemServiceImpl extends ServiceImpl<MallItemMapper, MallItem> implements MallItemService {

    @Override
    public List<MallItem> getItems(Long orderId) {
        return this.getBaseMapper().getItems(orderId);
    }

}
