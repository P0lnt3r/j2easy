package zy.pointer.j2easy.business.asset.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import zy.pointer.j2easy.business.asset.entity.AssetAccount;
import zy.pointer.j2easy.framework.repository.RepositoryMapper;

import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhouyang
 * @since 2020-08-07
 */
public interface AssetAccountMapper extends RepositoryMapper<AssetAccount> {

    IPage<AssetAccount> selectTokenAccountAsset(Page<AssetAccount> page , @Param("userId") Long userId );

    IPage<AssetAccount> selectCreditAccountAsset(Page<AssetAccount> page , @Param("userId") Long userId );

}
