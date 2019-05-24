package top.ss007.annotation.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 指定一个Uri跳转，此注解可以用在Activity上
 *
 * Created by jzj on 2018/3/19.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface RouterUri {

    /**
     * path
     */
    String[] path();

    /**
     * scheme
     */
    String scheme() default "";

    /**
     * host
     */
    String host() default "";

    /**
     * 是否允许外部跳转
     */
    boolean exported() default false;

    /**
     * 拦截器
     */
    Class[] interceptors() default {};
}
