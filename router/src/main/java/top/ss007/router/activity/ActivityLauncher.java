package top.ss007.router.activity;

import android.app.Activity;
import android.content.Intent;

import java.lang.reflect.Modifier;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import top.ss007.router.core.NavCallback;
import top.ss007.router.core.UriRequest;


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
        Intent intent = createIntent(request, request.getUriResponse().getDestination());
        if (intent == null || intent.getComponent() == null) {
            if (callback != null)
                callback.onInterrupt(request);
            return;
        }
        intent.putExtras(request.getExtras());
        intent.setData(request.getUri());

        // Set flags.
        int flags = request.getFlags();
        if (-1 != flags) {
            intent.setFlags(flags);
        } else if (!(request.getContext() instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        startActivity(request,intent,callback);
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

    private void startActivity(@NonNull UriRequest request, @NonNull Intent intent,NavCallback callback) {
        if (request.getRequestCode() >0){
            if (request.getContext() instanceof Activity){
                ActivityCompat.startActivityForResult((Activity) request.getContext(), intent, request.getRequestCode(),
                        null);
            }
        }else {
            ActivityCompat.startActivity(request.getContext(), intent, null);
        }

        if ((-1 != request.getEnterAnim() && -1 != request.getExitAnim()) && request.getContext() instanceof Activity) {
            ((Activity) request.getContext()).overridePendingTransition(request.getEnterAnim(), request.getExitAnim());
        }

        if (callback!=null){
            callback.onArrival(request);
        }
    }

    @Override
    public String toString() {
        return "Activity";
    }
}
