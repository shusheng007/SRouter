package top.ss007.router.uriHandlers;


import android.net.Uri;


import top.ss007.router.core.UriInterceptor;
import top.ss007.router.utils.RouterUtils;

/**
 *
 * 这个是一个路由目的地的信息载体
 *
 * Created by jzj on 2018/3/26.
 */
public class PathEntity {

    private String scheme;
    private String host;
    private String path;
    private Object target;
    private boolean isExport;
    private UriInterceptor[] mUriInterceptors;

    public PathEntity(String scheme, String host, String path, Object target, boolean isExport, UriInterceptor[] uriInterceptors) {
        this.scheme = scheme;
        this.host = host;
        this.path = path;
        this.target = target;
        this.isExport = isExport;
        mUriInterceptors = uriInterceptors;
    }

    public Uri getUri(){
       return Uri.parse(scheme+"://"+host+RouterUtils.insertSlashIfAbsent(path));
    }

    public Object getTarget() {
        return target;
    }

    public String getScheme() {
        return scheme;
    }

    public String getHost() {
        return host;
    }

    public String getPath() {
        return path;
    }

    public boolean isExport() {
        return isExport;
    }

    public UriInterceptor[] getUriInterceptors() {
        return mUriInterceptors;
    }
}
