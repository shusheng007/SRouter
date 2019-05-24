package top.ss007.demolib2.activitys;

import androidx.appcompat.app.AppCompatActivity;
import top.ss007.annotation.annotations.RouterUri;
import top.ss007.businessbase.RouteTable;
import top.ss007.demolib2.R;

import android.os.Bundle;

@RouterUri(path = RouteTable.LIB2_ACT_MY_SON)
public class Lib2MySonAct extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_lib2_my_son);

    }
}
