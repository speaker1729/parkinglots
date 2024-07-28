package back.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.formula.functions.T;

import static back.common.StatusCodeEnum.FAIL;
import static back.common.StatusCodeEnum.SUCCESS;

/**
 * 接口统一返回包装类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private Boolean flag; // 返回结果是否成功的标识
    private Integer code; // 返回结果的状态码
    private String message; // 返回结果的消息
    private Object data; // 返回结果的数据

    /**
     * 返回成功结果，没有数据
     */
    public static Result ok() {
        return resultVO(true, SUCCESS.getCode(), SUCCESS.getDesc(), null);
    }

    /**
     * 返回成功结果，包含数据
     */
    public static Result ok(Object data) {
        return resultVO(true, SUCCESS.getCode(), SUCCESS.getDesc(), data);
    }

    /**
     * 返回成功结果，包含数据和消息
     */
    public static Result ok(Object data, String message) {
        return resultVO(true, SUCCESS.getCode(), message, data);
    }

    /**
     * 返回失败结果，没有数据
     */
    public static Result fail() {
        return resultVO(false, FAIL.getCode(), FAIL.getDesc(), null);
    }

    /**
     * 返回失败结果，包含状态码和消息
     */
    public static Result fail(StatusCodeEnum statusCodeEnum) {
        return resultVO(false, statusCodeEnum.getCode(), statusCodeEnum.getDesc(), null);
    }

    /**
     * 返回失败结果，只包含消息
     */
    public static Result fail(String message) {
        return resultVO(false, message);
    }

    /**
     * 返回失败结果，包含数据
     */
    public static Result fail(Object data) {
        return resultVO(false, FAIL.getCode(), FAIL.getDesc(), data);
    }

    /**
     * 返回失败结果，包含数据和消息
     */
    public static Result fail(Object data, String message) {
        return resultVO(false, FAIL.getCode(), message, data);
    }

    /**
     * 返回失败结果，包含状态码、消息和数据
     */
    public static Result fail(Integer code, String message) {
        return resultVO(false, code, message, null);
    }

    /**
     * 返回结果的私有方法，根据标识、状态码、消息和数据构建返回结果对象
     */
    private static Result resultVO(Boolean flag, Integer code, String message, Object data) {
        return Result.builder()
                .flag(flag)
                .code(code)
                .message(message)
                .data(data).build();
    }

    /**
     * 返回结果的私有方法，根据标识和消息构建返回结果对象
     */
    private static Result resultVO(Boolean flag, String message) {
        return Result.builder()
                .flag(flag)
                .code(flag ? SUCCESS.getCode() : FAIL.getCode())
                .message(message).build();
    }
}
