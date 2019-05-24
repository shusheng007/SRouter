package top.ss007.router.core;


import androidx.annotation.NonNull;
import top.ss007.router.uriHandlers.UriResponse;


public interface UriInterceptor {
    /**
     * 拦截
     *
     * @param request
     * @param callback
     */
    void intercept(@NonNull UriRequest request, @NonNull InterceptorCallback callback);
}
