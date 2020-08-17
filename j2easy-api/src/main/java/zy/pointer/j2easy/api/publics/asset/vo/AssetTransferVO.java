package zy.pointer.j2easy.api.publics.asset.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import zy.pointer.j2easy.business.asset.entity.AssetTransfer;
import zy.pointer.j2easy.framework.web.model.vo.AbsValueObject;

import java.time.LocalDateTime;

@Data
public class AssetTransferVO extends AbsValueObject<AssetTransfer> {

    private String txId;

    private String assetAmount;

    private String assetSymbol;

    private String fromWalletAddress;

    private String toWalletAddress;

    private String state;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
