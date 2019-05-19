package top.ss007.demolib2.dataManagers;

import top.ss007.annotation.RouterService;
import top.ss007.businessbase.services.Lib2Service;
import top.ss007.businessbase.services.ServiceKeys;


/**
 * Copyright (C) 2019 shusheng007
 * 完全享有此软件的著作权，违者必究
 *
 * @author shusheng007
 * @version 1.0
 * @modifier
 * @createDate 2019/5/19 11:56
 * @description
 */
@RouterService(interfaces = Lib2Service.class,key = ServiceKeys.KEY_LIB2_SERVICE)
public class Lib2ServiceImp implements Lib2Service {
    @Override
    public String getLib2Name() {
        return "I come from lib2 ";
    }
}
