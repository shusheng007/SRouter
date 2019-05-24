package top.ss007.router.common;

import android.content.Intent;

import androidx.annotation.NonNull;
import top.ss007.router.core.UriHandler;
import top.ss007.router.core.UriRequest;
import top.ss007.router.services.DefaultFactory;
import top.ss007.router.services.IFactory;

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


    public static int startActivity(@NonNull UriRequest request, @NonNull Intent intent) {
        return DefaultActivityLauncher.INSTANCE.startActivity(request, intent);
    }
}
