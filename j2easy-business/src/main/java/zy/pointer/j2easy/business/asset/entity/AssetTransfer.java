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
@ApiModel(value="AssetTransfer对象", description="")
public class AssetTransfer extends BaseEntity{

    private static final long serialVersionUID = 1L;

    @TableField( "TYPE" )
    private String type;

    @TableField("FROM_USER_ID")
    private String fromUserId;

    @TableField("TO_USER_ID")
    private String toUserId;

    @TableField("ASSET_ID")
    private Long assetId;

    @TableField("ASSET_AMOUNT")
    private String assetAmount;

    @TableField( "TX_ID" )
    private String txId;

    @TableField("STATE")
    private String state;

    @TableField( exist = false)
    private String fromWalletAddress;

    @TableField( exist = false)
    private String toWalletAddress;

    @TableField( exist = false)
    private String assetSymbol;

}
