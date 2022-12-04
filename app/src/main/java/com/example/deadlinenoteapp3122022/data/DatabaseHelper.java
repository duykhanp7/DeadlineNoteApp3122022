package com.example.deadlinenoteapp3122022.data;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.room.Room;

import com.example.deadlinenoteapp3122022.data.daos.DaoNoteItem;
import com.example.deadlinenoteapp3122022.data.models.NoteItem;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {

    private DaoNoteItem daoNoteItem;
    private Context context;

    public DatabaseHelper(Context context) {
        AppDatabase appDatabase = Room.databaseBuilder(context, AppDatabase.class, "note_database").build();
        daoNoteItem = appDatabase.getDaoNoteItem();
    }

    public List<NoteItem> getAllItem() {
        List<NoteItem> notes = daoNoteItem.getAllItems();
        if (notes == null) {
            return new ArrayList<>();
        }
        return daoNoteItem.getAllItems();
    }

    public void deleteItem(NoteItem noteItem) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                daoNoteItem.deleteItem(noteItem);
            }
        }).start();
    }

    public void insertItem(NoteItem noteItem) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                daoNoteItem.insertItem(noteItem);
            }
        }).start();
    }

    public void updateItem(NoteItem noteItem) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                daoNoteItem.updateItem(noteItem);
            }
        }).start();
    }

}
