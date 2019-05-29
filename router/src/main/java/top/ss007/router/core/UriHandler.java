package top.ss007.router.core;


import androidx.annotation.NonNull;

/**
 * Uri处理类
 */
public abstract class UriHandler {

    //默认scheme
    protected final String mDefaultScheme;
    //默认host
    protected final String mDefaultHost;

    private InterceptorHandler mInterceptor;

    public UriHandler(String scheme, String host) {
        this.mDefaultScheme = scheme;
        this.mDefaultHost = host;
    }


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
     * @param request Uri请求
     */
    public void handleUri(@NonNull final UriRequest request, NavCallback callback) {
        if (mDefaultScheme.equals(request.getUri().getScheme()) && mDefaultHost.equals(request.getUri().getHost())) {
            buildUriRequest(request);
            if (mInterceptor != null) {
                mInterceptor.handleIntercept(request, new InterceptorCallback() {
                    @Override
                    public void onNext(UriRequest request) {
                        handleInternal(request, callback);
                    }

                    @Override
                    public void onInterrupt(Throwable exception) {
                        callback.onInterrupt(request);
                    }

                });
            } else {//没有拦截器直接执行跳转
                handleInternal(request, callback);
            }
        } else {
            handleExternal(request, callback);
        }
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    /**
     * 在执行跳转前，配置uriRequest
     *
     * @param request
     */
    protected abstract void buildUriRequest(@NonNull UriRequest request);

    /**
     * 处理符合约定的scheme://host 的URI。在 {@link UriInterceptor} 之后调用。
     */
    protected abstract void handleInternal(@NonNull UriRequest request, NavCallback callback);

    /**
     * 处理不符合合约定的scheme://host 的URI，例如http://homecredit。
     */
    protected abstract void handleExternal(@NonNull UriRequest request, NavCallback callback);
}
