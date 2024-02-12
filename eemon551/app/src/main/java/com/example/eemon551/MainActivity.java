package com.example.eemon551;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call; // Callをインポート
import retrofit2.Callback; // Callbackをインポート
import retrofit2.Response; // Responseをインポート


public class MainActivity extends AppCompatActivity {
//
//    private AppDatabase database;
//    private TextView textView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        textView = findViewById(R.id.app_name); // TextView を取得
//
//        // 複数のeemon_dbエンティティをリストに格納
//        List<eemon_db> eemonDbs = new ArrayList<>();
//        eemon_db eemonDb1 = new eemon_db();
//        eemonDb1.qes_id = 1;
//        eemonDb1.name = "名前1";
//        eemonDb1.img = "画像1";
//        eemonDb1.txt = "テキスト1";
//        eemonDb1.link = "リンク1";
//        eemonDb1.location = "場所1";
//        eemonDb1.location_img = "場所画像1";
//        eemonDb1.iskansai = true;
//        eemonDb1.genre_name = "ジャンル名1";
//        eemonDb1.genre_color = 0; // 色の値を修正
//        eemonDb1.isCorrect = true;
//
//
//        eemon_db eemonDb2 = new eemon_db();
//        eemonDb2.qes_id = 2;
//        eemonDb2.name = "名前2";
//        eemonDb2.img = "画像2";
//        eemonDb2.txt = "テキスト2";
//        eemonDb2.link = "リンク2";
//        eemonDb2.location = "場所2";
//        eemonDb2.location_img = "場所画像2";
//        eemonDb2.iskansai = false;
//        eemonDb2.genre_name = "ジャンル名2";
//        eemonDb2.genre_color = 0;
//        eemonDb2.isCorrect = false;
//
//        eemon_db eemonDb3 = new eemon_db();
//        eemonDb3.qes_id = 3;
//        eemonDb3.name = "名前3";
//        eemonDb3.img = "画像3";
//        eemonDb3.txt = "テキスト3";
//        eemonDb3.link = "リンク3";
//        eemonDb3.location = "場所3";
//        eemonDb3.location_img = "場所画像3";
//        eemonDb3.iskansai = false;
//        eemonDb3.genre_name = "ジャンル名3";
//        eemonDb3.genre_color = 0;
//        eemonDb3.isCorrect = false;
//
//        eemonDbs.add(eemonDb1);
//        eemonDbs.add(eemonDb2);
//        eemonDbs.add(eemonDb3);
//
//        // データベースへの接続を構築
//        database = Room.databaseBuilder(this, AppDatabase.class, "eemon_db").build();
//
//
//        // AsyncTaskを利用してデータベースへの挿入処理を実行
//        new InsertAsyncTask(database, eemonDbs).execute();
//
//    }
//
//    private class InsertAsyncTask extends AsyncTask<Void, Void, Void> {
//
//        private AppDatabase database;
//        private List<eemon_db> eemonDbs;
//
//        public InsertAsyncTask(AppDatabase database, List<eemon_db> eemonDbs) {
//            this.database = database;
//            this.eemonDbs = eemonDbs;
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            // DAOのinsertAllメソッドを利用して複数データを挿入
//            eemon_db_dao dao = database.eemonDbDao();
//            if(dao == null){
//                dao.insertAll(eemonDbs.toArray(new eemon_db[0]));
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            // データベースへの接続を閉じる
//            database.close();
//
//            // データベースからデータを取得して TextView に表示
//            new RetrieveAsyncTask(database).execute();
//        }
//    }
//
//    private class RetrieveAsyncTask extends AsyncTask<Void, Void, List<eemon_db>> {
//
//        private AppDatabase database;
//
//        public RetrieveAsyncTask(AppDatabase database) {
//            this.database = database;
//        }
//
//        @Override
//        protected List<eemon_db> doInBackground(Void... voids) {
//            // DAOのgetAllメソッドを利用して全データを取得
//            return database.eemonDbDao().getAll();
//        }
//
//        @Override
//        protected void onPostExecute(List<eemon_db> eemonDbs) {
//            // 取得したデータをログに出力
//            StringBuilder stringBuilder = new StringBuilder();
//            for (eemon_db eemonDb : eemonDbs) {
//                stringBuilder.append(eemonDb.name).append("\n"); // 追加
//            }
//            String text = stringBuilder.toString();
//
//            // TextView に表示
//            textView.setText(text);
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TextViewを取得
        TextView textView = findViewById(R.id.app_name);

        // ApiServiceインスタンスを取得
        ApiService apiService = ApiClient.getApiService();

        // APIリクエストを実行
        apiService.getAllQuestions().enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    // 最初の質問のnameを取得してTextViewにセット
                    String name = response.body().get(0).getName();
                    textView.setText(name);
                }
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                // エラーハンドリング
                Log.e("MainActivity", "APIリクエスト失敗: ", t);
            }
        });
    }
}
