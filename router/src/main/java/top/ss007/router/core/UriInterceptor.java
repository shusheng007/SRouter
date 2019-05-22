package top.ss007.router.core;


import androidx.annotation.NonNull;

/**
 * 拦截URI跳转并做处理，支持异步操作。
 *
 * Created by jzj on 2017/4/11.
 */
public interface UriInterceptor {
    /**
     * 拦截
     * @param request
     * @param callback
     */
    void intercept(@NonNull UriRequest request, @NonNull UriCallback callback);
}
