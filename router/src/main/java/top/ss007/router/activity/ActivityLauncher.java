package top.ss007.router.activity;

import android.content.Intent;

import java.lang.reflect.Modifier;

import androidx.annotation.NonNull;
import top.ss007.router.core.Debugger;
import top.ss007.router.core.NavCallback;
import top.ss007.router.core.UriHandler;
import top.ss007.router.core.UriRequest;
import top.ss007.router.core.RouteStatusCode;
import top.ss007.router.common.RouterComponents;


public class ActivityLauncher {

    private ActivityLauncher() {
    }

    public static ActivityLauncher getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        private static ActivityLauncher INSTANCE = new ActivityLauncher();
    }

    public void navigation(UriRequest request, @NonNull NavCallback callback) {
        Intent intent = createIntent(request, request.getUriResponse().getTarget());
        if (intent == null || intent.getComponent() == null) {
            Debugger.fatal("ActivityLauncher.createIntent()应返回的带有ClassName的显式跳转Intent");
            if (callback != null)
                callback.onInterrupt(request);
            return;
        }
        intent.setData(request.getUri());
        intent.putExtras(request.getExtras());

        int resultCode = RouterComponents.startActivity(request, intent);
        // 回调方法
        onActivityStartComplete(request, resultCode);
        // 完成
        if (callback != null)
            callback.onArrival(request);
    }


    /**
     * 创建用于跳转的Intent，必须是带有ClassName的显式跳转Intent，可覆写添加特殊参数
     */
    @NonNull
    protected Intent createIntent(@NonNull UriRequest request, @NonNull Object target) {
        if (target instanceof String) {
            return new Intent().setClassName(request.getContext(), (String) target);
        } else if (target instanceof Class && isValidActivityClass((Class) target)) {
            return new Intent(request.getContext(), (Class<? extends android.app.Activity>) target);
        } else {
            return null;
        }
    }

    private static boolean isValidActivityClass(Class clazz) {
        return clazz != null && android.app.Activity.class.isAssignableFrom(clazz)
                && !Modifier.isAbstract(clazz.getModifiers());
    }

    /**
     * 回调方法，子类可在此实现跳转动画等效果
     *
     * @param resultCode 跳转结果
     */
    protected void onActivityStartComplete(@NonNull UriRequest request, int resultCode) {

    }

    @Override
    public String toString() {
        return "Activity";
    }
}
