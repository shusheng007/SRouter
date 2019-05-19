package top.ss007.demolib1;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import top.ss007.businessbase.services.Lib2Service;
import top.ss007.businessbase.services.ServiceKeys;
import top.ss007.router.SRouter;

public class Lib1Activity extends AppCompatActivity {

    private Lib2Service lib2Service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lib1);
        lib2Service=SRouter.getService(Lib2Service.class, ServiceKeys.KEY_LIB2_SERVICE);
        findViewById(R.id.btn_getLib2Name).setOnClickListener(v -> {
            ((Button)v).setText(lib2Service.getLib2Name());
        });
    }
}
