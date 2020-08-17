package zy.pointer.j2easy.business.asset.service;

import zy.pointer.j2easy.business.asset.entity.*;
import zy.pointer.j2easy.framework.business.BusinessService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhouyang
 * @since 2020-08-07
 */
public interface IAssetFlowService extends BusinessService<AssetFlow> {

    void handleAssetIssue(AssetAccount assetAccount , AssetIssue assetIssue);

    void handleTransfer(AssetAccount from , AssetAccount to , AssetTransfer assetTransfer);

    void handleTradeDeal(AssetAccount buyer , AssetAccount seller , AssetDeal assetDeal);

}
