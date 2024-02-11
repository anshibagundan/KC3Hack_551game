package com.example.eemon551;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update; // 追加

import java.util.List;

@Dao
public interface eemon_db_dao {
    @Insert
    void insertAll(eemon_db... eemonDbs);

    @Update
    void update(eemon_db eemonDb);

    @Delete
    void delete(eemon_db eemonDb);

    @Query("SELECT * FROM eemon_db")
    List<eemon_db> getAll();
}
