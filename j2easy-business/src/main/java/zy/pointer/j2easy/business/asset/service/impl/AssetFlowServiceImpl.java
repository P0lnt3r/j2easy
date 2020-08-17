package zy.pointer.j2easy.business.asset.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import zy.pointer.j2easy.business.asset.entity.*;
import zy.pointer.j2easy.business.asset.mapper.AssetFlowMapper;
import zy.pointer.j2easy.business.asset.service.IAssetAccountService;
import zy.pointer.j2easy.business.asset.service.IAssetFlowService;
import zy.pointer.j2easy.business.asset.service.IAssetTradeService;
import zy.pointer.j2easy.business.asset.service.IAssetTransferService;
import zy.pointer.j2easy.business.commons.BigDecimalUtil;
import zy.pointer.j2easy.business.commons.DictConstant;
import zy.pointer.j2easy.business.commons.ScaleUtils;
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
public class AssetFlowServiceImpl extends AbsBusinessService<AssetFlowMapper, AssetFlow> implements IAssetFlowService {

    @Autowired
    IAssetAccountService assetAccountService;

    @Autowired
    IAssetTransferService assetTransferService;

    @Override
    public void handleTradeDeal(AssetAccount buyer, AssetAccount seller, AssetDeal assetDeal) {
        int scale = ScaleUtils.getScale( buyer.getAssetId() );
        String sellerIncomeAssetAmount = BigDecimalUtil.strSub( assetDeal.getPayAssetAmount() , assetDeal.getPayFee() , scale );
        /** 处理信用资产流水变更 */
        // 买家账户减少资金
        buyer = assetAccountService.decrease( buyer , assetDeal.getPayAssetAmount() );
        // 卖家账户增加资金
        seller = assetAccountService.increase( seller , sellerIncomeAssetAmount );
        AssetFlow assetFlow_add = new AssetFlow();
        assetFlow_add.setUserId( seller.getUserId() );
        assetFlow_add.setType( DictConstant.ASSET_FLOW_TYPE_ADD.value );
        assetFlow_add.setTypeTiny( DictConstant.ASSET_FLOW_TYPE_ADD_SELLINCOME.value );
        assetFlow_add.setAssetAmount( sellerIncomeAssetAmount );
        assetFlow_add.setAssetSymbol( seller.getAssetSymbol() );
        assetFlow_add.setRefUpdate( seller.getTotalAmount() );
        assetFlow_add.setAssetId( buyer.getAssetId() );
        getBaseMapper().insert( assetFlow_add );
        AssetFlow assetFlow_sub = new AssetFlow();
        assetFlow_sub.setUserId( buyer.getUserId() );
        assetFlow_sub.setType(DictConstant.ASSET_FLOW_TYPE_SUB.value);
        assetFlow_sub.setTypeTiny( DictConstant.ASSET_FLOW_TYPE_SUB_PAY.value);
        assetFlow_sub.setAssetAmount( assetDeal.getPayAssetAmount() );
        assetFlow_sub.setAssetSymbol( buyer.getAssetSymbol() );
        assetFlow_sub.setRefUpdate( buyer.getTotalAmount() );
        assetFlow_sub.setAssetId( buyer.getAssetId() );
        getBaseMapper().insert( assetFlow_sub );
        /** 处理数字资产流水变更 */
        AssetAccount from = assetAccountService.getByUserId_AssetId( seller.getUserId() , assetDeal.getReturnAssetId() );
        AssetAccount to   = assetAccountService.getByUserId_AssetId( buyer.getUserId() , assetDeal.getReturnAssetId() );
        AssetTransfer transfer = new AssetTransfer();
        transfer.setState( DictConstant.ASSET_TRANSFER_STATE_WAIT.value );
        transfer.setType( DictConstant.ASSET_TRANSFER_TYPE_DOTRADE.value);
        transfer.setFromUserId( from.getUserId() );
        transfer.setToUserId( from.getUserId() );
        transfer.setTxId( assetTransferService.buildTxId() );
        transfer.setAssetId( assetDeal.getReturnAssetId() );
        transfer.setAssetAmount( assetDeal.getReturnAssetAmount() );
        assetTransferService.save( transfer );
        handleTransfer( from , to , transfer );
    }

    @Override
    public void handleTransfer(AssetAccount from, AssetAccount to, AssetTransfer transfer) {
        String amount = transfer.getAssetAmount();
        String transferType = transfer.getType();
        if ( DictConstant.ASSET_TRANSFER_TYPE_DOTRADE.value.equals( transferType ) ){
            // FROM 从冻结资金中减少资金
            from = assetAccountService.subFrozen( from , amount );
        }else{
            // FROM 减少资金
            from = assetAccountService.decrease( from , amount );
        }
        // TO   增加冻结资金
        to = assetAccountService.addFrozen( to , amount );
        AssetFlow assetFlow_add = new AssetFlow();
        assetFlow_add.setUserId( to.getUserId() );
        assetFlow_add.setType( DictConstant.ASSET_FLOW_TYPE_ADD.value );
        assetFlow_add.setTypeTiny( DictConstant.ASSET_FLOW_TYPE_ADD_TRANSIN.value );
        assetFlow_add.setAssetAmount( amount );
        assetFlow_add.setAssetSymbol( to.getAssetSymbol() );
        assetFlow_add.setRefUpdate( to.getTotalAmount() );
        assetFlow_add.setAssetId( to.getAssetId() );
        getBaseMapper().insert( assetFlow_add );
        AssetFlow assetFlow_sub = new AssetFlow();
        assetFlow_sub.setUserId( from.getUserId() );
        assetFlow_sub.setType(DictConstant.ASSET_FLOW_TYPE_SUB.value);
        assetFlow_sub.setTypeTiny( DictConstant.ASSET_FLOW_TYPE_SUB_TRANSOUT.value);
        assetFlow_sub.setAssetAmount( amount );
        assetFlow_sub.setAssetSymbol( to.getAssetSymbol() );
        assetFlow_sub.setRefUpdate( to.getTotalAmount() );
        assetFlow_sub.setAssetId( to.getAssetId() );
        getBaseMapper().insert( assetFlow_sub );
    }

    @Override
    public void handleAssetIssue(AssetAccount assetAccount, AssetIssue assetIssue) {
        String userId = assetIssue.getUserId();
        String supply = assetIssue.getSupply();
        String tradeRate = assetIssue.getTradeRate();
        String issueAssetAmount = BigDecimalUtil.strMul2( supply , tradeRate , 6 );
        // 为发行者初始化资金账户
        AssetAccount _assetAccount = assetAccountService.initAssetAccount( userId , assetIssue.getId() , assetIssue.getAssetSymbol() );
        // 原账户资金减少
        assetAccount = assetAccountService.decrease( assetAccount ,  issueAssetAmount );
        // 新账户资金增多
        _assetAccount = assetAccountService.increase( _assetAccount , supply );
        // 流水记录
        AssetFlow assetFlow_sub = new AssetFlow();
        assetFlow_sub.setUserId( userId );
        assetFlow_sub.setType(DictConstant.ASSET_FLOW_TYPE_SUB.value);
        assetFlow_sub.setTypeTiny( DictConstant.ASSET_FLOW_TYPE_SUB_MORTGAGE.value );
        assetFlow_sub.setAssetAmount( issueAssetAmount );
        assetFlow_sub.setAssetSymbol( assetAccount.getAssetSymbol() );
        assetFlow_sub.setRefUpdate( assetAccount.getTotalAmount() );
        assetFlow_sub.setAssetId( assetAccount.getAssetId() );
        getBaseMapper().insert( assetFlow_sub );
        AssetFlow assetFlow_add = new AssetFlow();
        assetFlow_add.setUserId( userId );
        assetFlow_add.setType( DictConstant.ASSET_FLOW_TYPE_ADD.value );
        assetFlow_add.setTypeTiny( DictConstant.ASSET_FLOW_TYPE_ADD_ISSUE.value );
        assetFlow_add.setAssetAmount( supply );
        assetFlow_add.setAssetSymbol( _assetAccount.getAssetSymbol() );
        assetFlow_add.setRefUpdate( _assetAccount.getTotalAmount() );
        assetFlow_add.setAssetId( _assetAccount.getAssetId() );
        getBaseMapper().insert( assetFlow_add );
    }
}
