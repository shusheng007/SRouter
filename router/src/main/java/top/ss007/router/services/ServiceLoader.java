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
 * <p>
 * Created by jzj on 2018/3/29.
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
                // 反射调用Init类，避免引用的类过多，导致main dex capacity exceeded问题
                // 这个类需要操作字节码动态生成，目前没有做
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
     * InitClass使用的初始化接口
     *
     * @param interfaceClass 接口类
     * @param implementClass 实现类
     */
    public static void put(Class interfaceClass, String key, Class implementClass, boolean singleton) {
        ServiceLoader loader = SERVICES.get(interfaceClass);
        if (loader == null) {
            loader = new ServiceLoader(interfaceClass);
            SERVICES.put(interfaceClass, loader);
        }
        loader.putImpl(key, implementClass, singleton);
    }

    /**
     * 根据接口获取 {@link ServiceLoader}
     */
    @SuppressWarnings("unchecked")
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
     * @param key
     * @param implementClass
     * @param singleton
     */
    private void putImpl(String key, Class implementClass, boolean singleton) {
        if (key != null && implementClass != null) {
            mMap.put(key, new ServiceImpl(key, implementClass, singleton));
        }
    }

    /**
     * 创建指定key的实现类实例，使用 {@link //RouterProvider} 方法或无参数构造。对于声明了singleton的实现类，不会重复创建实例。
     *
     * @return 可能返回null
     */
    public <T extends I> T get(String key) {
        return createInstance(mMap.get(key), null);
    }

    /**
     * 创建指定key的实现类实例，使用Context参数构造。对于声明了singleton的实现类，不会重复创建实例。
     *
     * @return 可能返回null
     */
    public <T extends I> T get(String key, Context context) {
        return createInstance(mMap.get(key), new ContextFactory(context));
    }

    /**
     * 创建指定key的实现类实例，使用指定的Factory构造。对于声明了singleton的实现类，不会重复创建实例。
     *
     * @return 可能返回null
     */
    public <T extends I> T get(String key, IFactory factory) {
        return createInstance(mMap.get(key), factory);
    }


    /**
     * 获取指定key的实现类。注意，对于声明了singleton的实现类，获取Class后还是可以创建新的实例。
     *
     * @return 可能返回null
     */
    @SuppressWarnings("unchecked")
    public <T extends I> Class<T> getClass(String key) {
        return (Class<T>) mMap.get(key).getImplementationClazz();
    }

    /**
     * 获取所有实现类的Class。注意，对于声明了singleton的实现类，获取Class后还是可以创建新的实例。
     *
     * @return 可能返回EmptyList，List中的元素不为空
     */
    @SuppressWarnings("unchecked")
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
               // Debugger.i("[ServiceLoader] create instance: %s, result = %s", clazz, t);
                return t;
            } catch (Exception e) {
               // Debugger.fatal(e);
            }
        }
        return null;
    }

    /**
     * 创建所有实现类的实例，使用指定Factory构造。对于声明了singleton的实现类，不会重复创建实例。
     *
     * @return 可能返回EmptyList，List中的元素不为空
     */
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

    /**
     * 创建所有实现类的实例，使用 {@link RouterProvider} 方法或无参数构造。对于声明了singleton的实现类，不会重复创建实例。
     *
     * @return 可能返回EmptyList，List中的元素不为空
     */
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
