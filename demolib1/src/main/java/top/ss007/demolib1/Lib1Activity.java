package top.ss007.demolib1;

import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import top.ss007.annotation.annotations.RouterUri;
import top.ss007.businessbase.RouteTable;
import top.ss007.businessbase.services.IRequestCallback;
import top.ss007.businessbase.services.Lib2Service;
import top.ss007.businessbase.services.ServiceKeys;
import top.ss007.router.SRouter;
import top.ss007.router.core.NavCallbackSkeleton;
import top.ss007.router.core.UriRequest;
import top.ss007.router.services.IFactory;
import top.ss007.router.utils.SLogger;

@RouterUri(path = RouteTable.LIB1_ACT_PANEL)
public class Lib1Activity extends AppCompatActivity {

    private Lib2Service lib2Service;

    private TextView tvResult;
    private StringBuilder sb = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lib1);
        tvResult = findViewById(R.id.tv_result);
        tvResult.setMovementMethod(new ScrollingMovementMethod());

        //如果实现类存在带有参数的构造函数，可以使用IFactory的方式传入
        lib2Service = SRouter.getService(Lib2Service.class, ServiceKeys.KEY_LIB2_SERVICE, new IFactory() {
            @NonNull
            @Override
            public <T> T create(@NonNull Class<T> clazz) throws Exception {
                return clazz.getConstructor(String.class).newInstance("天津陆家嘴");
            }
        });
        //lib2Service=SRouter.getService(Lib2Service.class, ServiceKeys.KEY_LIB2_SERVICE);

        findViewById(R.id.btn_getLib2Name).setOnClickListener(v -> {
            sb.append("开始同步调用Lib2的方法...\n");
            sb.append("执行结果：" + lib2Service.syncGetLib2Name() + "\n");
            tvResult.setText(sb.toString());
        });


        findViewById(R.id.btn_async_invoke).setOnClickListener(v -> {
            sb.append("开始异步调用Lib2的方法...\n");
            tvResult.setText(sb.toString());
            lib2Service.asyncGetInfo(1, new IRequestCallback() {
                @Override
                public void onSuccess(String resultJson) {
                    sb.append(String.format("执行成功，结果为：%s\n", resultJson));
                    tvResult.setText(sb.toString());
                }

                @Override
                public void onFailed(String code, String msg) {
                    sb.append(String.format("执行失败，code:%s | message:%s\n", code, msg));
                    tvResult.setText(sb.toString());
                }
            });
        });

        findViewById(R.id.btn_check_boy).setOnClickListener(v -> {
            sb.append("开始打开Lib2的Activity...\n");
            sb.append("Uri:" + RouteTable.SCHEME_HOST + RouteTable.LIB2_ACT_MY_SON + "\n");
            tvResult.setText(sb.toString());

            SRouter.startNavNoResult(new UriRequest.Builder(this, Uri.parse(RouteTable.SCHEME_HOST + RouteTable.LIB2_ACT_MY_SON))
                    .setString("name", "my name is cc")
                    .setEnterAnim(R.anim.slide_in_bottom)
                    .setExitAnim(R.anim.slide_out_bottom)
                    .build());
        });

        findViewById(R.id.btn_get_fragment).setOnClickListener(v -> {
            sb.append("开始获取Lib2的Fragment...\n");
            tvResult.setText(sb.toString());

            SRouter.startNav(new UriRequest.Builder(this, Uri.parse(RouteTable.SCHEME_HOST + RouteTable.LIB2_FRAG_BLANK))
                            .setString("userName","shusheng007").build(), false,
                    new NavCallbackSkeleton() {
                        @Override
                        public void onArrival(UriRequest request) {
                            try {
                                Object instance = request.getUriResponse().getDestination().getConstructor().newInstance();
                                if (instance instanceof androidx.fragment.app.Fragment) {
                                    ((androidx.fragment.app.Fragment) instance).setArguments(request.getExtras());

                                    sb.append(instance.getClass().getCanonicalName()+"\n");
                                    tvResult.setText(sb.toString());
                                }
                            } catch (Exception ex) {
                                SLogger.error("Launcher", "Fetch fragment instance error, " + ex.getStackTrace());
                            }
                        }
                    });
        });

        findViewById(R.id.btn_start_uri).setOnClickListener(v -> {


            SRouter.startNavNoResult(new UriRequest.Builder(this, Uri.parse("https://baidu.com")).build());
        });

    }
}
