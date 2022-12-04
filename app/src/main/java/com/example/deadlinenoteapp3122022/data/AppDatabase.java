package com.example.deadlinenoteapp3122022.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.deadlinenoteapp3122022.data.daos.DaoNoteItem;
import com.example.deadlinenoteapp3122022.data.models.NoteItem;

@Database(entities = {NoteItem.class},version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DaoNoteItem getDaoNoteItem();
}
