package com.example.eemon551;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class Connect_SQL {

    public static void main(String[] args) throws SQLException {
        // 接続情報の準備
        String host = "dpg-cn3h3cv109ks73epvbhg-a";
        int port = 5432;
        String database = "eemon";
        String user = "eemon_user";
        String password = "xBcSex0Q4EVEJZf6FAeBtjHJlLdy7xbu";

        // 接続
        Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://" + host + ":" + port + "/" + database,
                user,
                password
        );

        // 接続成功
        System.out.println("接続成功");

        // 接続を閉じる
        connection.close();
    }
}
