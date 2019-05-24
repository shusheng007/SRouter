package top.ss007.router.core;


public interface InterceptorCallback {

    /**
     * 当前层已经处理完成，继续执行后续流程。
     */
    void onNext(UriRequest request);

    /**
     * 当前层完成后终止分发流程。
     *
     * @param exception 中断原因
     */
    void onInterrupt(Throwable exception);

}
