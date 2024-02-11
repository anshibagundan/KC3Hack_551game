package com.example.eemon551;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class QuestionActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        textView = findViewById(R.id.text_question_name);
        textView.setText('1');

        // 非同期処理でデータ取得
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> future = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Connection connection = null;
                StringBuilder resultText = new StringBuilder();
                textView.setText('1');
                try {
                    // データベース接続
                    connection = DriverManager.getConnection(getString(R.string.db_url), getString(R.string.db_username), getString(R.string.db_password));

                    // Statementの作成
                    Statement statement = connection.createStatement();

                    // SQLクエリの実行
                    ResultSet resultSet = statement.executeQuery("SELECT * FROM question");

                    // データの表示
                    textView.setText(1);
                    while (resultSet.next()) {
                        textView.append(resultSet.getString("name"));
                        textView.setText(1);
                    }

                } catch (SQLException e) {
                    return e.toString();
                } finally {
                    if (connection != null) {
                        try {
                            connection.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return resultText.toString();
            }
        });

        // データ取得結果の処理
        executorService.shutdown();
        try {
            String result = future.get();
            textView.setText(result);
        } catch (Exception e) {
            textView.setText(e.toString());
        }
    }
}