package com.example.eemon551;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;

import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

public class DataInsert {

    private static final String DATABASE_NAME = "eemon_db";

    public static void main(String[] args) {
        // 複数のeemon_dbエンティティをリストに格納
        List<eemon_db> eemonDbs = new ArrayList<>();
        eemon_db eemonDb1 = new eemon_db();
        eemonDb1.name = "名前1";
        eemonDb1.img = "画像1";
        eemonDb1.txt = "テキスト1";
        eemonDb1.link = "リンク1";
        eemonDb1.location = "場所1";
        eemonDb1.location_img = "場所画像1";
        eemonDb1.iskansai = true;
        eemonDb1.genre_name = "ジャンル名1";
        eemonDb1.genre_color = 000000;
        eemonDb1.isCorrect = true;

        eemon_db eemonDb2 = new eemon_db();
        eemonDb2.name = "名前2";
        eemonDb2.img = "画像2";
        eemonDb2.txt = "テキスト2";
        eemonDb2.link = "リンク2";
        eemonDb2.location = "場所2";
        eemonDb2.location_img = "場所画像2";
        eemonDb2.iskansai = false;
        eemonDb2.genre_name = "ジャンル名2";
        eemonDb2.genre_color = 000000;
        eemonDb2.isCorrect = false;

        eemonDbs.add(eemonDb1);
        eemonDbs.add(eemonDb2);

        Context context = getApplicationContext();
        AppDatabase database = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME)
                .build();

        // DAOのinsertAllメソッドを利用して複数データを挿入
        eemon_db_dao dao = database.eemonDbDao();
        dao.insertAll(eemonDbs.toArray(new eemon_db[0]));

        database.close();
    }

    private static Context getApplicationContext() {
        return MyApplication.getInstance().getApplicationContext();
    }

}
