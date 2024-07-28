package back.config;

import back.config.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport {
    /**
     * 配置 Swagger UI 静态资源映射
     *
     * @param registry ResourceHandlerRegistry 实例
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/");
        super.addResourceHandlers(registry);
    }

    /**
     * 配置拦截器
     *
     * @param registry InterceptorRegistry 实例
     */
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor())
                // 添加需要拦截的请求路径，/** 表示全部请求
                .addPathPatterns("/**")
                // 添加不需要拦截的请求路径，多个路径之间用逗号分隔
                .excludePathPatterns("/user/login", "/user/register", "/**/export", "/**/import", "/file/**")
                // 添加不需要拦截的静态资源路径，多个路径之间用逗号分隔
                .excludePathPatterns("/swagger**/**", "/webjars/**", "/v3/**", "/doc.html");
        super.addInterceptors(registry);
    }

    /**
     * 配置 JWT 拦截器
     *
     * @return JwtInterceptor 实例
     */
    @Bean
    public JwtInterceptor jwtInterceptor() {
        return new JwtInterceptor();
    }
}
