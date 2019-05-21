package top.ss007.router.core.common;


import androidx.annotation.NonNull;
import top.ss007.router.core.UriCallback;
import top.ss007.router.core.UriInterceptor;
import top.ss007.router.core.UriRequest;
import top.ss007.router.core.UriResult;

/**
 * 节点的exported为false，不允许来自外部的跳转，拦截并返回 {@link UriResult#CODE_FORBIDDEN}
 *
 * Created by jzj on 2018/3/26.
 */

public class NotExportedInterceptor implements UriInterceptor {

    public static final NotExportedInterceptor INSTANCE = new NotExportedInterceptor();

    private NotExportedInterceptor() {
    }

    @Override
    public void intercept(@NonNull UriRequest request, @NonNull UriCallback callback) {
        if (false) {
            callback.onNext();
        } else {
            callback.onComplete(UriResult.CODE_FORBIDDEN);
        }
    }
}
