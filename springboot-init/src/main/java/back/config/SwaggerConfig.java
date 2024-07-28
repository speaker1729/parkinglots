package back.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Swagger配置类
 */
@Configuration
@EnableOpenApi // 开启Swagger
public class SwaggerConfig extends WebMvcConfigurationSupport {
    /**
     * 配置Docket对象
     *
     * @return Docket对象
     */
    @Bean
    public Docket createRestAPI() {
        return new Docket(DocumentationType.OAS_30) // 创建Docket对象，并指定使用OpenAPI 3.0版本
                .apiInfo(apiInfo()) // 设置API文档信息
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.springboot.controller")) // 扫描API接口的包路径
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 配置API文档信息
     *
     * @return ApiInfo对象
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("接口文档") // 设置API文档标题
                .description("Restful 接口") // 设置API文档描述
                .contact(new Contact("xxx", "#", "xxx@qq.com")) // 设置API文档联系人
                .version("1.0") // 设置API文档版本
                .build();
    }
}
