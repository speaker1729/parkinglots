package back.service;

import back.entity.User;
import back.entity.dto.UserDTO;
import back.entity.dto.UserPasswordDTO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 用户服务类接口
 */
public interface IUserService extends IService<User> {
    /**
     * 登录
     *
     * @param userDTO 用户DTO
     * @return {@code UserDTO}
     */
    UserDTO login(UserDTO userDTO);

    /**
     * 注册
     *
     * @param userDTO 用户DTO
     * @return {@code User}
     */
    User register(UserDTO userDTO);

    /**
     * 更新密码
     *
     * @param userPasswordDTO 用户密码DTO
     */
    void updatePassword(UserPasswordDTO userPasswordDTO);

    /**
     * 获取安全密码
     *
     * @param pwd 明文密码
     * @return 安全密码
     */
    String getSafePwd(String pwd);
}
