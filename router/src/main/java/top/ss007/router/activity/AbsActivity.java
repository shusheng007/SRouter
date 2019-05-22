package top.ss007.router.activity;

import android.content.Intent;

import java.lang.reflect.Modifier;

import androidx.annotation.NonNull;
import top.ss007.router.core.Debugger;
import top.ss007.router.core.UriCallback;
import top.ss007.router.core.UriHandler;
import top.ss007.router.core.UriRequest;
import top.ss007.router.core.RouteStatusCode;
import top.ss007.router.common.RouterComponents;
import top.ss007.router.uriHandlers.PathEntity;


/**
 * 跳转Activity的 {@link UriHandler}
 * <p>
 * Created by jzj on 2017/4/11.
 */
public class AbsActivity {

    public void navigation(UriRequest request, PathEntity pathEntity, @NonNull UriCallback callback) {
        Intent intent = createIntent(request, pathEntity.getTarget());
        if (intent == null || intent.getComponent() == null) {
            Debugger.fatal("AbsActivity.createIntent()应返回的带有ClassName的显式跳转Intent");
            callback.onComplete(RouteStatusCode.CODE_ERROR);
            return;
        }
        intent.setData(pathEntity.getUri());
        //intent.putExtra("")
        int resultCode = RouterComponents.startActivity(request, intent);
        // 回调方法
        onActivityStartComplete(request, resultCode);
        // 完成
        callback.onComplete(resultCode);
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
