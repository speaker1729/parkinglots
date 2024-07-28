package back.entity.dto;

import back.entity.User;
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
@ApiModel(value = "ReservationsDTO", description = "预定信息DTO类")
public class ReservationsDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value = "reservation_id", type = IdType.AUTO)
    @ApiModelProperty(value = "预订ID")
    private Integer reservationId; // 预订ID
    @ApiModelProperty(value = "预订开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime; // 预订开始时间
    @ApiModelProperty(value = "预订结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime; // 预订结束时间
    @ApiModelProperty(value = "车牌号")
    private String licensePlate; // 车牌号
    @ApiModelProperty(value = "用户信息")
    private User user; // 用户信息
}
