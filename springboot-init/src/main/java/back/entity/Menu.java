package back.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单类
 */
@Getter
@Setter
@TableName("sys_menu")
@ApiModel(value = "Menu对象", description = "菜单实体类")
public class Menu implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 菜单ID
     */
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 菜单名称
     */
    @ApiModelProperty("名称")
    private String name;
    /**
     * 菜单路径
     */
    @ApiModelProperty("路径")
    private String path;
    /**
     * 菜单图标
     */
    @ApiModelProperty("图标")
    private String icon;
    /**
     * 菜单描述
     */
    @ApiModelProperty("描述")
    private String description;
    /**
     * 子菜单列表
     */
    @TableField(exist = false)
    private List<Menu> children;
    /**
     * 父菜单ID
     */
    private Integer pid;
    /**
     * 页面路径
     */
    private String pagePath;
    /**
     * 排序号
     */
    private String sortNum;
}
