package top.ss007.router.services;

import android.content.Context;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import top.ss007.annotation.internal.SuffixConst;
import top.ss007.annotation.service.ServiceImpl;
import top.ss007.router.utils.LazyInitHelper;
import top.ss007.router.utils.SLogger;
import top.ss007.router.utils.SingletonPool;

/**
 * 服务处理器，可通过此类获取到服务
 *
 * @param <I> 接口类型
 */
public class ServiceLoader<I> {

    private static final String TAG = "ServiceLoader";

    //每一个接口对应map中的一条
    //key: 服务接口的Class  value: serviceLoader
    private static final Map<Class, ServiceLoader> SERVICES = new HashMap<>();

    private final String mInterfaceName;

    private ServiceLoader(Class interfaceClass) {
        if (interfaceClass == null) {
            mInterfaceName = "";
        } else {
            mInterfaceName = interfaceClass.getName();
        }
    }

    private static final LazyInitHelper sInitHelper = new LazyInitHelper("ServiceLoader") {
        @Override
        protected void doInit() {
            try {
                //这个类SuffixConst.SERVICE_LOADER_INIT 是在gradle打包dex前生成的，其将所有的生成service类中的初始化代码整合到init()方法中。
                Class.forName(SuffixConst.SERVICE_LOADER_INIT)
                        .getMethod(SuffixConst.INIT_METHOD)
                        .invoke(null);
                SLogger.info(TAG, "[ServiceLoader] init class invoked");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    public static void lazyInit() {
        sInitHelper.lazyInit();
    }


    /**
     * 很重要，编译生成的代码通过这个方法将目标类的信息保存到一个map里
     *
     * @param interfaceClass 服务接口Class
     * @param key            指定实现在map中对应的key
     * @param implementClass 服务接口的实现类Class
     * @param singleton      是否单例
     */
    public static void put(Class interfaceClass, String key, Class implementClass, boolean singleton) {
        ServiceLoader loader = SERVICES.get(interfaceClass);
        if (loader == null) {
            loader = new ServiceLoader(interfaceClass);
            SERVICES.put(interfaceClass, loader);
        }
        loader.putImpl(key, implementClass, singleton);
    }


    public static <T> ServiceLoader<T> load(Class<T> interfaceClass) {
        sInitHelper.ensureInit();
        if (interfaceClass == null) {
            SLogger.error("ServiceLoader", "ServiceLoader.load的class参数不应为空");
            throw new InvalidParameterException("parameter interfaceClass can not be null");
        }
        ServiceLoader service = SERVICES.get(interfaceClass);
        if (service == null) {
            synchronized (SERVICES) {
                service = SERVICES.get(interfaceClass);
                if (service == null) {
                    service = new ServiceLoader(interfaceClass);
                    SERVICES.put(interfaceClass, service);
                }
            }
        }
        return service;
    }


    /**
     * key --> class name
     * 每个serviceLoader 里面包含一个map，里面保存了此接口的各个实现类
     */
    private Map<String, ServiceImpl> mMap = new HashMap<>();

    /**
     * 保存接口的实现类型
     *
     * @param key
     * @param implementClass
     * @param singleton
     */
    private void putImpl(String key, Class implementClass, boolean singleton) {
        if (key != null && implementClass != null) {
            mMap.put(key, new ServiceImpl(key, implementClass, singleton));
        }
    }


    public <T extends I> T get(String key) {
        return createInstance(mMap.get(key), null);
    }


    public <T extends I> T get(String key, Context context) {
        return createInstance(mMap.get(key), new ContextFactory(context));
    }


    public <T extends I> T get(String key, IFactory factory) {
        return createInstance(mMap.get(key), factory);
    }


    public <T extends I> Class<T> getClass(String key) {
        return (Class<T>) mMap.get(key).getImplementationClazz();
    }


    @NonNull
    public <T extends I> List<Class<T>> getAllClasses() {
        List<Class<T>> list = new ArrayList<>(mMap.size());
        for (ServiceImpl impl : mMap.values()) {
            Class<T> clazz = (Class<T>) impl.getImplementationClazz();
            if (clazz != null) {
                list.add(clazz);
            }
        }
        return list;
    }

    @Nullable
    private <T extends I> T createInstance(@Nullable ServiceImpl impl, @Nullable IFactory factory) {
        if (impl == null) {
            return null;
        }
        Class<T> clazz = (Class<T>) impl.getImplementationClazz();
        if (impl.isSingleton()) {
            try {
                return SingletonPool.get(clazz, factory);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                if (factory == null) {
                    factory = DefaultFactory.INSTANCE;
                }
                T t = factory.create(clazz);
                SLogger.info(TAG, String.format("[ServiceLoader] create instance: %s, result = %s", clazz, t));
                return t;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @NonNull
    public <T extends I> List<T> getAll(IFactory factory) {
        Collection<ServiceImpl> services = mMap.values();
        if (services.isEmpty()) {
            return Collections.emptyList();
        }
        List<T> list = new ArrayList<>(services.size());
        for (ServiceImpl impl : services) {
            T instance = createInstance(impl, factory);
            if (instance != null) {
                list.add(instance);
            }
        }
        return list;
    }

    @NonNull
    public <T extends I> List<T> getAll() {
        return getAll((IFactory) null);
    }

    @Override
    public String toString() {
        return "ServiceLoader (" + mInterfaceName + ")";
    }
}
