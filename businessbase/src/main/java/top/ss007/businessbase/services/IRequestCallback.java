package top.ss007.businessbase.services;

public interface IRequestCallback {
    void onSuccess(String resultJson);

    void onFailed(String code, String msg);
}
