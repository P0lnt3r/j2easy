package zy.pointer.j2easy.business.cms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import zy.pointer.j2easy.business.cms.entity.Discuss;
import zy.pointer.j2easy.framework.business.BusinessService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhouyang
 * @since 2020-04-15
 */
public interface IDiscussService extends BusinessService<Discuss> {

    IPage<Discuss> selectByMapForPage_extras(Page<Discuss> page , @Param("params") Map<String,Object> params);

    IPage<Discuss> selectByMapForPage_replies(Page<Discuss> page , @Param("params") Map<String,Object> params);

}
