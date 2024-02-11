package com.example.eemon551;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_questiondata")
public class UserQuestionData {

    @PrimaryKey(autoGenerate = true) //複合主キーは現在Room非対応のため、単一主キーにしてautoGenerate付与
    public int id;

    @ColumnInfo(name = "user_data_id")
    public int userDataId;

    @ColumnInfo(name = "isCorrect") // cor は予約後になってる場合があるため変更
    public boolean isCorrect;

    @ColumnInfo(name = "qes_id")
    public int questionId;

    @ForeignKey(
            entity = UserData.class,
            parentColumns = "id",
            childColumns = "userDataId",
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
    )
    public int userDataIdForeignKey;  //外部キー制約用のフィールド。Java側の関連設定はこのように書く

    @ForeignKey(
            entity = Question.class,
            parentColumns = "id",
            childColumns = "questionId",
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
    )
    public int questionIdForeignKey; //外部キー制約用のフィールド
}
