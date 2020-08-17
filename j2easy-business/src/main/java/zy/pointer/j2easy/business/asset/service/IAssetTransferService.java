package zy.pointer.j2easy.business.asset.service;

import zy.pointer.j2easy.business.asset.entity.AssetAccount;
import zy.pointer.j2easy.business.asset.entity.AssetTransfer;
import zy.pointer.j2easy.framework.business.BusinessService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhouyang
 * @since 2020-08-07
 */
public interface IAssetTransferService extends BusinessService<AssetTransfer> {

    AssetTransfer transfer(AssetAccount from , AssetAccount to , String amount , String type);

    List<AssetTransfer> getAllUnConfirmTransfer();

    void handleUnconfirmTransfer( AssetTransfer assetTransfer );

    String buildTxId();

}
