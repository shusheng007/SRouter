package top.ss007.annotation.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface RouterService {

    //服务接口
    Class[] interfaces();

    //当服务接口存在多个实现类，通过此可以获取对应的实现
     String[] key() default {};

    //服务实现是否为单例
    boolean singleton() default false;
}
