package top.ss007.router;

import android.content.Context;
import android.os.Looper;

import java.util.List;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import top.ss007.router.core.Debugger;
import top.ss007.router.core.NavCallback;
import top.ss007.router.core.RootUriHandler;
import top.ss007.router.core.UriRequest;
import top.ss007.router.services.IFactory;
import top.ss007.router.services.ServiceLoader;


/**
 * Copyright (C) 2019 shusheng007
 * 完全享有此软件的著作权，违者必究
 *
 * @author shusheng007
 * @version 1.0
 * @modifier
 * @createDate 2019/5/19 10:43
 * @description
 */
public class SRouter {

    private static RootUriHandler ROOT_HANDLER;


    @MainThread
    public static void init(@NonNull RootUriHandler rootUriHandler) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            Debugger.fatal("初始化方法init应该在主线程调用");
        }
        if (ROOT_HANDLER == null) {
            ROOT_HANDLER = rootUriHandler;
        } else {
            Debugger.fatal("请勿重复初始化UriRouter");
        }
    }
    /**
     * 此初始化方法的调用不是必须的。
     * 使用时会按需初始化；但也可以提前调用并初始化，使用时会等待初始化完成。
     * 本方法线程安全。
     */
    public static void lazyInit() {
        ServiceLoader.lazyInit();
    }

    public static RootUriHandler getRootHandler() {
        if (ROOT_HANDLER == null) {
            throw new RuntimeException("请先调用init");
        }
        return ROOT_HANDLER;
    }


    public static void startNav(@NonNull UriRequest request, boolean isForResult, NavCallback callback) {
        getRootHandler().startNav(request, false,callback);
    }
    public static void startNavNoCallback(@NonNull UriRequest request,boolean isForResult) {
        getRootHandler().startNavNoCallback(request, isForResult);
    }
    public static void startNavNoResult(@NonNull UriRequest request) {
        getRootHandler().startNavNoResult(request);
    }
    public static void startNavForResult(@NonNull UriRequest request) {
        getRootHandler().startNavForResult(request);
    }


    /**
     * 根据接口获取 {@link ServiceLoader}
     */
    public static <T> ServiceLoader<T> loadService(Class<T> clazz) {
        return ServiceLoader.load(clazz);
    }

    /**
     * 获得指定接口的所有实现类
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
     * @param clazz 服务接口
     * @param key   指定实现类对应的key
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
     * @param clazz 服务接口
     * @param key   指定实现类对应的key
     * @param factory  用来使用实现类的指定构造函数来构造实例
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
}
