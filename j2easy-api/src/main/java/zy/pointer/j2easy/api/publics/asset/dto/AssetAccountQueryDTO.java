package zy.pointer.j2easy.api.publics.asset.dto;

import lombok.Data;
import zy.pointer.j2easy.business.asset.entity.AssetAccount;
import zy.pointer.j2easy.framework.web.model.dto.PageQueryDTO;

@Data
public class AssetAccountQueryDTO extends PageQueryDTO<AssetAccount> {

    private Long assetId;

}
