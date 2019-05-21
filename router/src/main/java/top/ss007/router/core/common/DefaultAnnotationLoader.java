package top.ss007.router.core.common;



import java.util.List;

import top.ss007.router.SRouter;
import top.ss007.router.core.UriHandler;

/**
 * 使用ServiceLoader加载注解配置
 *
 * Created by jzj on 2018/4/28.
 */
public class DefaultAnnotationLoader implements AnnotationLoader {

    public static final AnnotationLoader INSTANCE = new DefaultAnnotationLoader();

    @Override
    public <T extends UriHandler> void load(T handler, Class<? extends AnnotationInit<T>> initClass) {
        List<? extends AnnotationInit<T>> services = SRouter.getAllServices(initClass);
        for (AnnotationInit<T> service : services) {
            service.init(handler);
        }
    }
}
