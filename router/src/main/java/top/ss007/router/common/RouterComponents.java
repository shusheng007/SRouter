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
 * Created by jzj on 2018/4/28.
 */
public class RouterComponents {


    @NonNull
    private static IFactory sDefaultFactory = DefaultFactory.INSTANCE;


    public static void setDefaultFactory(IFactory factory) {
        sDefaultFactory = factory == null ? DefaultFactory.INSTANCE : factory;
    }

    @NonNull
    public static IFactory getDefaultFactory() {
        return sDefaultFactory;
    }

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
