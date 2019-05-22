package top.ss007.router.core;

/**
 * 异步事件处理完成的回调
 * Created by jzj on 2017/4/11.
 */
public interface UriCallback {

    /**
     * 当前层以及处理完成，继续执行后续流程。
     */
    void onNext();

    /**
     * 当前层完成后终止分发流程。
     *
     * @param resultCode 结果
     */
    void onComplete(int resultCode);
}
