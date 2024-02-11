package com.example.eemon551;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class QuestionActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        textView = findViewById(R.id.text_question_name);

        // データベース接続
        String dbUrl = "jdbc:postgresql://dpg-cn3h3cv109ks73epvbhg-a:5432/eemon";
        String dbUsername = "eemon_user";
        String dbPassword = "xBcSex0Q4EVEJZf6FAeBtjHJlLdy7xbu";

        try (Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            // Statementの作成
            Statement statement = connection.createStatement();

            // SQLクエリの実行
            ResultSet resultSet = statement.executeQuery("SELECT * FROM \"question\"");

            // データの取得
            StringBuilder resultText = new StringBuilder();
            while (resultSet.next()) {
                resultText.append(resultSet.getString("\"name\"")).append("\n"); // 改行追加
            }

            // TextViewへの表示
            if (resultText.length() > 0) {
                textView.setText(resultText.toString());
            }else{
                textView.setText("接続エラー");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            textView.setText("接続エラー");
        }
    }
}

