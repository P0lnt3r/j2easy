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
@ApiModel(value="AssetIssue对象", description="")
public class AssetIssue extends BaseEntity{

    private static final long serialVersionUID = 1L;

    @TableField("TYPE")
    private String type;

    @TableField("USER_ID")
    private String userId;

    @TableField("ASSET_SYMBOL")
    private String assetSymbol;

    @TableField("ASSET_NAME")
    private String assetName;

    @ApiModelProperty(value = "交易兑信用资产")
    @TableField("TRADE_ASSET_SYMBOL")
    private String tradeAssetSymbol;

    @ApiModelProperty(value = "发行比例")
    @TableField("TRADE_RATE")
    private String tradeRate;

    @ApiModelProperty(value = "抵押资产数量")
    @TableField("MORTGAGE")
    private String mortgage;

    @ApiModelProperty(value = "供应量")
    @TableField("SUPPLY")
    private String supply;

    @ApiModelProperty( value = "发行主体")
    @TableField( "ISSUE_MAIN" )
    private String issueMain;

    @ApiModelProperty( value = "资产精度")
    @TableField( "ASSET_DECIMAL" )
    private String assetDecimal;

    @ApiModelProperty( value = "抵押资产ID")
    @TableField( "TRADE_ASSET_ID" )
    private Long tradeAssetId;

    @TableField( exist = false )
    private String tradeAssetName;

    @TableField( exist = false)
    private String tradeAssetCNYRate;

}
