package back.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ApiModel(value = "Reservations对象", description = "预订信息实体类")
public class Reservations implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value = "reservation_id", type = IdType.AUTO)
    @ApiModelProperty("预订id")
    private Integer reservationId;
    @ApiModelProperty("用户id")
    private Integer userId;
    @ApiModelProperty("停车场id")
    private Integer parkingLotId;
    @ApiModelProperty("预订时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date reservationTime;
    @ApiModelProperty("预订开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @ApiModelProperty("预订结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    @ApiModelProperty("状态：0-未使用，1-已使用，2-取消预订")
    private Integer status;
    @ApiModelProperty("车牌号")
    private String licensePlate;
}
