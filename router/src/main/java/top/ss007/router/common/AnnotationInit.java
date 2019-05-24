package top.ss007.router.common;


import top.ss007.router.core.UriHandler;

/**
 * 注解初始化接口
 */
public interface AnnotationInit<T extends UriHandler> {
    void init(T handler);
}
