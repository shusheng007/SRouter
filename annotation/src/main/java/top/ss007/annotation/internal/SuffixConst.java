package top.ss007.annotation.internal;

/**
 * Copyright (C) 2019 shusheng007
 * 完全享有此软件的著作权，违者必究
 *
 * @author shusheng007
 * @version 1.0
 * @modifier
 * @createDate 2019/5/19 10:49
 * @description
 */
public class SuffixConst {
    private SuffixConst(){}

    public static final String NAME = "SRouter";
    public static final String PKG = "top.ss007.router.";

    // 生成的代码
    public static final String GEN_PKG = PKG + "generated";
    public static final String GEN_PKG_SERVICE = GEN_PKG + ".service";

    public static final String SPLITTER = "_";

    /**
     * ServiceLoader初始化
     */
    public static final String SERVICE_LOADER_INIT = GEN_PKG + ".ServiceLoaderInit";

    public static final char DOT = '.';

    public static final String INIT_METHOD = "init";

    public static final String URI_HANDLER_CLASS =
            PKG + "core.UriHandler";
    public static final String URI_INTERCEPTOR_CLASS =
            PKG + "core.UriInterceptor";
    public static final String SERVICE_LOADER_CLASS =
            PKG + "services.ServiceLoader";


    // Library中的类名
    public static final String URI_ANNOTATION_HANDLER_CLASS =
            PKG + "uriHandlers.UriAnnotationHandler";
    public static final String URI_ANNOTATION_INIT_CLASS =
            PKG + "common.IUriAnnotationInit";


    // Android中的类名
    public static final String ACTIVITY_CLASS = "android.app.Activity";
    public static final String FRAGMENT_CLASS = "android.app.Fragment";
    public static final String FRAGMENT_V4_CLASS = "android.support.v4.app.Fragment";
    public static final String FRAGMENT_ANDROIDX_CLASS = "androidx.fragment.app.Fragment";
}
