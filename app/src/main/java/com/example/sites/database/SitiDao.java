package com.example.sites.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface SitiDao {

    @Query("select * from sitio")
    List<Sitio> getSitios();

    @Query("select * from sitio where id LIKE:uuid")
    Sitio getSitio(String uuid);

    //insert,update,delete
    @Insert
    void insertSitio(Sitio sitio);

    @Update
    void updateSitio(Sitio sitio);
    @Delete
    void deleteSitio(Sitio sitio);
}
