package top.ss007.srouter;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import top.ss007.businessbase.BaseController;
import top.ss007.demolib2.services.Lib2Controller;
import top.ss007.router.SRouter;
import top.ss007.router.core.Debugger;
import top.ss007.router.core.RootUriHandler;
import top.ss007.router.generated.service.ServiceInit_1e6f6806b22052ace546ed1961c3c4fd;
import top.ss007.router.generated.service.ServiceInit_2d9918ed6c84e4ee31af19029ca6d0ce;
import top.ss007.router.generated.service.ServiceInit_Lib2ServiceImp;
import top.ss007.router.uriHandlers.UriAnnotationHandler;
import top.ss007.router.utils.DefaultLogger;


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

        //1
        ServiceInit_Lib2ServiceImp.init();
        ServiceInit_1e6f6806b22052ace546ed1961c3c4fd.init();

        //2
        ServiceInit_2d9918ed6c84e4ee31af19029ca6d0ce.init();

        //        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... voids) {
//                SRouter.lazyInit();
//                return null;
//            }
//        }.execute();
        initRouter(this);
    }

    private void initRouter(Context context) {
        // 自定义Logger
        DefaultLogger logger = new DefaultLogger() {
            @Override
            protected void handleError(Throwable t) {
                super.handleError(t);
                // 此处上报Fatal级别的异常
            }
        };

        // 设置Logger
        Debugger.setLogger(logger);

        // Log开关，建议测试环境下开启，方便排查问题。
        Debugger.setEnableLog(true);

        // 调试开关，建议测试环境下开启。调试模式下，严重问题直接抛异常，及时暴漏出来。
        Debugger.setEnableDebug(true);

        // 创建RootHandler
        RootUriHandler rootHandler = new UriAnnotationHandler(context,"srouter","host");


        // 初始化
        SRouter.init(rootHandler);

        // 懒加载后台初始化（可选）
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                SRouter.lazyInit();
                return null;
            }
        }.execute();
    }
}
