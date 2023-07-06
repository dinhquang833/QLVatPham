package team7.fpoly.duan1.screen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import team7.fpoly.duan1.R;

public class ChaoActivity extends AppCompatActivity {
    ImageView ivLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_hinh_chao);

        ivLogo = findViewById(R.id.ivLogo);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(ChaoActivity.this, DangNhapActivity.class));
                finish();
            }
        },3000);
    }
}