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
@ApiModel(value="AssetTrade对象", description="")
public class AssetTrade extends BaseEntity{

    private static final long serialVersionUID = 1L;

    @TableField("TYPE")
    private Integer type;

    @TableField("USER_ID")
    private String userId;

    @TableField("ASSET_ID")
    private Long assetId;

    @TableField("TRADE_ASSET_ID")
    private Long tradeAssetId;

    @TableField("TRADE_PRICE")
    private String tradePrice;

    @TableField("TRADE_AMOUNT")
    private String tradeAmount;

    @TableField( "SELL_AMOUNT" )
    private String sellAmount;

    @TableField("DEAL_AMOUNT")
    private String dealAmount;

    @TableField("LEFT_AMOUNT")
    private String leftAmount;

    @TableField("STATE")
    private String state;

    @TableField("FEE_INCOME")
    private String feeIncome;

    /////////////////////////////////////////////////////////////////

    @TableField( exist = false)
    private String assetName;

    @TableField( exist = false)
    private String assetSymbol;

    @TableField( exist = false)
    private String tradeAssetName;

    @TableField( exist = false)
    private String tradeAssetSymbol;


}
