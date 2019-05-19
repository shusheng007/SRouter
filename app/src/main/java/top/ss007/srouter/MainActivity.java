package top.ss007.srouter;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import top.ss007.demolib1.Lib1Activity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_show_lib1).setOnClickListener(v->{
            startActivity(new Intent(this, Lib1Activity.class));
        });
    }
}
