package top.ss007.router.uriHandlers;


import android.net.Uri;


import top.ss007.router.core.UriInterceptor;
import top.ss007.router.core.UriRequest;
import top.ss007.router.utils.RouterUtils;

/**
 *
 * 这个是一个路由目的地的信息载体
 *
 */
public class UriResponse {

    private String scheme;
    private String host;
    private String path;
    private Class destination;
    private UriInterceptor[] mUriInterceptors;

    public UriResponse(String scheme, String host, String path, Class destination, UriInterceptor[] uriInterceptors) {
        this.scheme = scheme;
        this.host = host;
        this.path = path;
        this.destination = destination;
        mUriInterceptors = uriInterceptors;
    }

    public Uri getUri(){
       return Uri.parse(scheme+"://"+host+RouterUtils.insertSlashIfAbsent(path));
    }

    public Class getDestination() {
        return destination;
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


    public UriInterceptor[] getUriInterceptors() {
        return mUriInterceptors;
    }
}
