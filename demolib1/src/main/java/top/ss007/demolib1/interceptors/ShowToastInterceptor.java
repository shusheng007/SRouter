package top.ss007.demolib1.interceptors;

import android.widget.Toast;

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
public class ShowToastInterceptor implements UriInterceptor {

    @Override
    public void intercept(@NonNull UriRequest request, @NonNull InterceptorCallback callback) {
        Toast.makeText(request.getContext(),"我来自拦截器，URI:"+request.getUri().toString(),Toast.LENGTH_SHORT).show();
        callback.onNext(request);
    }
}
