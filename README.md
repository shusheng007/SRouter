# SRouter
>一款用于Android组件化的路由

# 说明
主要包含两个功能
1. 模块之间服务的路由
2. 模块之间Activity页面跳转的路由

# 使用方式
假设项目中存在3个module，BaseModule、FunModule1、FunModule2。其中BaseModule是基础模块，其余两个为业务模块，现在FunModule1 要调用FunModule2的服务，以及跳转到其中的某些页面。

## 1：引入SRouter
在`BaseModule`里面的`build.gradle `文件中配置如下依赖
```
    api 'top.shusheng007:srouter:0.1.0'
    annotationProcessor 'top.shusheng007:srouter-compiler:0.1.0'
```
## 2：用注解标注要路由的服务或Activity
### 对服务路由：
如果是对服务路由，则首先将模块的接口下沉到baseModule里面，例如在baseModule里面定义FunModule2 模块提供服务的接口Lib2Service
```
@RouterService(interfaces = Lib2Service.class,key = ServiceKeys.KEY_LIB2_SERVICE)
public class Lib2ServiceImp implements Lib2Service{
...
}
```
### 页面路由:
```
@RouterUri(path = RouteTable.LIB1_ACTIVITY1)
public class Lib1Activity extends AppCompatActivity {
}
```

## 3：发起路由请求

### 对服务路由
```
Lib2Service lib2Service=SRouter.getService(Lib2Service.class, ServiceKeys.KEY_LIB2_SERVICE);

lib2Service.getLib2Name();

lib2Service.asyncGetInfo(1, new IRequestCallback() {
    @Override
    public void onSuccess(String resultJson) {
        Toast.makeText(Lib1Activity.this,String.format("返回结果：%s",resultJson),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailed(String code, String msg) {

    }
});
```

### 页面路由:
```
SRouter.startNavNoResult(new UriRequest.Builder(this, Uri.parse(RouteTable.SCHEME_HOST + RouteTable.LIB2_ACT_MY_SON))
        .build());
```