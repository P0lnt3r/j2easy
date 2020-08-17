package zy.pointer.j2easy.business.asset.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import zy.pointer.j2easy.business.asset.entity.AssetAccount;
import zy.pointer.j2easy.business.asset.entity.AssetDeal;
import zy.pointer.j2easy.business.asset.entity.AssetIssue;
import zy.pointer.j2easy.business.asset.entity.AssetTrade;
import zy.pointer.j2easy.business.asset.mapper.AssetTradeMapper;
import zy.pointer.j2easy.business.asset.service.*;
import zy.pointer.j2easy.business.commons.*;
import zy.pointer.j2easy.framework.business.AbsBusinessService;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.Transactional;
import zy.pointer.j2easy.framework.exception.BusinessException;

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
public class AssetTradeServiceImpl extends AbsBusinessService<AssetTradeMapper, AssetTrade> implements IAssetTradeService {

    @Autowired
    IAssetIssueService assetIssueService;

    @Autowired
    IAssetAccountService assetAccountService;

    @Autowired
    IAssetFlowService assetFlowService;

    @Autowired
    IAssetDealService assetDealService;

    @Override
    public AssetTrade buy(AssetAccount assetAccount, AssetTrade assetTrade, String amount /* 购入数字货币资产数量 */ ) {
        String fixedFee = "1";
        int scale = ScaleUtils.getScale( assetAccount.getAssetId() );
        String needAmount = BigDecimalUtil.strMul2( assetTrade.getTradePrice() , amount , scale );
        needAmount = BigDecimalUtil.strAdd( needAmount , fixedFee , scale );
        String avaiAmount = assetAccount.getAvaiAmount();
        if ( BigDecimalUtil.strcompareTo( needAmount , avaiAmount ) ){
            throw new BusinessException( ErrorCode.ERROR_CODE_2005 );
        }
        String leftAmount = assetTrade.getLeftAmount();
        if ( BigDecimalUtil.strcompareTo( amount , leftAmount ) ){
            throw new BusinessException( ErrorCode.ERROR_CODE_2006 );
        }
        AssetDeal assetDeal = new AssetDeal();
        assetDeal.setPayAssetAmount( needAmount );
        assetDeal.setPayAssetId( assetTrade.getTradeAssetId() );
        assetDeal.setPayFee( "1" );
        assetDeal.setReturnAssetAmount( amount );
        assetDeal.setReturnAssetId( assetTrade.getAssetId() );
        assetDeal.setUserId( assetAccount.getUserId() );
        assetDeal.setTradeId( assetTrade.getId() );
        assetDealService.save( assetDeal );
        AssetAccount buyer = assetAccount;
        AssetAccount seller = assetAccountService.getByUserId_AssetId( assetTrade.getUserId() , assetTrade.getTradeAssetId() );
        assetFlowService.handleTradeDeal( assetAccount , seller ,assetDeal);
        // assetTrade 减少 LEFT
        handleAssetDeal( assetTrade , assetDeal );
        return assetTrade;
    }

    private AssetTrade handleAssetDeal( AssetTrade assetTrade , AssetDeal assetDeal ){
        LambdaQueryWrapper<AssetTrade> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq( AssetTrade::getId , assetTrade.getId() );
        queryWrapper.last( "for update" );
        assetTrade = getBaseMapper().selectOne( queryWrapper );

        int scale = ScaleUtils.getScale( assetTrade.getAssetId() );
        String dealAmount  = BigDecimalUtil.strAdd( assetTrade.getDealAmount() , assetDeal.getReturnAssetAmount() , scale );
        String leftAmount  = BigDecimalUtil.strSub( assetTrade.getLeftAmount() , assetDeal.getReturnAssetAmount() , scale );

        int _scale = ScaleUtils.getScale( assetDeal.getPayAssetId() );
        String tradeAmount = BigDecimalUtil.strAdd( assetTrade.getTradeAmount() , assetDeal.getPayAssetAmount() , _scale );
        String feeIncome = BigDecimalUtil.strAdd( assetTrade.getFeeIncome() , assetDeal.getPayFee() , _scale );

        assetTrade.setDealAmount(dealAmount);
        assetTrade.setLeftAmount( leftAmount );
        assetTrade.setTradeAmount( tradeAmount );
        assetTrade.setFeeIncome( feeIncome );
        if ( BigDecimalUtil.strequals( "0" , leftAmount ) ){
            assetTrade.setState(DictConstant.ASSET_TRADE_STATE_SELLOUT.value);
        }
        getBaseMapper().updateById( assetTrade );
        return assetTrade;
    }


    @Override
    public AssetTrade sell(AssetAccount assetAccount, String amount) {
        String avaiAmount = assetAccount.getAvaiAmount();
        if (BigDecimalUtil.strcompareTo( amount , avaiAmount )){
            throw new BusinessException(ErrorCode.ERROR_CODE_2003);
        }
        AssetIssue assetIssue = assetIssueService.getById( assetAccount.getAssetId() );
        AssetTrade assetTrade = new AssetTrade();
        assetTrade.setAssetId( assetAccount.getAssetId() );
        assetTrade.setDealAmount("0");
        assetTrade.setLeftAmount( amount );
        assetTrade.setSellAmount( amount );
        assetTrade.setTradeAssetId( assetIssue.getTradeAssetId() );
        assetTrade.setTradePrice( assetIssue.getTradeRate() );
        assetTrade.setUserId( assetAccount.getUserId() );
        assetTrade.setTradeAmount( "0" );
        assetTrade.setFeeIncome("0");
        assetTrade.setState( DictConstant.ASSET_TRADE_STATE_SELLING.value );
        getBaseMapper().insert( assetTrade );
        assetAccountService.prepareSell( assetAccount , amount );
        return assetTrade;
    }

    @Override
    @Transactional( readOnly = true )
    public IPage<AssetTrade> getAssetTrade(Page<AssetTrade> page, Long assetId) {
        return getBaseMapper().getAssetTrade( page , assetId );
    }
}
