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
@ApiModel(value="AssetDeal对象", description="")
public class AssetDeal extends BaseEntity{

    private static final long serialVersionUID = 1L;

    @TableField("TRADE_ID")
    private Long tradeId;

    @TableField("USER_ID")
    private String userId;

    @TableField("PAY_ASSET_ID")
    private Long payAssetId;

    @TableField("PAY_ASSET_AMOUNT")
    private String payAssetAmount;

    @TableField("RETURN_ASSET_ID")
    private Long returnAssetId;

    @TableField("RETURN_ASSET_AMOUNT")
    private String returnAssetAmount;

    @TableField("PAY_FEE")
    private String payFee;

}
