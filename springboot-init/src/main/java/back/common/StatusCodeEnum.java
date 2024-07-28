package back.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
/*
  状态码枚举类
 */
public enum StatusCodeEnum {
    /**
     * 操作成功
     */
    SUCCESS(0, "操作成功"),
    /**
     * 用户未登录
     */
    NO_LOGIN(100, "用户未登录"),
    AUTHORIZED(110, "没有操作权限"),
    SYSTEM_ERROR(500, "系统异常"),
    FAIL(501, "操作失败"),
    VALID_ERROR(502, "参数格式不正确"),
    USERNAME_EXIST(101, "用户名已存在"),
    USERNAME_NOT_EXIST(103, "用户名不存在");
    private final Integer code;
    private final String desc;
}
