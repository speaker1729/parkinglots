package back.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 数据字典实体类
 */
@TableName("sys_dict")
@Data
public class Dict {
    /**
     * 字典名称
     */
    private String name;
    /**
     * 字典值
     */
    private String value;
    /**
     * 字典类型
     */
    private String type;
}
