package com.example.sites.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Sitio.class},version = 1)
public abstract class SitioDatabase extends RoomDatabase {

    public abstract SitiDao getSitioDAO();
}
