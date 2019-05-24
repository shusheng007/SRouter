package top.ss007.router.common;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import top.ss007.router.core.Debugger;
import top.ss007.router.core.UriRequest;
import top.ss007.router.core.RouteStatusCode;

/**
 * 启动Activity的默认实现
 *
 */
public class DefaultActivityLauncher {

    public static final DefaultActivityLauncher INSTANCE = new DefaultActivityLauncher();

    @SuppressWarnings("ConstantConditions")
    public int startActivity(@NonNull UriRequest request, @NonNull Intent intent) {

        if (request == null || intent == null) {
            return RouteStatusCode.CODE_ERROR;
        }

        try {
            if (request.getRequestCode() >0 && request.getContext() instanceof Activity) {
                ActivityCompat.startActivityForResult((Activity) request.getContext(), intent, request.getRequestCode(),
                        null);
            } else {
                ActivityCompat.startActivity(request.getContext(), intent, null);
            }
            doAnimation(request);
            return RouteStatusCode.CODE_SUCCESS;
        } catch (ActivityNotFoundException e) {
            Debugger.w(e);
            return RouteStatusCode.CODE_NOT_FOUND;
        } catch (SecurityException e) {
            Debugger.w(e);
            return RouteStatusCode.CODE_FORBIDDEN;
        }
    }

    /**
     * 执行动画
     */
    protected void doAnimation(UriRequest request) {
        Context context = request.getContext();
//        int[] anim = request.getField(int[].class, FIELD_START_ACTIVITY_ANIMATION);
//        if (context instanceof Activity && anim != null && anim.length == 2) {
//            ((Activity) context).overridePendingTransition(anim[0], anim[1]);
//        }
    }
}
