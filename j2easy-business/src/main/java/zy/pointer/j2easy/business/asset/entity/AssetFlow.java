package zy.pointer.j2easy.business.asset.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import zy.pointer.j2easy.framework.repository.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
/**
 * <p>
 * 
 * </p>
 *
 * @author zhouyang
 * @since 2020-08-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="AssetFlow对象", description="")
public class AssetFlow extends BaseEntity{

    private static final long serialVersionUID = 1L;

    @TableField("USER_ID")
    private String userId;

    @TableField("REF_ID")
    private Long refId;

    @TableField("TYPE")
    private String type;

    @TableField("TYPE_TINY")
    private String typeTiny;

    @TableField("ASSET_ID")
    private Long assetId;

    @TableField( "ASSET_SYMBOL" )
    private String assetSymbol;

    @TableField("ASSET_AMOUNT")
    private String assetAmount;

    @TableField("REF_UPDATE")
    private String refUpdate;

}
