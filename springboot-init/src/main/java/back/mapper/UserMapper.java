package back.mapper;

import back.entity.dto.UserPasswordDTO;
import back.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;

/**
 * 用户映射器
 */
public interface UserMapper extends BaseMapper<User> {
    /**
     * 更新密码
     *
     * @param userPasswordDTO 用户密码dto
     * @return int
     */
    @Update("update sys_user set password = #{newPassword} where username = #{username} and password = #{password}")
    int updatePassword(UserPasswordDTO userPasswordDTO);
}
