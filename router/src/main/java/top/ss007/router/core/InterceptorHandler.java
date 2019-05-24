package top.ss007.router.core;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import top.ss007.router.uriHandlers.UriResponse;


/**
 * 支持添加多个子 {@link UriInterceptor} ，按先后顺序依次异步执行
 *
 */
public class InterceptorHandler {

    private final List<UriInterceptor> mInterceptors = new LinkedList<>();

    public void addInterceptor(@NonNull UriInterceptor interceptor) {
        if (interceptor != null) {
            mInterceptors.add(interceptor);
        }
    }

    public void clearInterceptors(){
        mInterceptors.clear();
    }


    public void handleIntercept(@NonNull UriRequest request, @NonNull InterceptorCallback callback){
        next(mInterceptors.iterator(), request, callback);
    }

    private void next(@NonNull final Iterator<UriInterceptor> iterator, @NonNull final UriRequest request,
                      @NonNull final InterceptorCallback callback) {
        if (iterator.hasNext()) {
            UriInterceptor t = iterator.next();
            if (Debugger.isEnableLog()) {
                Debugger.i("    %s: intercept, request = %s", t.getClass().getSimpleName(), request);
            }
            t.intercept(request,new InterceptorCallback() {

                @Override
                public void onNext(UriRequest request) {
                    next(iterator, request, callback);
                }

                @Override
                public void onInterrupt(Throwable exception) {
                    callback.onInterrupt(exception);
                }
            });
        } else {//拦截器处理完了就继续
            callback.onNext(request);
        }
    }
}
