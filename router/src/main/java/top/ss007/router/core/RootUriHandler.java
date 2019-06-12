package top.ss007.router.core;

import java.security.InvalidParameterException;

import androidx.annotation.NonNull;
import top.ss007.router.activity.DefaultPageLauncher;
import top.ss007.router.activity.IPageLauncher;
import top.ss007.router.utils.SLogger;


public abstract class RootUriHandler extends UriHandler {
    private static final String TAG = "RootUriHandler";

    protected IPageLauncher mIPageLauncher;

    public RootUriHandler(String scheme, String host) {
        super(scheme, host);
    }

    public void setPageLauncher(IPageLauncher pageLauncher){
        this.mIPageLauncher=pageLauncher;
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
        SLogger.info(TAG, String.format("receive request: %s", request.toFullString()));
        handleUri(request, callback);
    }

    @Override
    protected void handleExternal(@NonNull UriRequest request, NavCallback callback) {
        if (mIPageLauncher==null) {
            mIPageLauncher=new DefaultPageLauncher();
        }
        mIPageLauncher.illegalUriNav(request,callback);
    }

}
