package back.entity.dto;

import back.entity.Menu;
import lombok.Data;

import java.util.List;

/**
 * 接受前端登录请求的参数DTO类
 */
@Data
public class UserDTO {
    /**
     * 用户ID
     */
    private Integer id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户密码
     */
    private String password;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 用户头像URL
     */
    private String avatarUrl;
    /**
     * 用户手机号
     */
    private String phone;
    /**
     * 用户登录令牌
     */
    private String token;
    /**
     * 用户角色
     */
    private String role;
    /**
     * 用户菜单列表
     */
    private List<Menu> menus;
}
