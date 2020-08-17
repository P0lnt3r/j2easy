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
@ApiModel(value="AssetAccount对象", description="")
public class AssetAccount extends BaseEntity{

    private static final long serialVersionUID = 1L;

    @TableField("USER_ID")
    private String userId;

    @TableField("WALLET_ADDRESS")
    private String walletAddress;

    @TableField("ASSET_TYPE")
    private String assetType;

    @TableField("ASSET_ID")
    private Long assetId;

    @TableField( "ASSET_SYMBOL" )
    private String assetSymbol;

    @TableField("TOTAL_AMOUNT")
    private String totalAmount;

    @TableField("AVAI_AMOUNT")
    private String avaiAmount;

    @TableField("FROZEN_AMOUNT")
    private String frozenAmount;

    //////////////////////////////////////////////////////////////////////////

    @TableField( exist = false)
    private Long tradeAssetId;

    @TableField( exist = false)
    private String tradeAssetSymbol;

    @TableField( exist = false)
    private String tradeRate;

    @TableField( exist = false)
    private String assetName;


}
