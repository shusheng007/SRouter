package top.ss007.annotation.service;

import java.security.InvalidParameterException;

/**
 * Service的holder
 */
public class ServiceImpl {

    private final String key;//此实现类对应的key
    private final String implementation;//实现类的全类名
    private final Class implementationClazz; //实现类的Class对象
    private final boolean singleton;

    public ServiceImpl(String key, Class implementation, boolean singleton) {
        if (key == null || implementation == null) {
            throw new InvalidParameterException("key和implementation不应该为空");
        }
        this.key = key;
        this.implementation = "";
        this.implementationClazz = implementation;
        this.singleton = singleton;
    }

    public ServiceImpl(String key, String implementation, boolean singleton) {
        if (isEmpty(implementation)) {
            throw new InvalidParameterException("implementation不应该为空");
        }
        this.key = isEmpty(key) ? implementation : key; // 没有指定key，则为implementation
        this.implementation = implementation;
        this.implementationClazz = null;
        this.singleton = singleton;
    }


    public String getKey() {
        return key;
    }

    public String getImplementation() {
        return implementation;
    }

    public Class getImplementationClazz() {
        return implementationClazz;
    }

    public boolean isSingleton() {
        return singleton;
    }

    private static boolean isEmpty(String key) {
        return key == null || key.length() == 0;
    }

    @Override
    public String toString() {
        return "ServiceImpl{" +
                "key='" + key + '\'' +
                ", implementation='" + implementation + '\'' +
                ", implementationClazz=" + implementationClazz +
                ", singleton=" + singleton +
                '}';
    }
}
