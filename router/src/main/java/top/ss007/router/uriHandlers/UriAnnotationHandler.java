package top.ss007.router.uriHandlers;


import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import top.ss007.router.activity.DefaultPageLauncher;
import top.ss007.router.common.DefaultAnnotationLoader;
import top.ss007.router.common.IUriAnnotationInit;
import top.ss007.router.core.NavCallback;
import top.ss007.router.core.RootUriHandler;
import top.ss007.router.core.UriInterceptor;
import top.ss007.router.core.UriRequest;
import top.ss007.router.utils.LazyInitHelper;
import top.ss007.router.utils.RouterUtils;

/**
 * 处理通过注解配置的页面跳转
 */
public class UriAnnotationHandler extends RootUriHandler {

    //包含了跳转目的地信息  key：scheme://host/path; value ：UriResponse
    private final Map<String, UriResponse> mMap = new HashMap<>();
    private final LazyInitHelper mInitHelper = new LazyInitHelper("UriAnnotationHandler") {
        @Override
        protected void doInit() {
            initAnnotationConfig();
        }
    };

    public UriAnnotationHandler(@Nullable String defaultScheme, @Nullable String defaultHost) {
        super(RouterUtils.toNonNullString(defaultScheme), RouterUtils.toNonNullString(defaultHost));
    }

    private void initAnnotationConfig() {
        DefaultAnnotationLoader.INSTANCE.load(this, IUriAnnotationInit.class);
    }

    public void lazyInit() {
        mInitHelper.lazyInit();
    }

    /**
     * 很重要，通过此方法将要跳转的Activity的信息和数据保存起来
     *
     * @param scheme
     * @param host
     * @param path
     * @param handler
     * @param interceptors
     */
    public void register(String scheme, String host, String path,
                         Class handler, UriInterceptor... interceptors) {
        if (TextUtils.isEmpty(scheme)) {
            scheme = mDefaultScheme;
        }
        if (TextUtils.isEmpty(host)) {
            host = mDefaultHost;
        }
        String schemeHost = RouterUtils.schemeHostPath(scheme, host, path);
        UriResponse uriResponse = mMap.get(schemeHost);
        if (uriResponse == null) {
            uriResponse = new UriResponse(scheme, host, path, handler, interceptors);
            mMap.put(schemeHost, uriResponse);
        }
    }

    @Override
    protected void handleInternal(@NonNull UriRequest request, NavCallback callback) {
        if (mIPageLauncher==null) {
            mIPageLauncher=new DefaultPageLauncher();
        }
        mIPageLauncher.legalUriNav(request,callback);
    }


    @Override
    protected void buildUriRequest(@NonNull UriRequest request) {
        mInitHelper.ensureInit();
        UriResponse uriResponse = mMap.get(RouterUtils.schemeHostPath(request.getUri()));
        if (uriResponse == null)
            throw new UnsupportedOperationException(String.format("Uri:%s 没有注册",RouterUtils.schemeHostPath(request.getUri())));
        request.setUriResponse(uriResponse);
        setInterceptors(uriResponse.getUriInterceptors());
    }

    @Override
    public String toString() {
        return "UriAnnotationHandler";
    }
}
