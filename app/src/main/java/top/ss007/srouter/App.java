package top.ss007.srouter;

import android.app.Application;
import android.content.Context;

import top.ss007.businessbase.RouteTable;
import top.ss007.router.SRouter;
import top.ss007.router.core.RootUriHandler;
import top.ss007.router.uriHandlers.UriAnnotationHandler;
import top.ss007.router.utils.SLogger;


/**
 * Copyright (C) 2019 shusheng007
 * 完全享有此软件的著作权，违者必究
 *
 * @author shusheng007
 * @version 1.0
 * @modifier
 * @createDate 2019/5/19 14:20
 * @description
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initRouter(this);
    }

    private void initRouter(Context context) {

        SLogger.showLog(true);

        // 创建RootHandler
        RootUriHandler rootHandler = new UriAnnotationHandler(RouteTable.SCHEME, RouteTable.HOST);
        // 初始化
        SRouter.init(rootHandler);
    }
}
