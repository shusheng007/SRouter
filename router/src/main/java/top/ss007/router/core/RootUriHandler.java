package top.ss007.router.core;

import android.content.Context;
import android.net.Uri;

import java.security.InvalidParameterException;

import androidx.annotation.NonNull;
import top.ss007.router.uriHandlers.UriResponse;


public abstract class RootUriHandler extends UriHandler {

    private final Context mContext;

    public RootUriHandler(Context context) {
        mContext = context.getApplicationContext();
    }

    public void startNav(@NonNull UriRequest request, boolean isForResult, NavCallback callback) {
        startRequest(request, false, callback);
    }

    public void startNavNoCallback(@NonNull UriRequest request, boolean isForResult) {
        startRequest(request, isForResult, null);
    }

    public void startNavNoResult(@NonNull UriRequest request) {
        startRequest(request, false, null);
    }

    public void startNavForResult(@NonNull UriRequest request) {
        startRequest(request, true, null);
    }

    private void startRequest(@NonNull UriRequest request, boolean isForResult, NavCallback callback) {

        if (request == null) {
            throw new NullPointerException("UriRequest 不能为null");
        }
        if (request.getContext() == null) {
            throw new NullPointerException("UriRequest 里的context不能为null");
        }
        if (isForResult && request.getRequestCode() == -1) {
            throw new InvalidParameterException("UriRequest 需要设置请求码");
        }
        if (Debugger.isEnableLog()) {
            Debugger.i("---> receive request: %s", request.toFullString());
        }
        handleUri(request, callback);
    }
}
