package zy.pointer.j2easy.business.asset.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import zy.pointer.j2easy.business.asset.entity.AssetAccount;
import zy.pointer.j2easy.business.asset.entity.AssetIssue;
import zy.pointer.j2easy.business.asset.mapper.AssetIssueMapper;
import zy.pointer.j2easy.business.asset.service.IAssetAccountService;
import zy.pointer.j2easy.business.asset.service.IAssetFlowService;
import zy.pointer.j2easy.business.asset.service.IAssetIssueService;
import zy.pointer.j2easy.business.commons.BigDecimalUtil;
import zy.pointer.j2easy.business.commons.DictConstant;
import zy.pointer.j2easy.business.commons.ErrorCode;
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
public class AssetIssueServiceImpl extends AbsBusinessService<AssetIssueMapper, AssetIssue> implements IAssetIssueService {

    @Autowired
    IAssetAccountService assetAccountService;

    @Autowired
    IAssetFlowService assetFlowService;

    @Override
    public synchronized AssetIssue issue(AssetIssue assetIssue) {
        LambdaQueryWrapper< AssetIssue > queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq( AssetIssue::getAssetName,assetIssue.getAssetName());
        queryWrapper.or();
        queryWrapper.eq( AssetIssue::getAssetSymbol , assetIssue.getAssetSymbol());
        if ( getBaseMapper().selectCount( queryWrapper ) > 0 ){
            throw new BusinessException(ErrorCode.ERROR_CODE_2001);
        }
        // 计算需要抵押多少资产才能完成
        String supply = assetIssue.getSupply();
        String tradeRate = assetIssue.getTradeRate();
        String needMogerate = BigDecimalUtil.strMul2( supply , tradeRate , 6 );
        // 检查账户是否拥有足够的资产进行发行.
        String userId = assetIssue.getUserId();
        Long tradeAssetId = assetIssue.getTradeAssetId();
        AssetAccount assetAccount = assetAccountService.getByUserId_AssetId( userId , tradeAssetId );
        if ( BigDecimalUtil.strcompareTo( needMogerate , assetAccount.getAvaiAmount() ) ){
            throw new BusinessException( ErrorCode.ERROR_CODE_2002 );
        }
        assetIssue.setTradeAssetSymbol( assetAccount.getAssetSymbol() );
        assetIssue.setMortgage( needMogerate );
        assetIssue.setType(DictConstant.ASSET_TYPE_TOKEN.value);
        getBaseMapper().insert( assetIssue );
        // 流水记录完成资产变更
        assetFlowService.handleAssetIssue( assetAccount , assetIssue );
        return assetIssue;
    }

}
