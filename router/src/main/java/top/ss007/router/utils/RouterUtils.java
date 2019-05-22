package top.ss007.router.utils;

import android.net.Uri;
import android.text.TextUtils;


import java.util.Map;

import androidx.annotation.NonNull;
import top.ss007.router.core.Debugger;

public class RouterUtils {

    public static String toLowerCase(String s) {
        return TextUtils.isEmpty(s) ? s : s.toLowerCase();
    }

    public static String toNonNullString(String s) {
        return s == null ? "" : s;
    }


    /**
     * 根据scheme和host生成字符串
     */
    @NonNull
    public static String schemeHost(String scheme, String host) {
        return toNonNullString(toLowerCase(scheme)) + "://" + toNonNullString(toLowerCase(host));
    }

    public static String schemeHostPath(String scheme, String host, String path) {
        return schemeHost(scheme, host) + insertSlashIfAbsent(path);
    }

    public static String schemeHostPath(Uri uri) {
        return schemeHost(uri) + insertSlashIfAbsent(uri.getPath());
    }

    /**
     * 根据scheme和host生成字符串
     */
    public static String schemeHost(Uri uri) {
        return uri == null ? null : schemeHost(uri.getScheme(), uri.getHost());
    }

    /**
     * 在Uri中添加参数
     *
     * @param uri    原始uri
     * @param params 要添加的参数
     * @return uri    新的uri
     */
    public static Uri appendParams(Uri uri, Map<String, String> params) {
        if (uri != null && params != null && !params.isEmpty()) {
            Uri.Builder builder = uri.buildUpon();
            try {
                for (String key : params.keySet()) {
                    if (TextUtils.isEmpty(key)) continue;
                    final String val = uri.getQueryParameter(key);
                    if (val == null) { // 当前没有此参数时，才会添加
                        final String value = params.get(key);
                        builder.appendQueryParameter(key, value);
                    }
                }
                return builder.build();
            } catch (Exception e) {
                Debugger.fatal(e);
            }
        }
        return uri;
    }

    /**
     * 添加斜线前缀
     */
    public static String insertSlashIfAbsent(String path) {
        if (path != null && !path.startsWith("/")) {
            path = '/' + path;
        }
        return path;
    }
}
