package back.config.interceptor;

import back.common.Constants;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import back.entity.User;
import back.exception.ServiceException;
import back.service.IUserService;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT拦截器
 */
public class JwtInterceptor implements HandlerInterceptor {
    @Resource
    private IUserService userService;

    /**
     * 拦截请求，进行JWT认证
     *
     * @param request  请求
     * @param response 响应
     * @param handler  处理器
     * @return 是否通过拦截器
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 从请求头中获取token
        String token = request.getHeader("token");
        if (StrUtil.isBlank(token)) {
            // 如果请求头中没有token，则从请求参数中获取
            token = request.getParameter("token");
        }
        // 如果不是映射到方法直接通过
        if (handler instanceof HandlerMethod) {
            // 获取方法上的AuthAccess注解
            AuthAccess annotation = ((HandlerMethod) handler).getMethodAnnotation(AuthAccess.class);
            if (annotation != null) {
                // 如果方法上的AuthAccess注解不为空，则说明该方法需要进行权限访问控制，直接通过拦截器
                return true;
            }
        }
        // 执行认证
        if (StrUtil.isBlank(token)) {
            // 如果token为空，则抛出异常
            throw new ServiceException(Constants.CODE_401, "无token，请重新登录");
        }
        // 获取 token 中的 user id
        String userId;
        try {
            // 解码token获取用户id
            userId = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException j) {
            // 如果解码失败，则抛出异常
            throw new ServiceException(Constants.CODE_401, "token验证失败，请重新登录");
        }
        // 根据用户id查询数据库中的用户信息
        User user = userService.getById(userId);
        if (user == null) {
            // 如果用户不存在，则抛出异常
            throw new ServiceException(Constants.CODE_401, "用户不存在，请重新登录");
        }
        // 用户密码加签验证 token
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        try {
            // 验证token
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            // 如果验证失败，则抛出异常
            throw new ServiceException(Constants.CODE_401, "token验证失败，请重新登录");
        }
        // 如果验证通过，则返回true，表示通过JWT认证
        return true;
    }
}
