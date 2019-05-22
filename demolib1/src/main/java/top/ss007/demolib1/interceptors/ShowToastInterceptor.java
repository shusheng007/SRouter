package top.ss007.demolib1.interceptors;

import android.widget.Toast;

import androidx.annotation.NonNull;
import top.ss007.router.core.UriCallback;
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
    public void intercept(@NonNull UriRequest request, @NonNull UriCallback callback) {
        Toast.makeText(request.getContext(),"URI:"+request.getUri(),Toast.LENGTH_LONG).show();
        callback.onNext();
    }
}
