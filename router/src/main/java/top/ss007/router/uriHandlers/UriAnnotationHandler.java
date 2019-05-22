package top.ss007.router.uriHandlers;


import android.content.Context;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import top.ss007.annotation.RouterUri;
import top.ss007.router.activity.AbsActivity;
import top.ss007.router.core.RootUriHandler;
import top.ss007.router.core.UriCallback;
import top.ss007.router.core.UriInterceptor;
import top.ss007.router.core.UriRequest;
import top.ss007.router.common.IUriAnnotationInit;
import top.ss007.router.common.RouterComponents;
import top.ss007.router.utils.LazyInitHelper;
import top.ss007.router.utils.RouterUtils;

/**
 * 处理通过注解配置的页面跳转
 */
public class UriAnnotationHandler extends RootUriHandler {

    /**
     * 包含了跳转目的地信息
     *
     * key：scheme://host/path; value ：PathEntity
     *
     */
    private final Map<String, PathEntity> mMap = new HashMap<>();
    /**
     * {@link RouterUri} 默认scheme
     */
    private final String mDefaultScheme;
    /**
     * {@link RouterUri} 默认host
     */
    private final String mDefaultHost;

    private final LazyInitHelper mInitHelper = new LazyInitHelper("UriAnnotationHandler") {
        @Override
        protected void doInit() {
            initAnnotationConfig();
        }
    };

    public UriAnnotationHandler(Context context, @Nullable String defaultScheme, @Nullable String defaultHost) {
        super(context);
        mDefaultScheme = RouterUtils.toNonNullString(defaultScheme);
        mDefaultHost = RouterUtils.toNonNullString(defaultHost);
    }

    public void lazyInit() {
        mInitHelper.lazyInit();
    }

    protected void initAnnotationConfig() {
        RouterComponents.loadAnnotation(this, IUriAnnotationInit.class);
    }


    /**
     * 很重要，通过此方法将要跳转的Activity的信息和数据保存起来
     * @param scheme
     * @param host
     * @param path
     * @param handler
     * @param exported
     * @param interceptors
     */
    public void register(String scheme, String host, String path,
                         Object handler, boolean exported, UriInterceptor... interceptors) {
        if (TextUtils.isEmpty(scheme)) {
            scheme = mDefaultScheme;
        }
        if (TextUtils.isEmpty(host)) {
            host = mDefaultHost;
        }
        String schemeHost = RouterUtils.schemeHostPath(scheme, host,path);
        PathEntity pathEntity = mMap.get(schemeHost);
        if (pathEntity == null) {
            pathEntity = new PathEntity(scheme,host,path,handler,exported,interceptors);
            mMap.put(schemeHost, pathEntity);
        }
    }


    @Override
    public void handleUri(@NonNull UriRequest request, @NonNull UriCallback callback) {
        mInitHelper.ensureInit();
        PathEntity pathEntity = mMap.get(RouterUtils.schemeHostPath(request.getUri()));
        getInterceptors(pathEntity.getUriInterceptors());
        super.handleUri(request, callback);
    }

    @Override
    protected boolean shouldHandle(@NonNull UriRequest request) {
        return true;
    }

    @Override
    protected void handleInternal(@NonNull UriRequest request, @NonNull UriCallback callback) {
        PathEntity pathEntity = mMap.get(RouterUtils.schemeHostPath(request.getUri()));
        if (pathEntity != null) {
            new AbsActivity().navigation(request,pathEntity,callback);
        } else {
            // 没找到的继续分发
            callback.onNext();
        }
    }

    @Override
    public String toString() {
        return "UriAnnotationHandler";
    }
}
