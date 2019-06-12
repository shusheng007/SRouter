package top.ss007.router.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.text.TextUtils;

import java.lang.reflect.Modifier;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import top.ss007.router.core.NavCallback;
import top.ss007.router.core.UriRequest;
import top.ss007.router.utils.SLogger;


public class DefaultPageLauncher implements IPageLauncher {

    private static boolean isValidActivityClass(Class clazz) {
        return clazz != null && android.app.Activity.class.isAssignableFrom(clazz)
                && !Modifier.isAbstract(clazz.getModifiers());
    }

    private static boolean isValidFragmentClass(Class clazz) {
        return clazz != null&& !Modifier.isAbstract(clazz.getModifiers() ) &&
                (androidx.fragment.app.Fragment.class.isAssignableFrom(clazz)||android.app.Fragment.class.isAssignableFrom(clazz));
    }

    private Intent createIntent(@NonNull UriRequest request, @NonNull Class target) {
        if (target instanceof Class && isValidActivityClass(target)) {
            return new Intent(request.getContext(), (Class<? extends android.app.Activity>) target);
        } else {
            return null;
        }
    }

    private void startActivity(@NonNull UriRequest request, @NonNull Intent intent, NavCallback callback) {
        if (request.getRequestCode() > 0) {
            if (request.getContext() instanceof Activity) {
                ActivityCompat.startActivityForResult((Activity) request.getContext(), intent, request.getRequestCode(),
                        request.getOptionsCompat());
            }
        } else {
            ActivityCompat.startActivity(request.getContext(), intent, request.getOptionsCompat());
        }

        if ((-1 != request.getEnterAnim() && -1 != request.getExitAnim()) && request.getContext() instanceof Activity) {
            ((Activity) request.getContext()).overridePendingTransition(request.getEnterAnim(), request.getExitAnim());
        }

        if (callback != null) {
            callback.onArrival(request);
        }
    }

    @Override
    public void legalUriNav(UriRequest request, @NonNull NavCallback callback) {

        Class destMeta =request.getUriResponse().getDestination();

        if (isValidFragmentClass(destMeta)) {
            if (callback!=null){
                callback.onArrival(request);
            }
/*            try {
                Object instance = destMeta.getConstructor().newInstance();
                if (instance instanceof Fragment) {
                    ((android.app.Fragment) instance).setArguments(request.getExtras());
                } else if (instance instanceof androidx.fragment.app.Fragment) {
                    ((androidx.fragment.app.Fragment) instance).setArguments(request.getExtras());
                }
            } catch (Exception ex) {
                SLogger.error("Launcher", "Fetch fragment instance error, " + ex.getStackTrace());
            }*/
        }
        if (isValidActivityClass(destMeta)) {
            Intent intent = createIntent(request, destMeta);
            if (intent == null || intent.getComponent() == null) {
                if (callback != null)
                    callback.onInterrupt(request);
                return;
            }
            intent.putExtras(request.getExtras());
            // Set flags.
            int flags = request.getFlags();
            if (-1 != flags) {
                intent.setFlags(flags);
            } else if (!(request.getContext() instanceof Activity)) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            startActivity(request, intent, callback);
        }

    }

    @Override
    public void illegalUriNav(UriRequest request, @NonNull NavCallback callback) {
        Intent intent = new Intent();
        intent.setData(request.getUri());
        startActivity(request, intent, callback);
    }

}
