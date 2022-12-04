package com.example.deadlinenoteapp3122022.broadcast;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.deadlinenoteapp3122022.R;
import com.example.deadlinenoteapp3122022.common.Config;
import com.example.deadlinenoteapp3122022.data.models.NoteItem;
import com.example.deadlinenoteapp3122022.service.NoteItemService;

public class NoteItemBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("AAA","GO TO BROAD CAST");
        Bundle bundle = intent.getBundleExtra(Config.BUNDLE);

        Intent intentService = new Intent(context, NoteItemService.class);
        intentService.putExtra(Config.BUNDLE, bundle);

        context.startService(intentService);
    }

    // TẠO THÔNG BÁO ĐẨY ĐẾN UI

}
