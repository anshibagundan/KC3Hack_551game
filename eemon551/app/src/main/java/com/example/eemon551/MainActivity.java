
package com.example.eemon551;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // HTTPリクエストを送信するAsyncTaskを開始する
        new HttpRequestTask().execute();
    }

    // AsyncTaskを使用してHTTPリクエストを送信するクラス
    private class HttpRequestTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                // HTTPクライアントを作成する
                OkHttpClient client = new OkHttpClient();

                // リクエストオブジェクトを作成する
                Request request = new Request.Builder()
                        .url("https://eemon-551.onrender.com/api/Question/")  // Django REST APIのエンドポイントURLを指定する
                        .build();

                // リクエストを送信し、レスポンスを取得する
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                Log.e("HTTP Request", "Error: " + e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (response != null) {
                try {
                    // JSONレスポンスをパースしてユーザー情報を取得する
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String username = jsonObject.getString("username");
                        Log.d("User", "Username: " + username);
                        // ここで取得したユーザー情報をUIに表示するなどの処理を行う
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
