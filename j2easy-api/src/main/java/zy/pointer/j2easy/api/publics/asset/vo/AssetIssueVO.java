package zy.pointer.j2easy.api.publics.asset.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import zy.pointer.j2easy.business.asset.entity.AssetIssue;
import zy.pointer.j2easy.framework.web.model.vo.AbsValueObject;

import java.time.LocalDateTime;

@Data
public class AssetIssueVO extends AbsValueObject<AssetIssue>  {

    private Long id;

    private String assetName;

    private String assetSymbol;

    private String supply;

    private String mortgage;

    private String mortgageCNY;

    private String tradeAssetName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
