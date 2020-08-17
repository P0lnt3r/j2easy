package zy.pointer.j2easy.api.publics.asset.dto;

import lombok.Data;

@Data
public class AssetTransferDTO {

    private Long assetId;

    private String walletAddress;

    private String amount;

}
