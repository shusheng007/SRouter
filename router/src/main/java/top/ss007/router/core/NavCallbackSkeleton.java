package top.ss007.router.core;

import top.ss007.router.uriHandlers.UriResponse;

/**
 * Created by Ben.Wang
 *
 * @author Ben.Wang
 * @modifier
 * @createDate 2019/5/23 16:46
 * @description
 */
public abstract class NavCallbackSkeleton implements NavCallback {

    @Override
    public void onInterrupt(UriRequest request) {
       //如果子类需要可以overwrite这个方法。
    }
}
