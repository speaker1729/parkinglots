package back.utils;

import back.entity.User;
import back.service.IUserService;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;

@Component
public class TokenUtils {
    private static IUserService staticUserService; // 静态的 UserService
    @Resource
    private IUserService userService; // 注入 UserService

    @PostConstruct // 标记一个方法，会在构造函数之后执行，用于进行一些初始化操作
    public void setUserService() {
        staticUserService = userService; // 将注入的 UserService 赋值给静态的 UserService
    }

    /**
     * 生成token
     *
     * @param userId 用户id
     * @param sign   token密钥
     * @return token字符串
     */
    public static String genToken(String userId, String sign) {
        // 将 user id 保存到 token 里面,作为载荷
        return JWT.create().withAudience(userId)
                // 2小时后token过期
                .withExpiresAt(DateUtil.offsetHour(new Date(), 2))
                // 以 password 作为 token 的密钥
                .sign(Algorithm.HMAC256(sign)); // 用HMAC256算法生成签名
    }

    /**
     * 获取当前登录的用户信息
     *
     * @return user对象，如果未登录则返回null
     */
    public static User getCurrentUser() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest(); // 获取请求对象
            String token = request.getHeader("token"); // 获取请求头中的token
            if (StrUtil.isNotBlank(token)) { // 如果token不为空
                String userId = JWT.decode(token).getAudience().get(0); // 从token中获取用户id
                return staticUserService.getById(Integer.valueOf(userId)); // 根据id获取用户对象
            }
        } catch (Exception e) { // 如果出现异常
            return null; // 返回null
        }
        return null; // 如果token为空，则返回null
    }
}
