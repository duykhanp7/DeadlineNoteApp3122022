package com.example.deadlinenoteapp3122022;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.TimeUtils;
import androidx.databinding.DataBindingUtil;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.deadlinenoteapp3122022.broadcast.NoteItemBroadcast;
import com.example.deadlinenoteapp3122022.common.Config;
import com.example.deadlinenoteapp3122022.common.DateUtils;
import com.example.deadlinenoteapp3122022.common.FileUtils;
import com.example.deadlinenoteapp3122022.common.ToastUtils;
import com.example.deadlinenoteapp3122022.data.DatabaseHelper;
import com.example.deadlinenoteapp3122022.data.models.NoteItem;
import com.example.deadlinenoteapp3122022.databinding.ActivityAddNoteBinding;
import com.example.deadlinenoteapp3122022.databinding.LayoutChooseTimeNotifyBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.util.Calendar;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class AddNoteActivity extends AppCompatActivity {

    private ActivityAddNoteBinding activityAddNoteBinding;
    private DatabaseHelper databaseHelper;
    private BottomSheetDialog bottomSheetDialog;
    //Mode 0 - add, 1 - update
    long timeNotify = -1;
    File chooseFile = null;

    private ActivityResultLauncher<Intent> launcherGetImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                Intent intent = result.getData();
                if (intent != null) {
                    Uri uriImage = result.getData().getData();
                    if (uriImage != null) {
                        Log.i("AAA", "URI Image : " + uriImage.getPath());
                        chooseFile = FileUtils.getFile(AddNoteActivity.this, Config.tempFileName, Config.FOLDER_NAME);
                        File fileSrc = FileUtils.from(AddNoteActivity.this, uriImage);
                        FileUtils.copyFile(fileSrc,chooseFile);
                        ToastUtils.showToast(AddNoteActivity.this, "Add image successfully");
                    }
                }
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddNoteBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_note);
        databaseHelper = new DatabaseHelper(this);

        if (getIntent().getIntExtra(Config.mode, -1) == 1) {
            NoteItem noteItem = (NoteItem) Objects.requireNonNull(getIntent()).getSerializableExtra(Config.NOTE_ITEM);
            activityAddNoteBinding.inputTitle.setText(noteItem.getTitle());
            activityAddNoteBinding.inputContent.setText(noteItem.getContent());
        }

        createBottomSheetChooseTimeNotify(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu_add_note, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveNote:

                String title = activityAddNoteBinding.inputTitle.getText().toString();
                String content = activityAddNoteBinding.inputContent.getText().toString();
                if (!title.isEmpty() && !content.isEmpty()) {

                    String filePath = "";
                    String id = DateUtils.getTimeInLong();

                    if(chooseFile != null){
                        File newFile = FileUtils.rename(chooseFile,id.concat(Config.fileExtension));
                        filePath = newFile.getAbsolutePath();
                    }

                    NoteItem noteItem = new NoteItem(id, title, content, filePath, DateUtils.getCurrentDateTime());

                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra(Config.NOTE_ITEM, noteItem);

                    //mode == 0 add
                    if (getIntent().getIntExtra(Config.mode, -1) == 0) {
                        intent.putExtra(Config.mode, 0);
                        databaseHelper.insertItem(noteItem);
                        ToastUtils.showToast(this, "Add note successfully");
                        if (timeNotify != -1) {
                            createAlarm(noteItem);
                        }
                    }
                    //mode == 1 update
                    else if (getIntent().getIntExtra(Config.mode, -1) == 1) {
                        noteItem.setId(((NoteItem) Objects.requireNonNull(getIntent()).getSerializableExtra(Config.NOTE_ITEM)).getId());
                        intent.putExtra(Config.mode, 1);
                        databaseHelper.updateItem(noteItem);
                        ToastUtils.showToast(this, "Update note successfully");
                    }
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    ToastUtils.showToast(this, "Please fill up all fields");
                }
                break;
            case R.id.addAlarm:
                bottomSheetDialog.show();
                break;
            case R.id.addImage:
                openGallery();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }


    void createAlarm(NoteItem noteItem) {

        Log.i("AAA", "Note title : " + noteItem.getTitle());
        AlarmManager alarmManager = getSystemService(AlarmManager.class);
        Intent intent = new Intent(this, NoteItemBroadcast.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable(Config.NOTE_ITEM, noteItem);

        intent.putExtra(Config.BUNDLE, bundle);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, Integer.parseInt(noteItem.getId().substring(3)), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + timeNotify, pendingIntent);

    }

    void createBottomSheetChooseTimeNotify(Context context) {

        LayoutChooseTimeNotifyBinding layoutChooseTimeNotifyBinding =
                DataBindingUtil.inflate(LayoutInflater.from(context),
                        R.layout.layout_choose_time_notify, null, false);
        bottomSheetDialog = new BottomSheetDialog(this, R.style.CustomBottomSheetDialog);
        bottomSheetDialog.setContentView(layoutChooseTimeNotifyBinding.getRoot());

        bottomSheetDialog.getBehavior().setDraggable(true);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        bottomSheetDialog.setCancelable(true);

        layoutChooseTimeNotifyBinding.buttonSetTimeNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int hours = 0, minutes = 0, seconds = 0;

                String hourInput = layoutChooseTimeNotifyBinding.inputHours.getText().toString().trim();
                String minutesInput = layoutChooseTimeNotifyBinding.inputMinutes.getText().toString().trim();
                String secondsInput = layoutChooseTimeNotifyBinding.inputSeconds.getText().toString().trim();

                if (!hourInput.isEmpty() || !minutesInput.isEmpty() || !secondsInput.isEmpty()) {

                    hours = hourInput.isEmpty() ? 0 : Integer.parseInt(hourInput);
                    minutes = minutesInput.isEmpty() ? 0 : Integer.parseInt(minutesInput);
                    seconds = secondsInput.isEmpty() ? 0 : Integer.parseInt(secondsInput);

                    timeNotify = TimeUnit.HOURS.toMillis(hours) +
                            TimeUnit.MINUTES.toMillis(minutes) +
                            TimeUnit.SECONDS.toMillis(seconds);

                    ToastUtils.showToast(AddNoteActivity.this, "Hẹn thời gian thông báo thành công !");
                }

                bottomSheetDialog.dismiss();

            }
        });

        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {

            }
        });

        bottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                timeNotify = -1;
            }
        });

    }

    void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        launcherGetImage.launch(Intent.createChooser(intent, "Select image"));
    }

    @Override
    public void onBackPressed() {
        timeNotify = -1;
        chooseFile = null;
        super.onBackPressed();
    }
}