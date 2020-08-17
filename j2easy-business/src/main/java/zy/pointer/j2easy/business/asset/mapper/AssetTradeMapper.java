package zy.pointer.j2easy.business.asset.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import zy.pointer.j2easy.business.asset.entity.AssetTrade;
import zy.pointer.j2easy.framework.repository.RepositoryMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhouyang
 * @since 2020-08-07
 */
public interface AssetTradeMapper extends RepositoryMapper<AssetTrade> {

    IPage< AssetTrade > getAssetTrade(Page< AssetTrade > page , @Param("tradeAssetId") Long assetId);

}
