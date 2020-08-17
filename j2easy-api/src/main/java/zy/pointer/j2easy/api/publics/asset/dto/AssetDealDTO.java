package zy.pointer.j2easy.api.publics.asset.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AssetDealDTO {

    @NotNull
    private Long tradeId;

    @NotNull
    private String amount;

}
