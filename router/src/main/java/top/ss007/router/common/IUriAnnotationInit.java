package top.ss007.router.common;


import top.ss007.annotation.annotations.RouterUri;
import top.ss007.router.uriHandlers.UriAnnotationHandler;

/**
 *
 * 每个配置了 {@link RouterUri} 注解的Application/Library模块
 * 都会生成一个此接口的实现类，并在 {@link UriAnnotationHandler} 初始化时被加载。
 *
 */
public interface IUriAnnotationInit extends AnnotationInit<UriAnnotationHandler> {

    @Override
    void init(UriAnnotationHandler handler);
}
