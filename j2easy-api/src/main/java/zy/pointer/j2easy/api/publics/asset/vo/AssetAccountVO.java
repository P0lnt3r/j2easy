package zy.pointer.j2easy.api.publics.asset.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import zy.pointer.j2easy.business.asset.entity.AssetAccount;
import zy.pointer.j2easy.framework.web.model.vo.AbsValueObject;

import java.time.LocalDateTime;

@Data
public class AssetAccountVO extends AbsValueObject<AssetAccount> {

    private Long id;

    private Long assetId;

    private String assetSymbol;

    private String totalAmount;

    private String frozenAmount;

    private String avaiAmount;

    private Long tradeAssetId;

    private String tradeAssetSymbol;

    private String tradeRate;

    private String creditAssetVal;

    private String assetName;

    private String walletAddress;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

}
