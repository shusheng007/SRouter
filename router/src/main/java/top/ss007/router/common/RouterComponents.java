package top.ss007.router.common;

import top.ss007.router.core.UriHandler;

/**
 * 用于配置组件
 *
 */
public class RouterComponents {

    /**
     * @see AnnotationLoader#load(UriHandler, Class)
     */
    public static <T extends UriHandler> void loadAnnotation(T handler, Class<? extends AnnotationInit<T>> initClass) {
        DefaultAnnotationLoader.INSTANCE.load(handler, initClass);
    }

}
