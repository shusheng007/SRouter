package top.ss007.router;

import android.content.Context;

import java.util.List;

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


    /**
     * 此初始化方法的调用不是必须的。
     * 使用时会按需初始化；但也可以提前调用并初始化，使用时会等待初始化完成。
     * 本方法线程安全。
     */
    public static void lazyInit() {
        ServiceLoader.lazyInit();
        //getRootHandler().lazyInit();
    }
    /**
     * 根据接口获取 {@link ServiceLoader}
     */
    public static <T> ServiceLoader<T> loadService(Class<T> clazz) {
        return ServiceLoader.load(clazz);
    }

    /**
     * 创建指定key的实现类实例，使用 {@link //RouterProvider} 方法或无参数构造。对于声明了singleton的实现类，不会重复创建实例。
     *
     * @return 找不到或获取、构造失败，则返回null
     */
    public static <I, T extends I> T getService(Class<I> clazz, String key) {
        return ServiceLoader.load(clazz).get(key);
    }

    /**
     * 创建指定key的实现类实例，使用Context参数构造。对于声明了singleton的实现类，不会重复创建实例。
     *
     * @return 找不到或获取、构造失败，则返回null
     */
    public static <I, T extends I> T getService(Class<I> clazz, String key, Context context) {
        return ServiceLoader.load(clazz).get(key, context);
    }

    /**
     * 创建指定key的实现类实例，使用指定的Factory构造。对于声明了singleton的实现类，不会重复创建实例。
     *
     * @param factory 用于从Class构造实例
     * @return 找不到或获取、构造失败，则返回null
     */
    public static <I, T extends I> T getService(Class<I> clazz, String key, IFactory factory) {
        return ServiceLoader.load(clazz).get(key, factory);
    }

    /**
     * 创建所有实现类的实例，使用 {@link //RouterProvider} 方法或无参数构造。对于声明了singleton的实现类，不会重复创建实例。
     *
     * @return 可能返回EmptyList，List中的元素不为空
     */
    public static <I, T extends I> List<T> getAllServices(Class<I> clazz) {
        return ServiceLoader.load(clazz).getAll();
    }

    /**
     * 创建所有实现类的实例，使用Context参数构造。对于声明了singleton的实现类，不会重复创建实例。
     *
     * @return 可能返回EmptyList，List中的元素不为空
     */
    public static <I, T extends I> List<T> getAllServices(Class<I> clazz, Context context) {
        return ServiceLoader.load(clazz).getAll(context);
    }

    /**
     * 创建所有实现类的实例，使用指定Factory构造。对于声明了singleton的实现类，不会重复创建实例。
     *
     * @return 可能返回EmptyList，List中的元素不为空
     */
    public static <I, T extends I> List<T> getAllServices(Class<I> clazz, IFactory factory) {
        return ServiceLoader.load(clazz).getAll(factory);
    }

    /**
     * 根据key获取实现类的Class。注意，对于声明了singleton的实现类，获取Class后还是可以创建新的实例。
     *
     * @return 找不到或获取失败，则返回null
     */
    public static <I, T extends I> Class<T> getServiceClass(Class<I> clazz, String key) {
        return ServiceLoader.load(clazz).getClass(key);
    }

    /**
     * 获取所有实现类的Class。注意，对于声明了singleton的实现类，获取Class后还是可以创建新的实例。
     *
     * @return 可能返回EmptyList，List中的元素不为空
     */
    public static <I, T extends I> List<Class<T>> getAllServiceClasses(Class<I> clazz) {
        return ServiceLoader.load(clazz).getAllClasses();
    }
}
