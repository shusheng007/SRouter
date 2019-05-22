package top.ss007.router.utils;


import java.lang.reflect.Modifier;

/**
 * 跳转目标，支持ActivityClass, ActivityClassName, UriHandler。
 *
 * Created by jzj on 2018/3/26.
 */

public class UriTargetTools {


    private static void toHandler(Object target) {
//        if (target instanceof String) {
//            return new ActivityClassName((String) target);
//        } else if (target instanceof Class && isValidActivityClass((Class) target)) {
//            return new Activity((Class<? extends android.app.Activity>) target);
//        } else {
//            return null;
//        }
    }

    private static boolean isValidActivityClass(Class clazz) {
        return clazz != null && android.app.Activity.class.isAssignableFrom(clazz)
                && !Modifier.isAbstract(clazz.getModifiers());
    }
}
