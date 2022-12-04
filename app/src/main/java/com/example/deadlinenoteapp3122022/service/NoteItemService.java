package com.example.deadlinenoteapp3122022.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.deadlinenoteapp3122022.R;
import com.example.deadlinenoteapp3122022.common.Config;
import com.example.deadlinenoteapp3122022.data.models.NoteItem;

public class NoteItemService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("AAA","GO TO SERVICE");
        Bundle bundle = intent.getBundleExtra(Config.BUNDLE);

        NoteItem noteItem = (NoteItem) bundle.getSerializable(Config.NOTE_ITEM);

        createNotification(noteItem, getApplicationContext());

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public void createNotification(NoteItem noteItem, Context context) {

        //TẠO THÔNG BÁO
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "1111")
                .setSmallIcon(R.drawable.ic_add_note)
                .setContentTitle(noteItem.getTitle())
                .setContentText(noteItem.getContent())
                .setPriority(NotificationCompat.PRIORITY_MAX)// thử dùng NotificationCompatManager và NotificationManager????
                .setCategory(NotificationCompat.CATEGORY_ALARM) // thử dùng CATEGORY_CALL và các các khác thử xem thế nào???
                .setAutoCancel(true)
                .setOngoing(false);

        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.notify(Integer.parseInt(noteItem.getId().substring(3)),builder.build());

    }

}
