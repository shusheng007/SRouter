package top.ss007.demolib1;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import top.ss007.annotation.RouterUri;
import top.ss007.businessbase.RouteTable;
import top.ss007.businessbase.services.Lib2Service;
import top.ss007.businessbase.services.ServiceKeys;
import top.ss007.demolib1.interceptors.ShowToastInterceptor;
import top.ss007.router.SRouter;
import top.ss007.router.core.UriRequest;
import top.ss007.router.services.IFactory;

@RouterUri(path = RouteTable.LIB1_ACTIVITY1,interceptors = {ShowToastInterceptor.class})
public class Lib1Activity extends AppCompatActivity {

    private Lib2Service lib2Service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lib1);
        //如果实现类存在带有参数的构造函数，可以使用实现IFactory的方式传入
        lib2Service=SRouter.getService(Lib2Service.class, ServiceKeys.KEY_LIB2_SERVICE, new IFactory() {
            @NonNull
            @Override
            public <T> T create(@NonNull Class<T> clazz) throws Exception {
                return clazz.getConstructor(String.class).newInstance("天津陆家嘴");
            }
        });
        //lib2Service=SRouter.getService(Lib2Service.class, ServiceKeys.KEY_LIB2_SERVICE);
        findViewById(R.id.btn_getLib2Name).setOnClickListener(v -> {
            ((Button)v).setText(lib2Service.getLib2Name());
        });


        findViewById(R.id.btn_check_boy).setOnClickListener(v -> {
            SRouter.startNavigate(new UriRequest.Builder(this, Uri.parse("srouter://host"+RouteTable.LIB2_ACT_MY_SON))
                    .setString("name","my name is cc")
                    .build());
        });
    }
}
