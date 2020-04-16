package zy.pointer.j2easy.business.cms.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import zy.pointer.j2easy.business.cms.entity.Discuss;
import zy.pointer.j2easy.framework.repository.RepositoryMapper;

import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhouyang
 * @since 2020-04-15
 */
public interface DiscussMapper extends RepositoryMapper<Discuss> {

    IPage<Discuss> selectByMapForPage_extras( Page<Discuss> page , @Param("params") Map<String,Object> params );

    IPage<Discuss> selectByMapForPage_replies( Page<Discuss> page , @Param("params") Map<String,Object> params );

}
