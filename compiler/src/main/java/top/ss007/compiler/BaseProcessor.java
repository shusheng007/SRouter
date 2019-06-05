package top.ss007.compiler;


import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import top.ss007.annotation.internal.SuffixConst;

public abstract class BaseProcessor extends AbstractProcessor {

    protected Filer filer;
    protected Types types;
    protected Elements elements;
    protected Messager mMessager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
        types = processingEnvironment.getTypeUtils();
        elements = processingEnvironment.getElementUtils();
        mMessager = processingEnvironment.getMessager();
    }


    public TypeElement typeElement(String className) {
        return elements.getTypeElement(className);
    }


    public TypeMirror typeMirror(String className) {
        return typeElement(className).asType();
    }


    public ClassName className(String className) {
        return ClassName.get(typeElement(className));
    }


    public static String getClassName(TypeMirror typeMirror) {
        return typeMirror == null ? "" : typeMirror.toString();
    }

    public boolean isSubType(TypeMirror type, String className) {
        return type != null && types.isSubtype(type, typeMirror(className));
    }

    public boolean isSubType(Element element, String className) {
        return element != null && isSubType(element.asType(), className);
    }

    public boolean isSubType(Element element, TypeMirror typeMirror) {
        return element != null && types.isSubtype(element.asType(), typeMirror);
    }


    public boolean isConcreteType(Element element) {
        return element instanceof TypeElement && !element.getModifiers().contains(
                Modifier.ABSTRACT);
    }


    public boolean isConcreteSubType(Element element, String className) {
        return isConcreteType(element) && isSubType(element, className);
    }

    public boolean isConcreteSubType(Element element, TypeMirror typeMirror) {
        return isConcreteType(element) && isSubType(element, typeMirror);
    }

    public boolean isActivity(Element element) {
        return isConcreteSubType(element, SuffixConst.ACTIVITY_CLASS);
    }

    public boolean isHandler(Element element) {
        return isConcreteSubType(element, SuffixConst.URI_HANDLER_CLASS);
    }

    public boolean isInterceptor(Element element) {
        return isConcreteSubType(element, SuffixConst.URI_INTERCEPTOR_CLASS);
    }

    public static String randomHash() {
        return hash(UUID.randomUUID().toString());
    }

    public static String hash(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            return new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            return Integer.toHexString(str.hashCode());
        }
    }


    /**
     * 创建Interceptors。格式：<code>, new Interceptor1(), new Interceptor2()</code>
     */
    public CodeBlock buildInterceptors(List<? extends TypeMirror> interceptors) {
        CodeBlock.Builder b = CodeBlock.builder();
        if (interceptors != null && interceptors.size() > 0) {
            for (TypeMirror type : interceptors) {
                if (type instanceof Type.ClassType) {
                    Symbol.TypeSymbol e = ((Type.ClassType) type).asElement();
                    if (e instanceof Symbol.ClassSymbol && isInterceptor(e)) {
                        b.add(", new $T()", e);
                    }
                }
            }
        }
        return b.build();
    }

    /**
     * 生成类似下面格式的HandlerInitClass，同时生成ServiceInitClass
     *
     * <pre>
     * public class UriAnnotationInit_2ed60d4c2e177400d576cf6794f93d5c implements IUriAnnotationInit {
     *   public void init(UriAnnotationHandler handler) {
     *     handler.register("", "", "/lib2/showMySonAct", "top.ss007.demolib2.activitys.Lib2MySonAct", false);
     *   }
     * }
     * </pre>
     *
     * @param code             方法中的代码
     * @param genClassName     生成class的SimpleClassName，形如 UriAnnotationInit_2ed60d4c2e177400d576cf6794f93d5c
     * @param handlerClassName Handler类名，例如 top.ss007.router.uriHandlers.UriAnnotationHandler
     * @param interfaceName    接口名，例如 top.ss007.router.common.IUriAnnotationInit
     */
    public void buildHandlerInitClass(CodeBlock code, String genClassName, String handlerClassName, String interfaceName) {
        MethodSpec methodSpec = MethodSpec.methodBuilder(SuffixConst.INIT_METHOD)
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.VOID)
                .addParameter(className(handlerClassName), "handler")
                .addCode(code)
                .build();
        TypeSpec typeSpec = TypeSpec.classBuilder(genClassName)
                .addSuperinterface(className(interfaceName))
                .addModifiers(Modifier.PUBLIC)
                .addMethod(methodSpec)
                .build();
        try {
            JavaFile.builder(SuffixConst.GEN_PKG, typeSpec)
                    .build()
                    .writeTo(filer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String fullImplName = SuffixConst.GEN_PKG + SuffixConst.DOT + genClassName;
        String className = "ServiceInit" + SuffixConst.SPLITTER + hash(genClassName);

        new ServiceInitClassBuilder(className)
                .putDirectly(interfaceName, fullImplName, fullImplName, false)
                .build();
    }


    /**
     * 辅助工具类，用于生成ServiceInitClass，格式如下：
     * <pre>
     * package top.ss007.router.generated.service;
     *
     * public class ServiceInit_8da90e51c794acb005ea295dcd1dbc2f {
     *   public static void init() {
     *      ServiceLoader.put(Lib2Service.class, "lib2/service1", Lib2ServiceImp.class, false);
     *      }
     * }
     * </pre>
     */
    public class ServiceInitClassBuilder {

        private final String className;
        private final CodeBlock.Builder builder;
        private final ClassName serviceLoaderClass;

        public ServiceInitClassBuilder(String className) {
            this.className = className;
            this.builder = CodeBlock.builder();
            this.serviceLoaderClass = className(SuffixConst.SERVICE_LOADER_CLASS);
        }

        public ServiceInitClassBuilder put(String interfaceName, String key, String implementName, boolean singleton) {
            builder.addStatement("$T.put($T.class, $S, $T.class, $L)",
                    serviceLoaderClass,
                    className(interfaceName),
                    key,
                    className(implementName),
                    singleton);
            return this;
        }

        public ServiceInitClassBuilder putDirectly(String interfaceName, String key, String implementName, boolean singleton) {
            // implementName是注解生成的类，直接用$L拼接原始字符串
            builder.addStatement("$T.put($T.class, $S, $L.class, $L)",
                    serviceLoaderClass,
                    className(interfaceName),
                    key,
                    implementName,
                    singleton);
            return this;
        }

        public void build() {
            MethodSpec methodSpec = MethodSpec.methodBuilder(SuffixConst.INIT_METHOD)
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(TypeName.VOID)
                    .addCode(this.builder.build())
                    .build();

            TypeSpec typeSpec = TypeSpec.classBuilder(this.className)
                    .addModifiers(Modifier.PUBLIC)
                    .addMethod(methodSpec)
                    .build();
            try {
                JavaFile.builder(SuffixConst.GEN_PKG_SERVICE, typeSpec)
                        .build()
                        .writeTo(filer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
