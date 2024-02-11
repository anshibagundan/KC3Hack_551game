package com.example.eemon551;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_data")
public class UserData {

    @PrimaryKey
    public int id;

    @ColumnInfo(name = "name")
    public String name;

}
