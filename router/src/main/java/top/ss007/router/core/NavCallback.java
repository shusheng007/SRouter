package top.ss007.router.core;

import top.ss007.router.uriHandlers.UriResponse;

/**
 * Created by Ben.Wang
 * 跳转完成后的回调
 *
 * @author Ben.Wang
 * @modifier
 * @createDate 2019/5/23 16:41
 * @description
 */
public interface NavCallback {
    void onArrival(UriRequest request);

    /**
     * Callback on interrupt.
     *
     */
    void onInterrupt(UriRequest request);
}
