package top.ss007.demolib2.interceptors;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import top.ss007.router.core.InterceptorCallback;
import top.ss007.router.core.UriInterceptor;
import top.ss007.router.core.UriRequest;

/**
 * Created by Ben.Wang
 *
 * @author Ben.Wang
 * @modifier
 * @createDate 2019/5/22 16:01
 * @description
 */
public class LocatingInterceptor implements UriInterceptor {

    @Override
    public void intercept(@NonNull UriRequest request, @NonNull InterceptorCallback callback) {
        Toast.makeText(request.getContext(),"拦截器拦截后开始定位...",Toast.LENGTH_SHORT).show();
        Executors.newSingleThreadExecutor().execute(()->{
            try {
                Thread.sleep(3_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            new Handler(Looper.getMainLooper()).post(() -> {
                Toast.makeText(request.getContext(),String.format("定位成功，开始跳转:%s",request.getUri().toString()),Toast.LENGTH_SHORT).show();
                callback.onNext(request);
            });
        });

    }
}
