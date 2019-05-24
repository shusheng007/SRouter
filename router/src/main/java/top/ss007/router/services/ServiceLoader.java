package top.ss007.router.services;

import android.content.Context;

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
import top.ss007.router.SRouter;
import top.ss007.router.core.Debugger;
import top.ss007.router.utils.LazyInitHelper;
import top.ss007.router.utils.SingletonPool;

/**
 * 通过接口Class获取实现类
 *
 * @param <I> 接口类型
 */
public class ServiceLoader<I> {

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
                //这个类需要操作字节码动态生成，将processor生产的初始化类整合到一个类中
                Class.forName(SuffixConst.SERVICE_LOADER_INIT)
                        .getMethod(SuffixConst.INIT_METHOD)
                        .invoke(null);
                Debugger.i("[ServiceLoader] init class invoked");
            } catch (Exception e) {
                e.printStackTrace();
                //Debugger.fatal(e);
            }
        }
    };

    /**
     * @see LazyInitHelper#lazyInit()
     */
    public static void lazyInit() {
        sInitHelper.lazyInit();
    }


    /**
     * 很重要，生成的代码就是通过这个方法将目标类的信息保存到一个map里面的
     *
     * @param interfaceClass 服务接口
     * @param key            指定实现来在map中对应的key
     * @param implementClass 服务接口的实现类
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
            Debugger.fatal(new NullPointerException("ServiceLoader.load的class参数不应为空"));
            return EmptyServiceLoader.INSTANCE;
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
     */
    private HashMap<String, ServiceImpl> mMap = new HashMap<>();

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

    @SuppressWarnings("unchecked")
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
                //Debugger.fatal(e);
                e.printStackTrace();
            }
        } else {
            try {
                if (factory == null) {
                    factory = DefaultFactory.INSTANCE;
                }
                T t = factory.create(clazz);
                Debugger.i("[ServiceLoader] create instance: %s, result = %s", clazz, t);
                return t;
            } catch (Exception e) {
                Debugger.fatal(e);
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

    public static class EmptyServiceLoader extends ServiceLoader {

        public static final ServiceLoader INSTANCE = new EmptyServiceLoader();

        public EmptyServiceLoader() {
            super(null);
        }

        @NonNull
        @Override
        public List<Class> getAllClasses() {
            return Collections.emptyList();
        }

        @Override
        public String toString() {
            return "EmptyServiceLoader";
        }
    }
}
