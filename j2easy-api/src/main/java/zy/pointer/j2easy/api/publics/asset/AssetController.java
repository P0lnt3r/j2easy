package zy.pointer.j2easy.api.publics.asset;

import cn.hutool.core.bean.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zy.pointer.j2easy.api.bm.cms.vo.DiscussVO;
import zy.pointer.j2easy.api.publics.asset.dto.AssetAccountQueryDTO;
import zy.pointer.j2easy.api.publics.asset.dto.AssetIssueDTO;
import zy.pointer.j2easy.api.publics.asset.dto.AssetTradeDTO;
import zy.pointer.j2easy.api.publics.asset.dto.AssetTradeQueryDTO;
import zy.pointer.j2easy.api.publics.asset.vo.AssetAccountVO;
import zy.pointer.j2easy.api.publics.asset.vo.AssetTradeVO;
import zy.pointer.j2easy.business.asset.entity.AssetAccount;
import zy.pointer.j2easy.business.asset.entity.AssetIssue;
import zy.pointer.j2easy.business.asset.entity.AssetTrade;
import zy.pointer.j2easy.business.asset.service.IAssetAccountService;
import zy.pointer.j2easy.business.asset.service.IAssetIssueService;
import zy.pointer.j2easy.business.asset.service.IAssetTradeService;
import zy.pointer.j2easy.business.cms.entity.Discuss;
import zy.pointer.j2easy.business.commons.BigDecimalUtil;
import zy.pointer.j2easy.business.commons.ScaleUtils;
import zy.pointer.j2easy.framework.web.model.vo.PageVo;

@RestController
@RequestMapping("/api/publics/assets")
public class AssetController {
    @Autowired
    IAssetIssueService assetIssueService;
    @Autowired
    IAssetAccountService assetAccountService;
    @Autowired
    IAssetTradeService assetTradeService;

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
                    vo.setCreditAssetVal(BigDecimalUtil.strMul2( entity.getAvaiAmount() , entity.getTradeRate() , ScaleUtils.getScale( entity.getAssetId() )));
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
        assetTradeService.sell( assetAccount , amount );
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

}
