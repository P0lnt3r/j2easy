package zy.pointer.j2easy.business.asset.service.impl;

import zy.pointer.j2easy.business.asset.entity.AssetDeal;
import zy.pointer.j2easy.business.asset.mapper.AssetDealMapper;
import zy.pointer.j2easy.business.asset.service.IAssetDealService;
import zy.pointer.j2easy.framework.business.AbsBusinessService;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhouyang
 * @since 2020-08-07
 */
@Service
@Transactional
@Primary
public class AssetDealServiceImpl extends AbsBusinessService<AssetDealMapper, AssetDeal> implements IAssetDealService {

}
