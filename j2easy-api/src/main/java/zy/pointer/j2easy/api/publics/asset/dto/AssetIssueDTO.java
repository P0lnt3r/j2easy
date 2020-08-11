package zy.pointer.j2easy.api.publics.asset.dto;

import lombok.Data;
import zy.pointer.j2easy.business.asset.entity.AssetIssue;
import zy.pointer.j2easy.framework.web.model.dto.AbsDataTransferObject;

import javax.validation.constraints.NotNull;

@Data
public class AssetIssueDTO extends AbsDataTransferObject<AssetIssue> {

    @NotNull
    private String assetName;

    @NotNull
    private String assetSymbol;

    @NotNull
    private String issueMain;

    @NotNull
    private String tradeSymbol;

    @NotNull
    private Long tradeAssetId;

    @NotNull
    private String tradeRate;

    @NotNull
    private String supply;

    @NotNull
    private String assetDecimal;

}
