package top.ss007.compiler;

import com.squareup.javapoet.CodeBlock;
import com.sun.tools.javac.code.Symbol;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

import top.ss007.annotation.annotations.RouterUri;
import top.ss007.annotation.internal.SuffixConst;

//@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class UriAnnotationProcessor extends BaseProcessor {

    private static List<? extends TypeMirror> getInterceptors(RouterUri routerUri) {
        try {
            routerUri.interceptors();
        } catch (MirroredTypesException mte) {
            return mte.getTypeMirrors();
        }
        return null;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        if (annotations == null || annotations.isEmpty()) {
            return false;
        }
        CodeBlock.Builder builder = CodeBlock.builder();
        String hash = null;
        for (Element element : env.getElementsAnnotatedWith(RouterUri.class)) {
            if (!(element instanceof Symbol.ClassSymbol)) {
                continue;
            }
            boolean isActivity = isActivity(element);
            boolean isFragment = isFragment(element);
            if (!isActivity && !isFragment) {
                mMessager.printMessage(Diagnostic.Kind.WARNING, "只支持标记Activity和Fragment", element);
                continue;
            }

            Symbol.ClassSymbol cls = (Symbol.ClassSymbol) element;
            RouterUri uri = cls.getAnnotation(RouterUri.class);
            if (uri == null) {
                continue;
            }

            if (hash == null) {
                hash = hash(cls.className());
            }
            CodeBlock destClassName = CodeBlock.builder().add("$T.class", className(cls.className())).build();
            CodeBlock interceptors = buildInterceptors(getInterceptors(uri));

            // scheme, host, path, handler, interceptors
            builder.addStatement("handler.register($S, $S, $S, $L$L)",
                    uri.scheme(),
                    uri.host(),
                    uri.path(),
                    destClassName,
                    interceptors);

        }
        if (hash == null) {
            hash = randomHash();
        }
        buildHandlerInitClass(builder.build(), "UriAnnotationInit" + SuffixConst.SPLITTER + hash,
                SuffixConst.URI_ANNOTATION_HANDLER_CLASS, SuffixConst.URI_ANNOTATION_INIT_CLASS);
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new HashSet<>(Collections.singletonList(RouterUri.class.getName()));
    }
}
