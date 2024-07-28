package back.config.interceptor;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
/*
  权限访问注解
 */
public @interface AuthAccess {

}
