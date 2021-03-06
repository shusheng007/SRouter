package top.ss007.router.common;


import top.ss007.router.core.UriHandler;

/**
 * 用于加载注解配置
 *
 */
public interface AnnotationLoader {

    <T extends UriHandler> void load(T handler, Class<? extends AnnotationInit<T>> initClass);
}
