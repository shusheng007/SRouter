package top.ss007.router.core;


import androidx.annotation.NonNull;

/**
 * URI分发完成的监听器
 *
 * Created by jzj on 2017/4/18.
 */
public interface OnCompleteListener{

    /**
     * 分发成功
     */
    void onSuccess(@NonNull UriRequest request);

    /**
     * 分发失败
     *
     * @param resultCode 错误代码
     */
    void onError(@NonNull UriRequest request, int resultCode);
}
