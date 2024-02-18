package com.example.eemon551;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class garally extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garally);

        // LinearLayoutのインスタンスを取得
        GridLayout gridLayout = findViewById(R.id.genre1_layout);

        // forループを使用してImageViewを動的に追加
        for (int i = 0; i < 3; i++) {
            // ImageViewを作成
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(R.drawable.oumigyuu); // あなたの画像のリソースIDに変更

            // LayoutParamsを設定してGridLayoutにImageViewを追加
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 100;
            params.height = 100;
            imageView.setLayoutParams(params);

            // GridLayoutにImageViewを追加
            gridLayout.addView(imageView);
        }
    }
}
