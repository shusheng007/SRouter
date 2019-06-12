package top.ss007.businessbase.services;

/**
 * Copyright (C) 2019 shusheng007
 * 完全享有此软件的著作权，违者必究
 *
 * @author shusheng007
 * @version 1.0
 * @modifier
 * @createDate 2019/5/19 12:15
 * @description
 */
public interface Lib2Service {
    String syncGetLib2Name();

    void asyncGetInfo(int id, IRequestCallback callback);
}
