package zy.pointer.j2easy.api.publics.asset;

import cn.hutool.core.bean.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zy.pointer.j2easy.api.bm.cms.vo.DiscussVO;
import zy.pointer.j2easy.api.publics.asset.dto.*;
import zy.pointer.j2easy.api.publics.asset.vo.AssetAccountVO;
import zy.pointer.j2easy.api.publics.asset.vo.AssetIssueVO;
import zy.pointer.j2easy.api.publics.asset.vo.AssetTradeVO;
import zy.pointer.j2easy.api.publics.asset.vo.AssetTransferVO;
import zy.pointer.j2easy.business.asset.entity.*;
import zy.pointer.j2easy.business.asset.service.*;
import zy.pointer.j2easy.business.cms.entity.Discuss;
import zy.pointer.j2easy.business.commons.*;
import zy.pointer.j2easy.framework.exception.BusinessException;
import zy.pointer.j2easy.framework.web.model.vo.PageVo;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/publics/assets")
public class AssetController {
    @Autowired
    IAssetIssueService assetIssueService;
    @Autowired
    IAssetAccountService assetAccountService;
    @Autowired
    IAssetTradeService assetTradeService;
    @Autowired
    IAssetDealService assetDealService;
    @Autowired
    IAssetTransferService assetTransferService;

    /** 资产发行 */
    @PostMapping( "/issue" )
    public int issue(AssetIssueDTO DTO){
        AssetIssue assetIssue = DTO.convert();
        assetIssue.setUserId( "10000" );
        assetIssueService.issue( assetIssue );
        return 1;
    }

    /** 获取资产账户 */
    @GetMapping( "/getTokenAssetAccount" )
    public PageVo<AssetAccountVO , AssetAccount> getTokenAssetAccount(AssetAccountQueryDTO DTO){
        return new PageVo<AssetAccountVO, AssetAccount>().from(
                assetAccountService.getTokenAssetAccount( DTO.convert() , 10000L) ,
                AssetAccountVO.class ,
                ( entity , vo ) -> {
                    vo.from( entity , vo.getClass() );
                    vo.setCreditAssetVal(BigDecimalUtil.strMul2( entity.getTotalAmount() , entity.getTradeRate() , ScaleUtils.getScale( entity.getAssetId() )));
                    return vo;
                }
        );
    }

    @GetMapping( "/getCreditAssetAccount" )
    public PageVo<AssetAccountVO , AssetAccount> getCreditAssetAccount(AssetAccountQueryDTO DTO){
        return new PageVo<AssetAccountVO, AssetAccount>().from(
                assetAccountService.getCreditAssetAccount( DTO.convert() , 10000L) ,
                AssetAccountVO.class ,
                ( entity , vo ) -> {
                    vo.from( entity , vo.getClass() );
                    vo.setCreditAssetVal(BigDecimalUtil.strMul2( entity.getTotalAmount() , entity.getTradeRate() , ScaleUtils.getScale( entity.getAssetId() )));
                    return vo;
                }
        );
    }

    @GetMapping("/getAssetIssue")
    public PageVo<AssetIssueVO , AssetIssue> getAssetIssue( AssetIssueQueryDTO DTO ){
        String userId = "10000";
        Map<String,Object> params = BeanUtil.beanToMap( DTO );
        params.put( "userId", userId );
        return new PageVo<AssetIssueVO, AssetIssue>().from(
                assetIssueService.selectByMapForPage( DTO.convert() , params) ,
                AssetIssueVO.class ,
                ( entity , vo ) -> {
                    vo.from( entity , vo.getClass() );
                    String tradeAssetCNYRate = entity.getTradeAssetCNYRate();
                    String mortgageCNY = BigDecimalUtil.strMul2( tradeAssetCNYRate , entity.getMortgage() , 2 );
                    vo.setMortgageCNY( mortgageCNY );
                    return vo;
                }
        );
    }



    @PostMapping( "/assetTrade" )
    public int assetTrade(AssetTradeDTO DTO){
        String userId = "10000";
        Long assetId = DTO.getAssetId();
        String amount = DTO.getAmount();
        AssetAccount assetAccount = assetAccountService.getByUserId_AssetId( userId , assetId );
        String accountKey = "account:" + assetAccount.getId();
        try {
            LockPoolHelper.lock( accountKey , 10 );
            assetTradeService.sell( assetAccount , amount );
        }finally {
            LockPoolHelper.unlock( accountKey );
        }
        return 1;
    }

    /** 获取资产挂售 */
    @GetMapping( "/getTokenAssetTrade" )
    public PageVo<AssetTradeVO, AssetTrade> getTokenAssetTrade(AssetTradeQueryDTO DTO){
        Long assetId = DTO.getAssetId();
        return new PageVo<AssetTradeVO, AssetTrade>().from(
                assetTradeService.getAssetTrade( DTO.convert() , assetId) ,
                AssetTradeVO.class
        );
    }

    @GetMapping( "/getTokenAssetAccountTokenId" )
    public PageVo<AssetAccountVO, AssetAccount> getTokenAssetTrade(AssetAccountQueryDTO DTO){
        Long assetId = DTO.getAssetId();
        return new PageVo<AssetAccountVO, AssetAccount>().from(
                assetAccountService.getTokenAssetAccountTokenId( DTO.convert() , assetId) ,
                AssetAccountVO.class
        );
    }

    @GetMapping( "/getAssetTransfer" )
    public PageVo<AssetTransferVO, AssetTransfer> getTokenAssetTrade(AssetTransferQueryDTO DTO){
        Map<String,Object> params = BeanUtil.beanToMap( DTO );
        String userId = "10000";
        String type = DTO.getType();
        String walletAddress = "0xb414ba499fe73512f01aca91ef2b92f6b3f9f26f";
        if ( "2".equals( walletAddress ) ){
            params.put("fromWalletAddress" , walletAddress);
        }else {
            params.put("toWalletAddress" , walletAddress);
        }
        return new PageVo<AssetTransferVO, AssetTransfer>().from(
                assetTransferService.selectByMapForPage( DTO.convert() , params ),
                AssetTransferVO.class
        );
    }

    @PostMapping( "/assetDeal" )
    public int assetDeal(AssetDealDTO DTO){
        String userId = "10000";
        String amount = DTO.getAmount();
        AssetTrade assetTrade = assetTradeService.getById( DTO.getTradeId() );
        if ( assetTrade == null ){
            throw new BusinessException(ErrorCode.ERROR_CODE_2007);
        }
        if ( BigDecimalUtil.strequals( "0" , assetTrade.getLeftAmount() )
                || BigDecimalUtil.strcompareTo( amount , assetTrade.getLeftAmount() ) ){
            throw new BusinessException( ErrorCode.ERROR_CODE_2006 );
        }
        AssetAccount assetAccount = assetAccountService.getByUserId_AssetId( userId , assetTrade.getTradeAssetId() );
        String avaiAmount = assetAccount.getAvaiAmount();
        String tradePrice = assetTrade.getTradePrice();
        int scale = ScaleUtils.getScale( assetTrade.getTradeAssetId() );
        String needPayAmount = BigDecimalUtil.strMul2( tradePrice , amount , scale);
        needPayAmount = BigDecimalUtil.strAdd( needPayAmount , "1" /* fee */ , scale );
        if ( BigDecimalUtil.strcompareTo( needPayAmount , avaiAmount ) ){
            throw new BusinessException( ErrorCode.ERROR_CODE_2005 );
        }
        String accountKey = "account:" + assetAccount.getId();
        String tradeKey   = "trade:" + assetTrade.getId();
        String[] keys = new String[]{ accountKey , tradeKey };
        try{
            LockPoolHelper.lock( keys , 10 );
            assetAccount = assetAccountService.getById( assetAccount.getId() );
            assetTrade = assetTradeService.getById( assetTrade.getId() );
            assetTradeService.buy( assetAccount , assetTrade , amount );
        }finally {
            LockPoolHelper.unlock(keys);
        }
        return 1;
    }



    @PostMapping("/assetTransfer")
    public int assetTransfer( AssetTransferDTO DTO ){
        String userId = "10000";
        String amount = DTO.getAmount();
        Long assetId  = DTO.getAssetId();
        String walletAddress = DTO.getWalletAddress();
        AssetAccount from = assetAccountService.getByUserId_AssetId( userId , assetId );
        String avaiAmount = from.getAvaiAmount();
        if ( BigDecimalUtil.strcompareTo( amount , avaiAmount ) ){
            throw new BusinessException( ErrorCode.ERROR_CODE_2004 );
        }
        AssetAccount to = assetAccountService.getByWalletAddress( assetId , walletAddress );
        if ( to == null ){
            throw new BusinessException( ErrorCode.ERROR_CODE_2008 );
        }
        String fromKey = "account:" + from.getId();
        String toKey   = "account:" + to.getId();
        String[] keys = new String[]{ fromKey , toKey };
        try {
            LockPoolHelper.lock(keys , 10);
            assetTransferService.transfer( from , to , amount , DictConstant.ASSET_TRANSFER_TYPE_DOTRASFER.value);
        }finally {
            LockPoolHelper.unlock( keys );
        }
        return 1;
    }



}
