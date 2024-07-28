package back.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 文件实体类
 */
@Data
@TableName("sys_file")
public class Files {
    /**
     * 文件ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 文件名称
     */
    private String name;
    /**
     * 文件类型
     */
    private String type;
    /**
     * 文件大小
     */
    private Long size;
    /**
     * 文件URL
     */
    private String url;
    /**
     * 文件MD5值
     */
    private String md5;
}
