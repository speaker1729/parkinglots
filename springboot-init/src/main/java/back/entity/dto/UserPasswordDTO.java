package back.entity.dto;

import lombok.Data;

/**
 * 用户密码DTO类
 */
@Data
public class UserPasswordDTO {
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户手机号
     */
    private String phone;
    /**
     * 旧密码
     */
    private String password;
    /**
     * 新密码
     */
    private String newPassword;
}
