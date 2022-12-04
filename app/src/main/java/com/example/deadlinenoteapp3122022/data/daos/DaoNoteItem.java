package com.example.deadlinenoteapp3122022.data.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.deadlinenoteapp3122022.data.models.NoteItem;

import java.util.List;

@Dao
public interface DaoNoteItem {

    @Insert
    void insertItem(NoteItem noteItem);

    @Delete
    void deleteItem(NoteItem noteItem);

    @Update
    void updateItem(NoteItem noteItem);

    @Query("SELECT * FROM noteItem")
    List<NoteItem> getAllItems();

}
