package top.ss007.srouter;

import android.app.Application;

import top.ss007.demolib2.services.Lib2Controller;


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

        Lib2Controller.init();

        //        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... voids) {
//                SRouter.lazyInit();
//                return null;
//            }
//        }.execute();
    }
}
