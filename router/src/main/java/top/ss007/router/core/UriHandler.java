package top.ss007.router.core;


import androidx.annotation.NonNull;

/**
 * Uri处理类
 */
public abstract class UriHandler {

    protected InterceptorHandler mInterceptor;

    public void getInterceptor(@NonNull UriInterceptor interceptor) {
        if (interceptor != null) {
            if (mInterceptor == null) {
                mInterceptor = new InterceptorHandler();
            }
            mInterceptor.addInterceptor(interceptor);
        }
    }

    public void getInterceptors(UriInterceptor... interceptors) {
        if (interceptors != null && interceptors.length > 0) {
            if (mInterceptor == null) {
                mInterceptor = new InterceptorHandler();
            }
            mInterceptor.clearInterceptors();
            for (UriInterceptor interceptor : interceptors) {
                mInterceptor.addInterceptor(interceptor);
            }
        }
    }


    /**
     * 处理URI
     *
     * @param request  Uri请求
     * @param callback 处理完成后的回调
     */
    public void handleUri(@NonNull final UriRequest request, @NonNull final UriCallback callback) {
        if (shouldHandle(request)) {
            Debugger.i("%s: handle request %s", this, request);
            if (mInterceptor != null) {
                mInterceptor.handleIntercept(request, new UriCallback() {
                    @Override
                    public void onNext() {
                        handleInternal(request, callback);
                    }

                    @Override
                    public void onComplete(int result) {
                        callback.onComplete(result);
                    }
                });
            } else {//没有拦截器直接执行跳转
                handleInternal(request, callback);
            }
        } else {
            Debugger.i("%s: ignore request %s", this, request);
            callback.onNext();
        }
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    /**
     * 是否要处理给定的URI。在 {@link UriInterceptor} 之前调用。
     */
    protected abstract boolean shouldHandle(@NonNull UriRequest request);

    /**
     * 处理URI。在 {@link UriInterceptor} 之后调用。
     */
    protected abstract void handleInternal(@NonNull UriRequest request, @NonNull UriCallback callback);
}
