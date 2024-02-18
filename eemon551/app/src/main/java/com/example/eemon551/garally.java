package com.example.eemon551;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class garally extends AppCompatActivity {

    private ApiService apiService;

    public List<Question> QuestionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garally);

        apiService = ApiClient.getApiService();

        // GridLayoutのインスタンスを取得
        GridLayout gridLayout = findViewById(R.id.genre1_layout);
        gridLayout.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
        gridLayout.setColumnCount(3); // 3列を使用する設定

        apiService.getAllQuestions().enqueue(new Callback<List<Question>>() {
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    QuestionList.clear();
                    for (int i = 0; i < Math.min(response.body().size(), 10); i++) {
                        Question question = response.body().get(i);
                        QuestionList.add(question);
                    }
                }
            }

            public void onFailure(Call<List<Question>> call, Throwable t) {
                Log.e("API Request Failure", "Error: ", t);
            }
        });



        // forループを使用してImageViewを動的に追加
        for (int i = 0; i < 10; i++) {
            ImageView imageView = new ImageView(this);
            String img = QuestionList.get(i).getImg().replace("\"", "").trim();
            int resourceId = getResources().getIdentifier(img, "drawable", getPackageName());
            imageView.setImageResource(resourceId);

            if (QuestionList.get(i).getGenre_id() == 1) {
                imageView.setBackgroundColor(ContextCompat.getColor(garally.this, R.color.food));
            } else if (QuestionList.get(i).getGenre_id() == 2) {
                imageView.setBackgroundColor(ContextCompat.getColor(garally.this, R.color.build));
            }
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = (int) getResources().getDimension(R.dimen.image_width); // 100dpをピクセル単位に変換
            params.height = (int) getResources().getDimension(R.dimen.image_height); // 100dpをピクセル単位に変換
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f); // android:layout_columnWeight="1" に相当

            imageView.setLayoutParams(params);

            // GridLayoutにImageViewを追加
            gridLayout.addView(imageView);
        }
    }
    public void back_home(View view){
        Intent intent = new Intent(garally.this, activity_home.class);
        startActivity(intent);
    }
}
