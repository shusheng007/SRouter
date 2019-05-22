package top.ss007.router.core;

import android.content.Context;
import android.net.Uri;

import android.text.TextUtils;


import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import top.ss007.router.utils.RouterUtils;

/**
 * 一次URI跳转请求，包含了Request和Response的功能。
 * 可以通过Fields存放任意扩展参数；
 *
 * <p>
 * Created by jzj on 2017/4/11.
 */
public class UriRequest {

    @NonNull
    private  Context mContext;
    @NonNull
    private Uri mUri;
    private int requestCode;
    @NonNull
    private  Map<String, Object> mFields;



    private UriRequest(Builder builder){
        this.mContext=builder.mContext;
        this.mUri=builder.mUri;
        this.requestCode=builder.requestCode;
        this.mFields=builder.mData;
    }



    @NonNull
    public Map<String, Object> getFields() {
        return mFields;
    }

    @NonNull
    public Context getContext() {
        return mContext;
    }

    @NonNull
    public Uri getUri() {
        return mUri;
    }

    public int getInt(@NonNull String key, int defaultValue) {
        return getField(Integer.class, key, defaultValue);
    }

    public long getLong(@NonNull String key, long defaultValue) {
        return getField(Long.class, key, defaultValue);
    }

    public boolean getBoolean(@NonNull String key, boolean defaultValue) {
        return getField(Boolean.class, key, defaultValue);
    }

    public String getString(@NonNull String key, String defaultValue) {
        return getField(String.class, key, defaultValue);
    }

    public int getRequestCode(){
        return this.requestCode;
    }

    private  <T> T getField(@NonNull Class<T> clazz, @NonNull String key, T defaultValue) {
        Object field = mFields.get(key);
        if (field != null) {
            try {
                return clazz.cast(field);
            } catch (ClassCastException e) {
                e.printStackTrace();
                Debugger.fatal(e);
            }
        }
        return defaultValue;
    }


    @Override
    public String toString() {
        return mUri.toString();
    }

    public String toFullString() {
        StringBuilder s = new StringBuilder(mUri.toString());
        s.append(", fields = {");
        boolean first = true;
        for (Map.Entry<String, Object> entry : mFields.entrySet()) {
            if (first) {
                first = false;
            } else {
                s.append(", ");
            }
            s.append(entry.getKey()).append(" = ").append(entry.getValue());
        }
        s.append("}");
        return s.toString();
    }

    public static class Builder {
        private Context mContext;
        private Uri mUri;
        private int requestCode;
        private Map<String, Object> mData;

        public Builder(Context context, Uri uri) {
            this.mContext = context;
            this.mUri = uri;
            mData=new HashMap<>();
        }

        public Builder setRequestDataMap(Map<String, Object> dataMap) {
            this.mData.putAll(dataMap);
            return this;
        }

        public Builder setInt(String key,int intData){
            putField(key,intData);
            return this;
        }

        public Builder setLong(String key,long longData){
            putField(key,longData);
            return this;
        }

        public Builder setString(String key,String strData){
            putField(key,strData);
            return this;
        }

        public Builder setBoolean(String key,boolean boolData){
            putField(key,boolData);
            return this;
        }

        public Builder setObject(String key,Object objData){
            putField(key,objData);
            return this;
        }

        public Builder setRequestCode(int requestCode){
            this.requestCode=requestCode;
            return this;
        }

        public UriRequest build(){
            return new UriRequest(this);
        }

        /**
         * 设置Extra参数
         */
        private <T> void putField(@NonNull String key, T val) {
            if (val != null) {
                mData.put(key, val);
            }
        }

    }
}
