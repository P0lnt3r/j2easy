package zy.pointer.j2easy.api.publics.asset.dto;

import lombok.Data;
import zy.pointer.j2easy.business.asset.entity.AssetTransfer;
import zy.pointer.j2easy.framework.web.model.dto.AbsDataTransferObject;
import zy.pointer.j2easy.framework.web.model.dto.PageQueryDTO;

@Data
public class AssetTransferQueryDTO extends PageQueryDTO<AssetTransfer> {

    private String type;

}
