package zy.pointer.j2easy.api.publics.asset.dto;

import lombok.Data;
import zy.pointer.j2easy.business.asset.entity.AssetTrade;
import zy.pointer.j2easy.framework.web.model.dto.PageQueryDTO;

@Data
public class AssetTradeQueryDTO extends PageQueryDTO<AssetTrade> {

    private Long assetId;

}
