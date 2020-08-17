package zy.pointer.j2easy.business.asset.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import zy.pointer.j2easy.business.asset.entity.AssetAccount;
import zy.pointer.j2easy.framework.business.BusinessService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhouyang
 * @since 2020-08-07
 */
public interface IAssetAccountService extends BusinessService<AssetAccount> {

    AssetAccount getByUserId_AssetId( String userId , Long assetId );

    AssetAccount getByWalletAddress( Long assetId , String walletAddress );

    AssetAccount initAssetAccount( String userId , Long assetId , String assetSymbol );

    AssetAccount increase( AssetAccount assetAccount , String addAmount );

    AssetAccount decrease( AssetAccount assetAccount , String subAmount );

    IPage< AssetAccount > getTokenAssetAccount(Page< AssetAccount > page , Long userId );

    IPage< AssetAccount > getCreditAssetAccount(Page< AssetAccount > page , Long userId );

    AssetAccount prepareSell( AssetAccount assetAccount , String amount );

    AssetAccount addFrozen( AssetAccount assetAccount , String amount );

    AssetAccount releaseFrozen( AssetAccount assetAccount , String amount );

    AssetAccount subFrozen( AssetAccount assetAccount , String amount );

    String getWalletAddress( String userId );

    IPage< AssetAccount > getTokenAssetAccountTokenId( Page< AssetAccount > page , Long tokenId  );

}
