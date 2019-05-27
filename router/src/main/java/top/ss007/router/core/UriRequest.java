package top.ss007.router.core;

import android.content.Context;
import android.net.Uri;

import android.os.Bundle;
import android.os.Parcelable;


import androidx.annotation.NonNull;
import top.ss007.router.uriHandlers.UriResponse;


public class UriRequest {

    @NonNull
    private  Context mContext;
    @NonNull
    private Uri mUri;
    private int requestCode=-1;
    @NonNull
    private Bundle mExtras;
    private int mFlags=-1;

    private int mEnterAnim = -1;
    private int mExitAnim = -1;

    private UriResponse mUriResponse;

    private UriRequest(Builder builder){
        this.mContext=builder.context;
        this.mUri=builder.uri;
        this.requestCode=builder.requestCode;
        this.mExtras =builder.data;
        this.mFlags=builder.flags;
        this.mEnterAnim=builder.enterAnim;
        this.mExitAnim=builder.exitAnim;
    }


    public void setUriResponse(UriResponse uriResponse) {
        mUriResponse = uriResponse;
    }

    public UriResponse getUriResponse() {
        return mUriResponse;
    }

    @NonNull
    public Bundle getExtras() {
        return mExtras;
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

    public int getFlags() {
        return mFlags;
    }

    public int getEnterAnim() {
        return mEnterAnim;
    }

    public int getExitAnim() {
        return mExitAnim;
    }

    public int getRequestCode(){
        return this.requestCode;
    }

    private  <T> T getField(@NonNull Class<T> clazz, @NonNull String key, T defaultValue) {
        Object field = mExtras.get(key);
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
        for (String s1 : mExtras.keySet()) {
            s.append(mExtras.get(s1));
        }
        s.append("}");
        return s.toString();
    }

    public static class Builder {
        private Context context;
        private Uri uri;
        private int requestCode;
        private Bundle data;
        private int flags;
        private int enterAnim;
        private int exitAnim;

        public Builder(Context context, Uri uri) {
            this.context = context;
            this.uri = uri;
            data =new Bundle();
        }

        public Builder setRequestData(Bundle dataMap) {
            this.data.putAll(dataMap);
            return this;
        }

        public Builder setInt(String key,int intData){
            data.putInt(key,intData);
            return this;
        }

        public Builder setLong(String key,long longData){
            data.putLong(key,longData);
            return this;
        }

        public Builder setString(String key,String strData){
            data.putString(key,strData);
            return this;
        }

        public Builder setBoolean(String key,boolean boolData){
            data.putBoolean(key,boolData);
            return this;
        }

        public Builder setParcelable(String key, Parcelable parcelableData){
            data.putParcelable(key,parcelableData);
            return this;
        }

        public Builder setRequestCode(int requestCode){
            this.requestCode=requestCode;
            return this;
        }

        public Builder setFlags(int flag){
            this.flags=flag;
            return this;
        }

        public Builder setEnterAnim(int enterAnim){
            this.enterAnim=enterAnim;
            return this;
        }

        public Builder setExitAnim(int exitAnim){
            this.exitAnim=exitAnim;
            return this;
        }



        public UriRequest build(){
            return new UriRequest(this);
        }
    }
}
