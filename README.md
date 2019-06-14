# SRouter
>一款用于Android组件化的路由

# 说明
现在存在很多为android组件化而生的路由，大家的思路基本都一样，区别就在实现复杂度上，本项目在解决问题的前提下力求简单易懂，上帝喜欢简单事务。
组件化主要解决模块间解耦与通信的问题，其实这哥两是如影随形的，本身必须要要有联系，而我们现在又不希望他们纠缠在一起（解耦），还要求他们互相之间调用（通信）。

1. 模块之间服务的路由
2. 模块之间Activity页面跳转的路由

# 使用方式
假设项目中存在3个Module，BaseModule、FunModule1、FunModule2。其中BaseModule是基础模块，其余两个为业务模块，现在FunModule1 要调用FunModule2的服务，以及跳转其中的某些功能页面。

## 1：引入SRouter
在`BaseModule`里面的`build.gradle `文件中配置如下依赖
```
    api 'top.shusheng007:srouter:0.2.0'
    annotationProcessor 'top.shusheng007:srouter-compiler:0.2.0'
```
并在你工程的Application里面初始化SRouter

```
SRouter.init();
```
## 2：路由
### 对服务路由：
第一步：

 如果是对服务路由，则首先将模块的接口下沉到baseModule里面，例如在baseModule里面定义FunModule2 模块提供服务的接口Lib2Service

在FunModule2中实现此接口，对外提供服务，使用`@RouterService`注解标注此服务实现类。

```
@RouterService(interfaces = Lib2Service.class,key = ServiceKeys.KEY_LIB2_SERVICE)
public class Lib2ServiceImp implements Lib2Service{
...
}
```

第二步：

 在FunModule1 中要使用FunModule2 的服务，那么就可以使用SRouter获取此接口实现类，然后调用其方法即可。

```
Lib2Service lib2Service=SRouter.getService(Lib2Service.class, ServiceKeys.KEY_LIB2_SERVICE);
//调用同步方法
lib2Service.syncGetLib2Name() ;
//调用异步方法
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

### 对页面路由:
第一步：

使用`@RouterUri` 注解标注要跳转的`Activity`，例如`FunModule2`中的Lib2MySonAct 
```
@RouterUri(path = RouteTable.LIB2_ACT_MY_SON, interceptors = LocatingInterceptor.class)
public class Lib2MySonAct extends AppCompatActivity {
}
```
第二步：
发起跳转路由
```
SRouter.startNavNoResult(new UriRequest.Builder(this, Uri.parse(RouteTable.SCHEME_HOST + RouteTable.LIB2_ACT_MY_SON))
          .setString("name", "my name is cc")
          .setEnterAnim(R.anim.slide_in_bottom)
          .setExitAnim(R.anim.slide_out_bottom)
          .build());
```
