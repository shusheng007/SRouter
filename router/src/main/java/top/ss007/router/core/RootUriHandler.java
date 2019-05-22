package top.ss007.router.core;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import java.security.InvalidParameterException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import top.ss007.router.utils.RouterUtils;


/**
 * 最顶层的 {@link UriHandler}
 * <p>
 * Created by jzj on 2017/4/17.
 */
public abstract class RootUriHandler extends UriHandler {

    private final Context mContext;

    public RootUriHandler(Context context) {
        mContext = context.getApplicationContext();
    }

    public void startNavigate(@NonNull UriRequest request) {
        startRequest(request, false);
    }

    public void startNavigateForResult(@NonNull UriRequest request) {
        startRequest(request, true);
    }

    public void startRequest(@NonNull UriRequest request, boolean isForResult) {
        if (request == null) {

            String msg = "UriRequest为空";
            Debugger.fatal(msg);
            UriRequest req = new UriRequest.Builder(mContext, Uri.EMPTY).build();
            onError(req, RouteStatusCode.CODE_BAD_REQUEST);

        } else if (request.getContext() == null) {

            String msg = "UriRequest.Context为空";
            Debugger.fatal(msg);
            UriRequest req = new UriRequest.Builder(mContext, Uri.EMPTY).build();
            onError(req, RouteStatusCode.CODE_BAD_REQUEST);

        } else if (isUriEmpty(request.getUri())) {

            String msg = "跳转链接为空";
            Debugger.e(msg);
            onError(request, RouteStatusCode.CODE_BAD_REQUEST);

        } else {
            if (isForResult && request.getRequestCode() == 0) {
                throw new InvalidParameterException("UriRequest 需要设置请求码");
            }
            if (Debugger.isEnableLog()) {
                Debugger.i("");
                Debugger.i("---> receive request: %s", request.toFullString());
            }
            handleUri(request, new RootUriCallback(request));
        }
    }

    private void onSuccess(@NonNull UriRequest request) {

    }

    private void onError(@NonNull UriRequest request, int resultCode) {

    }


    private boolean isUriEmpty(Uri uri) {
        return Uri.EMPTY.equals(uri);
    }


    protected class RootUriCallback implements UriCallback {

        private final UriRequest mRequest;

        public RootUriCallback(UriRequest request) {
            mRequest = request;
        }

        @Override
        public void onNext() {
            onComplete(RouteStatusCode.CODE_NOT_FOUND);
        }

        @Override
        public void onComplete(int resultCode) {
            switch (resultCode) {

                case RouteStatusCode.CODE_REDIRECT:
                    // 重定向，重新跳转
                    Debugger.i("<--- redirect, result code = %s", resultCode);
                    if (mRequest.getRequestCode() == 0)
                        startNavigate(mRequest);
                    else
                        startNavigateForResult(mRequest);
                    break;

                case RouteStatusCode.CODE_SUCCESS:
                    // 跳转成功
                    onSuccess(mRequest);
                    Debugger.i("<--- success, result code = %s", resultCode);
                    break;

                default:
                    // 跳转失败
                    onError(mRequest, resultCode);
                    Debugger.i("<--- error, result code = %s", resultCode);
                    break;
            }
        }
    }
}
