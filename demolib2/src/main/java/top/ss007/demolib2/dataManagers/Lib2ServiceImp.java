package top.ss007.demolib2.dataManagers;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Executors;

import top.ss007.annotation.annotations.RouterService;
import top.ss007.businessbase.services.IRequestCallback;
import top.ss007.businessbase.services.Lib2Service;
import top.ss007.businessbase.services.ServiceKeys;


/**
 * Copyright (C) 2019 shusheng007
 * 完全享有此软件的著作权，违者必究
 *
 * @author shusheng007
 * @version 1.0
 * @modifier
 * @createDate 2019/5/19 11:56
 * @description
 */
@RouterService(interfaces = Lib2Service.class,key = ServiceKeys.KEY_LIB2_SERVICE)
public class Lib2ServiceImp implements Lib2Service {

    private final String address;

    public Lib2ServiceImp(String address) {
        this.address = address;
    }

    @Override
    public String syncGetLib2Name() {
        return String.format("I come from lib2 : %s",address);
    }

    @Override
    public void asyncGetInfo(int id, IRequestCallback callback) {
        //模拟耗时操作，可以使用rxjava2
        Executors.newSingleThreadExecutor().execute(()->{
            try {
                Thread.sleep(3_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            new Handler(Looper.getMainLooper()).post(() -> {
                JSONObject obj=new JSONObject();
                try {
                    obj.put("name","shusheng007");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callback.onSuccess(obj.toString());
            });
        });
    }

}
