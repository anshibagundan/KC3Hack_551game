package com.example.eemon551;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class garally extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garally);

        // LinearLayoutのインスタンスを取得
        LinearLayout linearLayout = findViewById(R.id.linearLayout);

        // forループを使用してImageViewを動的に追加
        for (int i = 0; i < 3; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            imageView.setImageResource(R.drawable.oumigyuu); // 画像リソースの設定

            // ImageViewをLinearLayoutに追加
            linearLayout.addView(imageView);
        }
    }
}
