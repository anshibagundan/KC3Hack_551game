package com.example.eemon551;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "location")
public class Location {

    @PrimaryKey
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "font")
    public String font;

    @ColumnInfo(name = "img")
    public String img;

    @ColumnInfo(name = "iskansai")
    public boolean isKansai;

}
