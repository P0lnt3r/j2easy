package zy.pointer.j2easy.business.asset.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import zy.pointer.j2easy.business.asset.entity.AssetAccount;
import zy.pointer.j2easy.business.asset.mapper.AssetAccountMapper;
import zy.pointer.j2easy.business.asset.service.IAssetAccountService;
import zy.pointer.j2easy.business.commons.BigDecimalUtil;
import zy.pointer.j2easy.business.commons.DictConstant;
import zy.pointer.j2easy.business.commons.ScaleUtils;
import zy.pointer.j2easy.framework.business.AbsBusinessService;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.Transactional;
import zy.pointer.j2easy.framework.repository.BaseEntity;

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
public class AssetAccountServiceImpl extends AbsBusinessService<AssetAccountMapper, AssetAccount> implements IAssetAccountService {

    @Override
    @Transactional( readOnly = true )
    public IPage<AssetAccount> getTokenAssetAccountTokenId(Page<AssetAccount> page , Long tokenId) {
        LambdaQueryWrapper< AssetAccount > queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq( AssetAccount::getAssetId , tokenId );
        queryWrapper.gt( AssetAccount::getTotalAmount , "0" );
        return getBaseMapper().selectPage( page , queryWrapper );
    }

    @Transactional( readOnly = true )
    @Override
    public AssetAccount getByWalletAddress(Long assetId, String walletAddress) {
        LambdaQueryWrapper< AssetAccount > queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq( AssetAccount::getAssetId , assetId );
        queryWrapper.eq( AssetAccount::getWalletAddress , walletAddress );
        return getBaseMapper().selectOne( queryWrapper );
    }

    @Override
    public String getWalletAddress(String userId) {
        return "0xb414ba499fe73512f01aca91ef2b92f6b3f9f26f";
    }

    @Override
    @Transactional( readOnly = true)
    public IPage<AssetAccount> getTokenAssetAccount(Page<AssetAccount> page, Long userId) {
        return getBaseMapper().selectTokenAccountAsset( page, userId );
    }

    @Override
    @Transactional( readOnly = true)
    public IPage<AssetAccount> getCreditAssetAccount(Page<AssetAccount> page, Long userId) {
        return getBaseMapper().selectCreditAccountAsset( page, userId );
    }

    @Override
    @Transactional( readOnly = true )
    public AssetAccount getByUserId_AssetId(String userId, Long assetId) {
        LambdaQueryWrapper<AssetAccount> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq( AssetAccount::getUserId , userId );
        queryWrapper.eq( AssetAccount::getAssetId , assetId );
        return getBaseMapper().selectOne( queryWrapper );
    }

    @Override
    public AssetAccount initAssetAccount( String userId, Long assetId, String assetSymbol) {
        AssetAccount assetAccount = new AssetAccount();
        assetAccount.setAssetId( assetId );
        assetAccount.setAssetSymbol( assetSymbol );
        assetAccount.setUserId( userId );
        assetAccount.setAssetType( DictConstant.ASSET_TYPE_TOKEN.value );
        assetAccount.setTotalAmount("0");
        assetAccount.setFrozenAmount("0");
        assetAccount.setAvaiAmount("0");
        assetAccount.setWalletAddress( getWalletAddress( userId ) );
        getBaseMapper().insert( assetAccount );
        return assetAccount;
    }

    @Override
    public AssetAccount subFrozen(AssetAccount assetAccount, String amount) {
        assetAccount = getForUpdate( assetAccount );
        int scale = ScaleUtils.getScale( assetAccount.getAssetId() );
        String totalAmount = BigDecimalUtil.strSub( assetAccount.getTotalAmount() , amount , scale);
        String frozenAmount = BigDecimalUtil.strSub( assetAccount.getFrozenAmount() , amount ,scale );
        assetAccount.setTotalAmount( totalAmount );
        assetAccount.setFrozenAmount( frozenAmount );
        getBaseMapper().updateById( assetAccount );
        return assetAccount;
    }

    @Override
    public AssetAccount addFrozen(AssetAccount assetAccount, String amount) {
        assetAccount = getForUpdate( assetAccount );
        int scale = ScaleUtils.getScale( assetAccount.getAssetId() );
        String totalAmount = BigDecimalUtil.strAdd( assetAccount.getTotalAmount() , amount , scale);
        String frozenAmount = BigDecimalUtil.strAdd( assetAccount.getFrozenAmount() , amount ,scale );
        assetAccount.setTotalAmount( totalAmount );
        assetAccount.setFrozenAmount( frozenAmount );
        getBaseMapper().updateById( assetAccount );
        return assetAccount;
    }

    @Override
    public AssetAccount releaseFrozen(AssetAccount assetAccount, String amount) {
        assetAccount = getForUpdate( assetAccount );
        int scale = ScaleUtils.getScale( assetAccount.getAssetId() );
        String avaiAmount = BigDecimalUtil.strAdd( assetAccount.getAvaiAmount() , amount , scale);
        String frozenAmount = BigDecimalUtil.strSub( assetAccount.getFrozenAmount() , amount ,scale );
        assetAccount.setAvaiAmount( avaiAmount );
        assetAccount.setFrozenAmount( frozenAmount );
        getBaseMapper().updateById( assetAccount );
        return assetAccount;
    }

    @Override
    public AssetAccount increase(AssetAccount assetAccount, String addAmount) {
        assetAccount = getForUpdate( assetAccount );
        int scale = ScaleUtils.getScale( assetAccount.getAssetId() );
        String totalAmount = BigDecimalUtil.strAdd( assetAccount.getTotalAmount() , addAmount , scale);
        String avaiAmount = BigDecimalUtil.strAdd( assetAccount.getAvaiAmount() , addAmount , scale );
        assetAccount.setTotalAmount( totalAmount );
        assetAccount.setAvaiAmount( avaiAmount );
        getBaseMapper().updateById( assetAccount );
        return assetAccount;
    }

    @Override
    public AssetAccount decrease(AssetAccount assetAccount, String subAmount) {
        assetAccount = getForUpdate( assetAccount );
        int scale = ScaleUtils.getScale( assetAccount.getAssetId() );
        String totalAmount = BigDecimalUtil.strSub( assetAccount.getTotalAmount() , subAmount , scale );
        String avaiAmount = BigDecimalUtil.strSub( assetAccount.getAvaiAmount() , subAmount , scale );
        assetAccount.setTotalAmount( totalAmount );
        assetAccount.setAvaiAmount( avaiAmount );
        getBaseMapper().updateById( assetAccount );
        return assetAccount;
    }

    @Override
    public AssetAccount prepareSell(AssetAccount assetAccount, String amount) {
        assetAccount = getForUpdate( assetAccount );
        int scale = ScaleUtils.getScale( assetAccount.getAssetId() );
        String avaiAmount = BigDecimalUtil.strSub( assetAccount.getAvaiAmount() , amount , scale );
        String frozenAmount = BigDecimalUtil.strAdd( assetAccount.getFrozenAmount() , amount , scale );
        assetAccount.setAvaiAmount( avaiAmount );
        assetAccount.setFrozenAmount( frozenAmount );
        getBaseMapper().updateById( assetAccount );
        return assetAccount;
    }

    private AssetAccount getForUpdate(AssetAccount assetAccount ){
        LambdaQueryWrapper< AssetAccount > queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq( BaseEntity::getId , assetAccount.getId() );
        queryWrapper.last("for update");
        return getBaseMapper().selectOne( queryWrapper );
    }


}
