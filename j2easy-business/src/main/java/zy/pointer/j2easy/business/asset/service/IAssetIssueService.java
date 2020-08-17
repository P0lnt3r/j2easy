package zy.pointer.j2easy.business.asset.service;

import zy.pointer.j2easy.business.asset.entity.AssetIssue;
import zy.pointer.j2easy.framework.business.BusinessService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhouyang
 * @since 2020-08-07
 */
public interface IAssetIssueService extends BusinessService<AssetIssue> {

    AssetIssue issue( AssetIssue assetIssue );

}
