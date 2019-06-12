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


    protected void setInterceptors(UriInterceptor... interceptors) {
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
        if (mDefaultScheme != null &&
                mDefaultHost != null &&
                mDefaultScheme.equals(request.getUri().getScheme()) &&
                mDefaultHost.equals(request.getUri().getHost())) {
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

    /**
     * 在执行跳转前，配置uriRequest
     *
     * @param request
     */
    protected abstract void buildUriRequest(@NonNull UriRequest request);

    /**
     * 处理注册了的URI,例如注册的URI格式为 srouter://host ，那么所有符合此格式的URI都在此处处理。在 {@link UriInterceptor} 之后调用。
     */
    protected abstract void handleInternal(@NonNull UriRequest request, NavCallback callback);

    /**
     * 处理没有注册的URI, 例如http://homeCredit 没有注册，那么就会在此处处理. 在 {@link UriInterceptor} 之后调用。
     */
    protected abstract void handleExternal(@NonNull UriRequest request, NavCallback callback);
}
