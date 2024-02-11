package com.example.eemon551;

import androidx.room.Database;
import androidx.room.RoomDatabase;

// データベースエンティティクラスなどを指定
@Database(entities = {eemon_db.class}, version = 1)
abstract class AppDatabase extends RoomDatabase {
    public abstract eemon_db_dao eemonDbDao();
}
