package back.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MybatisPlus配置类
 */
@Configuration
@MapperScan("back.mapper") // 扫描Mapper接口所在包路径
public class MybatisPlusConfig {
    /**
     * 配置MybatisPlus分页插件
     *
     * @return MybatisPlusInterceptor对象
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor(); // 创建MybatisPlusInterceptor对象
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL)); // 添加MybatisPlus的分页插件
        return interceptor; // 返回MybatisPlusInterceptor对象
    }
}
