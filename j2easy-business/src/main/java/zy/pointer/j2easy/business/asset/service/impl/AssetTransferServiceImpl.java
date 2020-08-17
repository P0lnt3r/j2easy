package zy.pointer.j2easy.business.asset.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import zy.pointer.j2easy.business.asset.entity.AssetAccount;
import zy.pointer.j2easy.business.asset.entity.AssetTransfer;
import zy.pointer.j2easy.business.asset.mapper.AssetTransferMapper;
import zy.pointer.j2easy.business.asset.service.IAssetAccountService;
import zy.pointer.j2easy.business.asset.service.IAssetFlowService;
import zy.pointer.j2easy.business.asset.service.IAssetTransferService;
import zy.pointer.j2easy.business.commons.BigDecimalUtil;
import zy.pointer.j2easy.business.commons.DictConstant;
import zy.pointer.j2easy.business.commons.ErrorCode;
import zy.pointer.j2easy.business.commons.LockPoolHelper;
import zy.pointer.j2easy.framework.business.AbsBusinessService;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.Transactional;
import zy.pointer.j2easy.framework.exception.BusinessException;

import java.time.LocalDateTime;
import java.util.List;

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
public class AssetTransferServiceImpl extends AbsBusinessService<AssetTransferMapper, AssetTransfer> implements IAssetTransferService {

    @Autowired
    IAssetFlowService assetFlowService;

    @Autowired
    IAssetAccountService assetAccountService;

    @Override
    public String buildTxId() {
        return "0x000000000000000000000000000000000000000000";
    }

    @Override
    @Transactional( readOnly = true )
    public List<AssetTransfer> getAllUnConfirmTransfer() {
        LambdaQueryWrapper<AssetTransfer> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq( AssetTransfer::getState , DictConstant.ASSET_TRANSFER_STATE_WAIT.value );
        return getBaseMapper().selectList( queryWrapper );
    }

    @Override
    public void handleUnconfirmTransfer(AssetTransfer assetTransfer) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime txTime = assetTransfer.getCreateTime();
        if ( txTime.plusSeconds( 5L ).isBefore( now ) ){
            assetTransfer.setState( DictConstant.ASSET_TRANSFER_STATE_CONFIRMED.value );
            getBaseMapper().updateById( assetTransfer );
            AssetAccount assetAccount = assetAccountService.getByUserId_AssetId( assetTransfer.getToUserId() , assetTransfer.getAssetId() );
            assetAccountService.releaseFrozen( assetAccount , assetTransfer.getAssetAmount() );
        }
    }

    @Override
    public AssetTransfer transfer(AssetAccount from, AssetAccount to, String amount , String type ) {
        AssetTransfer transfer = null;
        String fromAvaiAmount = from.getAvaiAmount();
        if (BigDecimalUtil.strcompareTo( amount , fromAvaiAmount )){
            throw new BusinessException(ErrorCode.ERROR_CODE_2004);
        }
        transfer = new AssetTransfer();
        transfer.setType( type );
        transfer.setAssetAmount( amount );
        transfer.setAssetId( from.getAssetId() );
        transfer.setFromUserId( from.getUserId() );
        transfer.setToUserId( to.getUserId() );
        transfer.setTxId( buildTxId() );
        transfer.setState(DictConstant.ASSET_TRANSFER_STATE_WAIT.value);
        getBaseMapper().insert( transfer );
        assetFlowService.handleTransfer( from , to , transfer );
        return transfer;
    }

}
