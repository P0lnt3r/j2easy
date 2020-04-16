package zy.pointer.j2easy.business.cms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.Transactional;
import zy.pointer.j2easy.business.cms.entity.Discuss;
import zy.pointer.j2easy.business.cms.mapper.DiscussMapper;
import zy.pointer.j2easy.business.cms.service.IDiscussService;
import zy.pointer.j2easy.framework.business.AbsBusinessService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhouyang
 * @since 2020-04-15
 */
@Service
@Primary
@Transactional
public class DiscussServiceImpl extends AbsBusinessService<DiscussMapper, Discuss> implements IDiscussService {

    @Override
    public IPage<Discuss> selectByMapForPage_extras(Page<Discuss> page, Map<String, Object> params) {
        return getBaseMapper().selectByMapForPage_extras(page,params);
    }

    @Override
    public IPage<Discuss> selectByMapForPage_replies(Page<Discuss> page, Map<String, Object> params) {
        return getBaseMapper().selectByMapForPage_replies(page , params);
    }
}
