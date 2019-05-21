package top.ss007.router.core.common;


import top.ss007.router.core.UriHandler;

/**
 * Created by jzj on 2018/4/28.
 */

public interface AnnotationInit<T extends UriHandler> {

    void init(T handler);
}
