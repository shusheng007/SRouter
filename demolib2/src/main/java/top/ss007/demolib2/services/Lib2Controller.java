package top.ss007.demolib2.services;


//import top.ss007.router.generated.service.ServiceInit_Lib2ServiceImp;

/**
 * Copyright (C) 2019 shusheng007
 * 完全享有此软件的著作权，违者必究
 *
 * @author shusheng007
 * @version 1.0
 * @modifier
 * @createDate 2019/5/19 19:16
 * @description
 */
public class Lib2Controller {
    public static void init(){
        try {
            // 编译的时候生成的代码还不存在，所以需要使用反射
            Class.forName("top.ss007.router.generated.service.ServiceInit_Lib2ServiceImp")
                    .getMethod("init")
                    .invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //ServiceInit_Lib2ServiceImp.init();
    }
}
