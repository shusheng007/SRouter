package top.ss007.router.core;


import androidx.annotation.NonNull;
import top.ss007.router.uriHandlers.UriResponse;

/**
 * Uri处理类
 */
public abstract class UriHandler {

    protected InterceptorHandler mInterceptor;

    public void getInterceptors(UriInterceptor... interceptors) {
        if (mInterceptor == null) {
            mInterceptor = new InterceptorHandler();
        }
        mInterceptor.clearInterceptors();
        if (interceptors != null) {
            for (UriInterceptor interceptor : interceptors) {
                mInterceptor.addInterceptor(interceptor);
            }
        }
    }


    /**
     * 处理URI
     *
     * @param request  Uri请求
     */
    public void handleUri(@NonNull final UriRequest request,NavCallback callback) {
        if (shouldHandle(request,callback)){
            if (mInterceptor != null) {
                mInterceptor.handleIntercept(request, new InterceptorCallback() {
                    @Override
                    public void onNext(UriRequest request) {
                        handleInternal(request,callback);
                    }

                    @Override
                    public void onInterrupt(Throwable exception) {
                        callback.onInterrupt(request);
                    }

                });
            } else {//没有拦截器直接执行跳转
                handleInternal(request,callback);
            }

        }else {
            callback.onInterrupt(request);
        }
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    /**
     * 是否要处理给定的URI。在 {@link UriInterceptor} 之前调用。
     */
    protected abstract boolean shouldHandle(@NonNull UriRequest request,NavCallback callback);

    /**
     * 处理URI。在 {@link UriInterceptor} 之后调用。
     */
    protected abstract void handleInternal(@NonNull UriRequest request,NavCallback callback);
}
