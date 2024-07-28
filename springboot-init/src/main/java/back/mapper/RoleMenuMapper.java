package back.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import back.entity.RoleMenu;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RoleMenuMapper extends BaseMapper<RoleMenu> {
    /**
     * 按角色删除菜单id
     *
     * @param roleId 角色id
     * @return int
     */
    @Delete("delete from sys_role_menu where role_id = #{roleId}")
    int deleteByRoleId(@Param("roleId") Integer roleId);

    /**
     * 选择通过角色id
     *
     * @param roleId 角色id
     * @return {@code List<Integer>}
     */
    @Select("select menu_id from sys_role_menu where role_id = #{roleId}")
    List<Integer> selectByRoleId(@Param("roleId") Integer roleId);
}
