package top.ss007.router;

import android.content.Context;

import java.util.List;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import top.ss007.router.core.NavCallback;
import top.ss007.router.core.RootUriHandler;
import top.ss007.router.core.UriRequest;
import top.ss007.router.services.IFactory;
import top.ss007.router.services.ServiceLoader;
import top.ss007.router.utils.SLogger;


/**
 * Copyright (C) 2019 shusheng007
 *
 * @author shusheng007
 * @version 1.0
 * @modifier
 * @createDate 2019/5/19 10:43
 * @description
 */
public class SRouter {
    private static final String TAG = "SRouter";

    private static RootUriHandler ROOT_HANDLER;

    @MainThread
    public static void init(@NonNull RootUriHandler rootUriHandler) {
        if (ROOT_HANDLER == null) {
            ROOT_HANDLER = rootUriHandler;
        } else {
            SLogger.error(TAG, "请勿重复初始化UriRouter");
        }
    }

    public static void lazyInit() {
        ServiceLoader.lazyInit();
    }

    private static RootUriHandler getRootHandler() {
        if (ROOT_HANDLER == null) {
            throw new NullPointerException("请先调用init");
        }
        return ROOT_HANDLER;
    }

    /**
     * 最全的导航
     *
     * @param request
     * @param isForResult 是否以startActivityForResult的方式启动Activity
     * @param callback    导航回调
     */
    public static void startNav(@NonNull UriRequest request, boolean isForResult, NavCallback callback) {
        getRootHandler().startRequest(request, isForResult, callback);
    }

    /**
     * startActivityForResult
     *
     * @param request
     */
    public static void startNavForResult(@NonNull UriRequest request) {
        getRootHandler().startRequest(request, true, null);
    }

    /**
     * startActivity
     *
     * @param request
     */
    public static void startNavNoResult(@NonNull UriRequest request) {
        getRootHandler().startRequest(request, false, null);
    }

    public static void startUri(@NonNull UriRequest request) {
        getRootHandler().startRequest(request, false, null);
    }


    //region ServiceLoader


    /**
     * 通过指定接口获得ServiceLoader
     *
     * @param interfaceClass 目标接口的class
     * @param <T>
     * @return
     */
    public static <T> ServiceLoader<T> loadService(Class<T> interfaceClass) {
        return ServiceLoader.load(interfaceClass);
    }

    /**
     * 获得指定接口的所有实现类
     *
     * @param clazz 服务接口
     * @param <I>
     * @param <T>
     * @return
     */
    public static <I, T extends I> List<T> getAllServices(Class<I> clazz) {
        return ServiceLoader.load(clazz).getAll();
    }


    /**
     * 获得指定接口的指定实现类
     *
     * @param clazz 服务接口
     * @param key   指定实现类对应的key
     * @param <I>
     * @param <T>
     * @return
     */
    public static <I, T extends I> T getService(Class<I> clazz, String key) {
        return ServiceLoader.load(clazz).get(key);
    }


    /**
     * 获得指定接口的指定实现类，这个实现类存在一个context为参数的构造函数
     *
     * @param clazz   服务接口
     * @param key     指定实现类对应的key
     * @param context
     * @param <I>
     * @param <T>
     * @return
     */
    public static <I, T extends I> T getService(Class<I> clazz, String key, Context context) {
        return ServiceLoader.load(clazz).get(key, context);
    }


    /**
     * 获得指定接口的指定实现类，可以使用factory使用此实现类的特定构造函数来构造实例
     *
     * @param clazz   服务接口
     * @param key     指定实现类对应的key
     * @param factory 用来使用实现类的指定构造函数来构造实例
     * @param <I>
     * @param <T>
     * @return
     */
    public static <I, T extends I> T getService(Class<I> clazz, String key, IFactory factory) {
        return ServiceLoader.load(clazz).get(key, factory);
    }


    public static <I, T extends I> Class<T> getServiceClass(Class<I> clazz, String key) {
        return ServiceLoader.load(clazz).getClass(key);
    }


    public static <I, T extends I> List<Class<T>> getAllServiceClasses(Class<I> clazz) {
        return ServiceLoader.load(clazz).getAllClasses();
    }
    //endregion
}
