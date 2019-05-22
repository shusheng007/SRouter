package top.ss007.srouter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import top.ss007.businessbase.RouteTable;
import top.ss007.demolib1.Lib1Activity;
import top.ss007.router.SRouter;
import top.ss007.router.core.UriRequest;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_show_lib1).setOnClickListener(v->{
            SRouter.startNavigate(new UriRequest.Builder(this, Uri.parse("srouter://host"+RouteTable.LIB1_ACTIVITY1))
            .build());
        });
    }
}
