package zy.pointer.j2easy.business.asset.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import zy.pointer.j2easy.business.asset.entity.AssetAccount;
import zy.pointer.j2easy.business.asset.entity.AssetTrade;
import zy.pointer.j2easy.framework.business.BusinessService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhouyang
 * @since 2020-08-07
 */
public interface IAssetTradeService extends BusinessService<AssetTrade> {

    AssetTrade sell(AssetAccount assetAccount , String amount);

    /**
     *
     * @param assetAccount  哪个账户
     * @param assetTrade    哪笔挂单
     * @param amount        成交多少
     * @return
     */
    AssetTrade buy( AssetAccount assetAccount , AssetTrade assetTrade , String amount );

    IPage< AssetTrade > getAssetTrade(Page< AssetTrade > page , Long assetId );

}
