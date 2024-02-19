package com.example.eemon551;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class garally extends AppCompatActivity {

    private ApiService apiService;
    private List<Question> Osaka_QuestionList = new ArrayList<>();
    private List<Question> Kyoto_QuestionList = new ArrayList<>();
    private List<Question> Shiga_QuestionList = new ArrayList<>();
    private List<Question> Nara_QuestionList = new ArrayList<>();
    private List<Question> Hyogo_QuestionList = new ArrayList<>();
    private List<Question> Wakayama_QuestionList = new ArrayList<>();

    private GridLayout gridLayout_1;
    private GridLayout gridLayout_2;
    private GridLayout gridLayout_3;
    private GridLayout gridLayout_4;
    private GridLayout gridLayout_5;
    private GridLayout gridLayout_6;

    private FrameLayout card;
    private FrameLayout zukan;
    private ImageView card_image;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garally);

        apiService = ApiClient.getApiService();
        gridLayout_1 = findViewById(R.id.genre1_layout);
        gridLayout_2 = findViewById(R.id.genre2_layout);
        gridLayout_3 = findViewById(R.id.genre3_layout);
        gridLayout_4 = findViewById(R.id.genre4_layout);
        gridLayout_5 = findViewById(R.id.genre5_layout);
        gridLayout_6 = findViewById(R.id.genre6_layout);
        gridLayout_1.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
        gridLayout_1.setColumnCount(3);
        gridLayout_2.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
        gridLayout_2.setColumnCount(3);
        gridLayout_3.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
        gridLayout_3.setColumnCount(3);
        gridLayout_4.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
        gridLayout_4.setColumnCount(3);
        gridLayout_5.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
        gridLayout_5.setColumnCount(3);
        gridLayout_6.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
        gridLayout_6.setColumnCount(3);
        card = findViewById(R.id.card);
        zukan = findViewById(R.id.zukan);
        card_image = findViewById(R.id.card_image);

        fetchQuestions();
    }

    private void fetchQuestions() {
        apiService.getAllQuestions().enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    // 各リストをクリア
                    Osaka_QuestionList.clear();
                    Kyoto_QuestionList.clear();
                    Shiga_QuestionList.clear();
                    Nara_QuestionList.clear();
                    Hyogo_QuestionList.clear();
                    Wakayama_QuestionList.clear();

                    // 取得した質問リストをループ
                    for (Question question : response.body()) {
                        switch (question.getLoc_id()) {
                            case 1:
                                Osaka_QuestionList.add(question);
                                break;
                            case 2:
                                Kyoto_QuestionList.add(question);
                                break;
                            case 3:
                                Shiga_QuestionList.add(question);
                                break;
                            case 4:
                                Nara_QuestionList.add(question);
                                break;
                            case 5:
                                Hyogo_QuestionList.add(question);
                                break;
                            case 6:
                                Wakayama_QuestionList.add(question);
                                break;
                            default:
                                // 関西以外はないと思う
                                break;
                        }
                    }

                    // UIを更新するためのメソッドをメインスレッドで実行
                    runOnUiThread(() -> addImagesToGridLayout());
                }
            }


            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                Log.e("API Request Failure", "Error: ", t);
            }
        });
    }

    private void addImagesToGridLayout() {
        for (int i = 0; i < Osaka_QuestionList.size(); i++) {
            Question question = Osaka_QuestionList.get(i);
            ImageView imageView = new ImageView(this);
            DisplayQuestion(question,imageView);
            gridLayout_1.addView(imageView);
        }
        for (int i = 0; i < Kyoto_QuestionList.size(); i++) {
            Question question = Kyoto_QuestionList.get(i);
            ImageView imageView = new ImageView(this);
            DisplayQuestion(question,imageView);
            gridLayout_2.addView(imageView);
        }
        for (int i = 0; i < Shiga_QuestionList.size(); i++) {
            Question question = Shiga_QuestionList.get(i);
            ImageView imageView = new ImageView(this);
            DisplayQuestion(question,imageView);
            gridLayout_3.addView(imageView);
        }
        for (int i = 0; i < Nara_QuestionList.size(); i++) {
            Question question = Nara_QuestionList.get(i);
            ImageView imageView = new ImageView(this);
            DisplayQuestion(question,imageView);
            gridLayout_4.addView(imageView);
        }
        for (int i = 0; i < Hyogo_QuestionList.size(); i++) {
            Question question = Hyogo_QuestionList.get(i);
            ImageView imageView = new ImageView(this);
            DisplayQuestion(question,imageView);
            gridLayout_5.addView(imageView);
        }
        for (int i = 0; i < Wakayama_QuestionList.size(); i++) {
            Question question = Wakayama_QuestionList.get(i);
            ImageView imageView = new ImageView(this);
            DisplayQuestion(question,imageView);
            gridLayout_6.addView(imageView);
        }
    }

    private void back_home(View view){
        Intent intent = new Intent(garally.this, activity_home.class);
        startActivity(intent);
    }

    private void DisplayQuestion(Question question, ImageView imageView){
        String img = question.getImg().replace("\"", "").trim();
        int imageResId = getResources().getIdentifier(img, "drawable", getPackageName());
        Glide.with(this)
                .load(imageResId)
                .override(600, 400) // 画像の解像度を指定
                .into(imageView);

        imageView.setOnClickListener(v -> {
            card.setVisibility(View.VISIBLE);
            zukan.setVisibility(View.GONE);
            String card = question.getCard().replace("\"", "").trim();
            int cardResId = getResources().getIdentifier(card, "drawable", getPackageName());
            Glide.with(garally.this)
                    .load(cardResId)
                    .into(card_image);

        });
        Log.d("card_image", "Img: "+question.getImg());
        Log.d("card_image", "card: "+question.getCard());


        if (question.getGenre_id() == 1) {
            imageView.setBackgroundColor(ContextCompat.getColor(this, R.color.food));
        } else if (question.getGenre_id() == 2) {
            imageView.setBackgroundColor(ContextCompat.getColor(this, R.color.build));
        }  else if (question.getGenre_id() == 3) {
            imageView.setBackgroundColor(ContextCompat.getColor(this, R.color.chara));
        } else if (question.getGenre_id() == 4) {
            imageView.setBackgroundColor(ContextCompat.getColor(this, R.color.place));
        } else if (question.getGenre_id() == 5) {
            imageView.setBackgroundColor(ContextCompat.getColor(this, R.color.culture));
        }

        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = (int) getResources().getDimension(R.dimen.image_width); // 100dpをピクセル単位に変換
        params.height = (int) getResources().getDimension(R.dimen.image_height); // 100dpをピクセル単位に変換
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f); // layout_columnWeight="1" に相当
        params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 2f);
        params.setGravity(Gravity.CENTER);
        imageView.setLayoutParams(params);


    }



}
