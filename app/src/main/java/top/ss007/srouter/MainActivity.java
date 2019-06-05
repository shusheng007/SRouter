package top.ss007.srouter;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import top.ss007.businessbase.RouteTable;
import top.ss007.router.SRouter;
import top.ss007.router.core.UriRequest;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_show_lib1).setOnClickListener(v -> {
            SRouter.startNavNoResult(new UriRequest.Builder(this, Uri.parse(RouteTable.SCHEME_HOST + RouteTable.LIB1_ACTIVITY1))
                    .build());
        });
    }
}
