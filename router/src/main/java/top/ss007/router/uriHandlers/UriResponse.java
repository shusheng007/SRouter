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
    private Object destination;
    private boolean isExport;
    private UriInterceptor[] mUriInterceptors;

    public UriResponse(String scheme, String host, String path, Object destination, boolean isExport, UriInterceptor[] uriInterceptors) {
        this.scheme = scheme;
        this.host = host;
        this.path = path;
        this.destination = destination;
        this.isExport = isExport;
        mUriInterceptors = uriInterceptors;
    }

    public static UriResponse fromUriRequest(UriRequest request){
        Uri uri=request.getUri();
       return new UriResponse(uri.getScheme(),uri.getHost(),uri.getPath(),null,false,null);
    }

    public Uri getUri(){
       return Uri.parse(scheme+"://"+host+RouterUtils.insertSlashIfAbsent(path));
    }

    public Object getDestination() {
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

    public boolean isExport() {
        return isExport;
    }

    public UriInterceptor[] getUriInterceptors() {
        return mUriInterceptors;
    }
}
