package top.ss007.compiler;

import com.sun.tools.javac.code.Symbol;

import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeMirror;

import top.ss007.annotation.annotations.RouterService;
import top.ss007.annotation.internal.SuffixConst;
import top.ss007.annotation.service.ServiceImpl;

/**
 * ServiceLoader 注解处理器
 */
//@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ServiceAnnotationProcessor extends BaseProcessor {

    /**
     * interfaceClass --> Entity
     */
    private final Map<String, Entity> mEntityMap = new HashMap<>();
    private String mHash = null;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        if (env.processingOver()) {
            generateInitClass();
        } else {
            processAnnotations(env);
        }
        return true;
    }

    private void processAnnotations(RoundEnvironment env) {
        for (Element element : env.getElementsAnnotatedWith(RouterService.class)) {
            if (!(element instanceof Symbol.ClassSymbol)) {
                continue;
            }

            Symbol.ClassSymbol cls = (Symbol.ClassSymbol) element;
            if (mHash == null) {
                mHash = hash(cls.className());
            }

            RouterService service = cls.getAnnotation(RouterService.class);
            if (service == null) {
                continue;
            }

            List<? extends TypeMirror> typeMirrors = getInterface(service);
            String[] keys = service.key();

            String implementationName = cls.className();
            boolean singleton = service.singleton();

            if (typeMirrors != null && !typeMirrors.isEmpty()) {
                for (TypeMirror mirror : typeMirrors) {
                    if (mirror == null) {
                        continue;
                    }
                    if (!isConcreteSubType(cls, mirror)) {
                        String msg = cls.className() + "没有实现注解:" + RouterService.class.getName()
                                + "标注的接口:" + mirror.toString();
                        throw new RuntimeException(msg);
                    }
                    String interfaceName = getClassName(mirror);

                    Entity entity = mEntityMap.get(interfaceName);
                    if (entity == null) {
                        entity = new Entity();
                        mEntityMap.put(interfaceName, entity);
                    }
                    for (String key : keys) {
                        if (key.contains(":")) {
                            String msg = String.format("%s: 注解%s的key参数不可包含冒号",
                                    implementationName, RouterService.class.getName());
                            throw new RuntimeException(msg);
                        }
                        entity.put(key, implementationName, singleton);
                    }
                }
            }
        }
    }

    //generate the init class
    private void generateInitClass() {
        if (mEntityMap.isEmpty() || mHash == null) {
            return;
        }

        ServiceInitClassBuilder generator = new ServiceInitClassBuilder("ServiceInit" + SuffixConst.SPLITTER + mHash);
        for (Map.Entry<String, Entity> entry : mEntityMap.entrySet()) {
            for (ServiceImpl service : entry.getValue().getMap().values()) {
                generator.put(entry.getKey(), service.getKey(), service.getImplementation(), service.isSingleton());
            }
        }
        generator.build();
    }

    //这个是不是有点扯
    private static List<? extends TypeMirror> getInterface(RouterService service) {
        try {
            service.interfaces();
        } catch (MirroredTypesException mte) {
            return mte.getTypeMirrors();
        }
        return null;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new HashSet<>(Collections.singletonList(RouterService.class.getName()));
    }

    public static class Entity {

        private final Map<String, ServiceImpl> mMap = new HashMap<>();

        public Map<String, ServiceImpl> getMap() {
            return mMap;
        }

        //将接口的实现放入holder中
        public void put(String key, String implementationName, boolean singleton) {
            if (key == null || implementationName == null) {
                throw new InvalidParameterException("the key and implementationName should not null");
            }

            ServiceImpl check = mMap.get(key);
            if (check != null) {
                throw new InvalidParameterException(String.format("the key:%s has been used", key));
            }
            ServiceImpl impl = new ServiceImpl(key, implementationName, singleton);
            mMap.put(impl.getKey(), impl);
        }
    }

}
