package zy.pointer.j2easy.api.publics.asset.vo;

import lombok.Data;
import zy.pointer.j2easy.business.asset.entity.AssetTrade;
import zy.pointer.j2easy.framework.web.model.vo.AbsValueObject;

@Data
public class AssetTradeVO extends AbsValueObject<AssetTrade> {

    private Long id;

    private Long assetId;

    private String assetName;

    private String assetSymbol;

    private Long tradeAssetId;

    private String tradeAssetName;

    private String tradeAssetSymbol;

    private String tradePrice;

    private String leftAmount;

}
