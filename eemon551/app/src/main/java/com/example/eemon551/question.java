package com.example.eemon551;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "question") // テーブル名の指定
public class Question { // Javaの規約ではクラス名はキャメルケースで大文字始まり

    @PrimaryKey // プライマリキー
    public int qes_id;

    @ColumnInfo(name = "name") // カラム名の指定
    public String name; // Javaの規約ではフィールド名は小文字始まり

    @ColumnInfo(name = "img")
    public String img;

    @ColumnInfo(name = "txt")
    public String txt;

    @ColumnInfo(name = "link")
    public String link;

    @ColumnInfo(name = "location")
    public String location;

    @ColumnInfo(name = "location_img")
    public String location_img;

    @ColumnInfo

}
