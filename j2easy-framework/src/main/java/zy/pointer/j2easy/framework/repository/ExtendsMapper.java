package zy.pointer.j2easy.framework.repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 自定义的扩展接口
 */
public interface ExtendsMapper<Entity extends BaseEntity> {

    /**
     * 该方法设计上由 XML 的方式结合 Mybatis-plus 分页插件来实现.
     * 做法上,就是在 XML 中对 #{params.xxxx} 进行 if 判断后拼接查询SQL即可.
     *
     * @param page
     * @param params
     * @return
     */
    IPage<Entity> selectByMapForPage(Page<Entity> page , @Param("params") Map<String,Object> params);

}
