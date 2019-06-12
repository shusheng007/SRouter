package top.ss007.router.activity;

import androidx.annotation.NonNull;
import top.ss007.router.core.NavCallback;
import top.ss007.router.core.UriRequest;

/**
 * Created by Ben.Wang
 *
 * 如果默认的页面导航不能满足业务需要，可以实现此接口，自定义启动逻辑
 *
 * @author Ben.Wang
 * @modifier
 * @createDate 2019/6/12 13:30
 * @description
 */
public interface IPageLauncher {

    void legalUriNav(UriRequest request, @NonNull NavCallback callback);

    void illegalUriNav(UriRequest request, @NonNull NavCallback callback);
}
