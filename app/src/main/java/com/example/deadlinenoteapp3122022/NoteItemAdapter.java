package com.example.deadlinenoteapp3122022;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deadlinenoteapp3122022.common.Config;
import com.example.deadlinenoteapp3122022.data.models.NoteItem;
import com.example.deadlinenoteapp3122022.databinding.ItemNoteBinding;

import java.io.File;
import java.util.List;

public class NoteItemAdapter extends RecyclerView.Adapter<NoteItemAdapter.ViewHolder> {

    private List<NoteItem> noteItemList;
    public OnItemClick onItemClick;


    NoteItemAdapter(List<NoteItem> notes, OnItemClick onItemClick1){
        this.noteItemList = notes;
        this.onItemClick = onItemClick1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNoteBinding itemNoteBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.item_note,parent,false);
        return new ViewHolder(itemNoteBinding, onItemClick);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NoteItem noteItem = noteItemList.get(position);
        holder.bindData(noteItem);
    }

    @Override
    public int getItemCount() {
        return noteItemList.size();
    }


    NoteItem getNoteItem(int pos){
        return noteItemList.get(pos);
    }

    void removeItem(int pos){
        noteItemList.remove(pos);
        notifyItemRemoved(pos);
    }

    void addItem(NoteItem noteItem){
        int size = noteItemList.size();
        noteItemList.add(size, noteItem);
        notifyDataSetChanged();
        //notifyItemInserted(size);
    }

    void updateItem(NoteItem noteItem){
        for (int i = 0; i < noteItemList.size(); i++) {
            if(noteItem.getId().equals(noteItemList.get(i).getId())){
                noteItemList.get(i).setTitle(noteItem.getTitle());
                noteItemList.get(i).setContent(noteItem.getContent());
                noteItemList.get(i).setDateCreated(noteItem.getDateCreated());
                notifyItemChanged(i);
                break;
            }
        }
    }

    void addItems(List<NoteItem> noteItems){
        int size = noteItemList.size();
        noteItemList.addAll(noteItems);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ItemNoteBinding itemNoteBinding;
        private NoteItem noteItem;
        private OnItemClick onItemClick;

        public ViewHolder(@NonNull ItemNoteBinding itemNoteBinding, OnItemClick onItemClick1) {
            super(itemNoteBinding.getRoot());
            this.itemNoteBinding = itemNoteBinding;
            this.onItemClick = onItemClick1;
        }

        void bindData(NoteItem noteItem){

            this.noteItem = noteItem;
            itemNoteBinding.title.setText(noteItem.getTitle());
            itemNoteBinding.dateCreated.setText(noteItem.getDateCreated());
            itemNoteBinding.content.setText(noteItem.getContent());
            itemNoteBinding.getRoot().setOnClickListener(this);

            if(!noteItem.getImagePath().trim().isEmpty()){
                File file = new File(noteItem.getImagePath());
                if(file.exists()){
                    Log.i("AAA","Load image success");
                    itemNoteBinding.parentImage.setVisibility(View.VISIBLE);
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    itemNoteBinding.noteItemImage.setImageBitmap(bitmap);
                }
                else{
                    Log.i("AAA","Load image fail");
                }
            }
            else{
                Log.i("AAA","Load image eeeeeeeeeeeeeee");
            }

        }

        @Override
        public void onClick(View view) {
            onItemClick.updateItem(noteItem);
        }
    }

}


