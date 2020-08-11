package zy.pointer.j2easy.api.publics.asset.vo;

import lombok.Data;
import zy.pointer.j2easy.business.asset.entity.AssetAccount;
import zy.pointer.j2easy.framework.web.model.vo.AbsValueObject;

@Data
public class AssetAccountVO extends AbsValueObject<AssetAccount> {

    private Long id;

    private Long assetId;

    private String assetSymbol;

    private String avaiAmount;

    private Long tradeAssetId;

    private String tradeAssetSymbol;

    private String creditAssetVal;

}
