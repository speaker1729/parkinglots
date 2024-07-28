package back.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 停车场类
 */
@Getter
@Setter
@TableName("parking_lots")
@ApiModel(value = "ParkingLots对象", description = "停车场实体类")
public class ParkingLots implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 停车场ID
     */
    @TableId(value = "parking_lot_id", type = IdType.AUTO)
    @ApiModelProperty("id")
    private Integer parkingLotId;
    /**
     * 停车场位置
     */
    @ApiModelProperty("位置")
    private String location;
    /**
     * 停车场价格
     */
    @ApiModelProperty("价格")
    private BigDecimal price;
    /**
     * 停车场状态
     */
    @ApiModelProperty("状态")
    private Integer status;
    /**
     * 停车场所有者ID
     */
    @ApiModelProperty("所有者id")
    private Integer ownerId;
    /**
     * 可用起始时间
     */
    @ApiModelProperty("可用起始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date availableFrom;
    /**
     * 可用结束时间
     */
    @ApiModelProperty("可用结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date availableTo;
    /**
     * 停车场描述
     */
    @ApiModelProperty("描述")
    private String description;
    /**
     * 停车场图片
     */
    @ApiModelProperty("图片")
    private String image;
    /**
     * 停车场总数
     */
    private Integer sum;
    /**
     * 用户ID
     */
    private Integer userId;
    /**
     * 停车场所有者姓名
     */
    private String ownerName;
    /**
     * 停车场所有者电话
     */
    private String ownerPhone;
}
