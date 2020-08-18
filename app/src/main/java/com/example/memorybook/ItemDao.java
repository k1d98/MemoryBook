package com.example.memorybook;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ItemDao {
    //from user items
    @Query("SELECT * FROM item")
    List<Item> getAll();

    @Query("SELECT * FROM item WHERE id =:id")
    Item getOne(int id);

    @Insert
    void insertOne(Item item);

    @Delete
    void deleteOne(Item item);

    @Query("DELETE FROM item")
    void deleteAll();

}
