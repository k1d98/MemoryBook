package com.example.memorybook;

import android.content.Context;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Item.class}, version = 8, exportSchema = false)
public abstract class ItemDatabase extends RoomDatabase {

    public abstract ItemDao getItemDao();

    private static ItemDatabase itemDB;

    private static ItemDatabase buildDatabaseInstance(Context context){
        //allowmainthreadQueries is not recommended to be used on real apps. It's recommended to be used AsyncTask
        return Room.databaseBuilder(context, ItemDatabase.class, "itemsdb.db").allowMainThreadQueries().fallbackToDestructiveMigration().build();
    }

    public static ItemDatabase getInstance(Context context){
        if(itemDB == null) itemDB = buildDatabaseInstance(context);

        return itemDB;
    }
    public void cleanUp(){
        itemDB = null;
    }


}
