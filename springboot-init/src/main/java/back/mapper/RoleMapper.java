package back.mapper;

import back.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Mapper 接口
 */
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 选择目标角色
     *
     * @param flag 角色标识
     * @return {@code Integer}
     */
    @Select("select id from sys_role where flag = #{flag}")
    Integer selectByFlag(@Param("flag") String flag);
}
