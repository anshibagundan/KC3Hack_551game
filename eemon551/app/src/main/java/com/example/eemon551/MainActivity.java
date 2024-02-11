package com.example.eemon551;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private eemon_db_dao dao;

    private TextView questionTitleTextView;
    private TextView questionTextTextView;
    private ImageView questionImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // View に結びつける
        questionTitleTextView = findViewById(R.id.questionTitleTextView);
        questionTextTextView = findViewById(R.id.questionTextTextView);
        questionImageView = findViewById(R.id.questionImageView);

        // データベース取得
        AppDatabase db = Room.databaseBuilder(this, AppDatabase.class, "eemon_db")
                .build();
        dao = db.eemonDbDao();

        // データベースを読み込み
        loadQuestionsFromDatabase();
    }

    private void loadQuestionsFromDatabase() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {

            // 読み込みの処理を記載 (質問を１つだけ抽出すると仮定)
            List<eemon_db> eemonData = dao.getAll();
            eemon_db currentData = eemonData.get(0);

            // UI メインスレッドでテキストとイメージをセット
            runOnUiThread(() -> {
                questionTitleTextView.setText(currentData.name);
                questionTextTextView.setText(currentData.txt);
                // ImageViewの扱いは別に行う
            });
        });
    }
}
