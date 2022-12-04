package com.example.deadlinenoteapp3122022;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.deadlinenoteapp3122022.data.DatabaseHelper;
import com.example.deadlinenoteapp3122022.data.models.NoteItem;

import java.util.List;

public class MainViewModel extends ViewModel {

    private DatabaseHelper databaseHelper;
    private Context context;

    public MutableLiveData<List<NoteItem>> mutableLiveDataNotes = new MutableLiveData<>();


    public void getAllNoteItems(Context context){
        this.context = context;
        new Thread(new Runnable() {
            @Override
            public void run() {
                databaseHelper = new DatabaseHelper(context);
                mutableLiveDataNotes.postValue(databaseHelper.getAllItem());
            }
        }).start();
    }

    public void insertItem(NoteItem noteItem){
        databaseHelper.insertItem(noteItem);
    }

    public void deleteItem(NoteItem noteItem){
        databaseHelper.deleteItem(noteItem);
    }

    public void updateItem(NoteItem noteItem){
        databaseHelper.updateItem(noteItem);
    }

}
