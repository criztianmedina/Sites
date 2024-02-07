package com.example.sites.database;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.room.Room;

import java.util.List;

public class SitioLab {

    @SuppressLint("StaticFieldLeak")
    private static SitioLab sSitioLab;

    private SitiDao mSitioDao;
    public SitioLab(Context context){
        Context appContext=context.getApplicationContext();
        SitioDatabase database=
                Room.databaseBuilder(
                                appContext,SitioDatabase.class,"sitio")
                        .allowMainThreadQueries().build();
        mSitioDao=database.getSitioDAO();
    }
    public static SitioLab get(Context context){
        if (sSitioLab==null){
            sSitioLab=new SitioLab(context);
        }
        return sSitioLab;
    }
    public List<Sitio> getSitios(){

        return  mSitioDao.getSitios();
    }

    public Sitio getSitio(String id){

        return mSitioDao.getSitio(id);
    }

    public void addSitio(Sitio sitio){

        mSitioDao.insertSitio(sitio);

    }
    public void deleteSitio(Sitio sitio){
        mSitioDao.deleteSitio(sitio);
    }
    public void updateSitio(Sitio sitio){

        mSitioDao.updateSitio(sitio);
    }
}
