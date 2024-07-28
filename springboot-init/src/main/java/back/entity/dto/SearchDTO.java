package back.entity.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 搜索DTO类
 */
@Data
public class SearchDTO {
    /**
     * 名称
     */
    private String name;
    /**
     * 价格区间起始值
     */
    private BigDecimal beginPrice;
    /**
     * 价格区间结束值
     */
    private BigDecimal endPrice;
    /**
     * 创建时间区间起始值
     */
    private Date beginTime;
    /**
     * 创建时间区间结束值
     */
    private Date endTime;
    /**
     * 用户手机号
     */
    private String userPhone;
    /**
     * 状态：0-已预定，1-未预定
     */
    private Integer status;
    /**
     * 分页查询起始页码
     */
    private Integer pageNum;
    /**
     * 分页查询每页条数
     */
    private Integer pageSize;
}
