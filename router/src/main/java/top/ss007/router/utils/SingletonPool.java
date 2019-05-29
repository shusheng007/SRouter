package top.ss007.router.utils;


import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import top.ss007.router.services.DefaultFactory;
import top.ss007.router.services.IFactory;

/**
 * 单例缓存
 *
 */
public class SingletonPool {
    private static final String TAG="SingletonPool";

    private static final Map<Class, Object> CACHE = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <I, T extends I> T get(Class<I> clazz, IFactory factory) throws Exception {
        if (clazz == null) {
            return null;
        }
        if (factory == null) {
            factory = DefaultFactory.INSTANCE;
        }
        Object instance = getInstance(clazz, factory);
        SLogger.info(TAG,String.format("[SingletonPool]   get instance of class = %s, result = %s", clazz, instance));
        return (T) instance;
    }

    @NonNull
    private static Object getInstance(@NonNull Class clazz, @NonNull IFactory factory) throws Exception {
        Object t = CACHE.get(clazz);
        if (t != null) {
            return t;
        } else {
            synchronized (CACHE) {
                t = CACHE.get(clazz);
                if (t == null) {
                    t = factory.create(clazz);
                    //noinspection ConstantConditions
                    if (t != null) {
                        CACHE.put(clazz, t);
                    }
                }
            }
            return t;
        }
    }
}
