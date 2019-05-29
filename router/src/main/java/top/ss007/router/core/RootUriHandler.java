package top.ss007.router.core;

import android.widget.Toast;

import java.security.InvalidParameterException;

import androidx.annotation.NonNull;
import top.ss007.router.utils.SLogger;


public abstract class RootUriHandler extends UriHandler {
    private static final String TAG = "RootUriHandler";

    public RootUriHandler(String scheme, String host) {
        super(scheme, host);
    }

    public void startRequest(@NonNull UriRequest request, boolean isForResult, NavCallback callback) {
        if (request == null) {
            throw new NullPointerException("UriRequest 不能为null");
        }
        if (request.getContext() == null) {
            throw new NullPointerException("UriRequest 里的context不能为null");
        }
        if (isForResult && request.getRequestCode() == -1) {
            throw new InvalidParameterException("UriRequest 需要设置请求码");
        }
        SLogger.info(TAG, String.format("---> receive request: %s", request.toFullString()));
        handleUri(request, callback);
    }

    @Override
    protected void handleExternal(@NonNull UriRequest request, NavCallback callback) {
        Toast.makeText(request.getContext(),"暂时不支持URI:"+request.getUri().toString(),Toast.LENGTH_LONG).show();
        // TODO: 2019/5/29 处理其他scheme的uri
    }

}
