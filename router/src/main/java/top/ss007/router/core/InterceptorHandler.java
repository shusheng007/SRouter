package top.ss007.router.core;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;


/**
 * 支持添加多个子 {@link UriInterceptor} ，按先后顺序依次异步执行
 * Created by jzj on 2017/4/11.
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


    public void handleIntercept(@NonNull UriRequest request, @NonNull UriCallback callback){
        next(mInterceptors.iterator(), request, callback);
    }

    private void next(@NonNull final Iterator<UriInterceptor> iterator, @NonNull final UriRequest request,
                      @NonNull final UriCallback callback) {
        if (iterator.hasNext()) {
            UriInterceptor t = iterator.next();
            if (Debugger.isEnableLog()) {
                Debugger.i("    %s: intercept, request = %s", t.getClass().getSimpleName(), request);
            }
            t.intercept(request, new UriCallback() {
                @Override
                public void onNext() {
                    next(iterator, request, callback);
                }

                @Override
                public void onComplete(int resultCode) {
                    callback.onComplete(resultCode);
                }
            });
        } else {
            callback.onNext();
        }
    }
}
