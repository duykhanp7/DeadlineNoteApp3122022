package com.example.deadlinenoteapp3122022.data.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = "noteItem")
public class NoteItem implements Serializable {

    @NonNull
    @PrimaryKey
    String id;
    @ColumnInfo
    String title;
    @ColumnInfo
    String content;
    @ColumnInfo
    String imagePath;
    @ColumnInfo
    String dateCreated;

    public NoteItem(String id, String title, String content, String imagePath, String dateCreated) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.imagePath = imagePath;
        this.dateCreated = dateCreated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }
}
