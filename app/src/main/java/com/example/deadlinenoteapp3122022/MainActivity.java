package com.example.deadlinenoteapp3122022;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.deadlinenoteapp3122022.common.Config;
import com.example.deadlinenoteapp3122022.common.ToastUtils;
import com.example.deadlinenoteapp3122022.data.DatabaseHelper;
import com.example.deadlinenoteapp3122022.data.models.NoteItem;
import com.example.deadlinenoteapp3122022.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements OnItemClick{

    private ActivityMainBinding activityMainBinding;
    private NoteItemAdapter noteItemAdapter = new NoteItemAdapter(new ArrayList<>(), this);
    private ItemTouchHelper.SimpleCallback simpleCallback;
    public static final String TAG = "AAA";
    private MainViewModel mainViewModel;


    public ActivityResultLauncher<Intent> launcher= registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK){
                Intent intent = result.getData();
                if(intent != null){
                    NoteItem noteItem = (NoteItem) Objects.requireNonNull(intent).getSerializableExtra(Config.NOTE_ITEM);
                    if(intent.getIntExtra(Config.mode,-1) == 0){
                        if (noteItem != null){
                            Log.i(MainActivity.TAG,"DMMMMMMMMM ADD: "+noteItem.getId()+" - "+noteItem.getTitle()+" - "+noteItem.getContent()+" - "+noteItem.getDateCreated());
                            noteItemAdapter.addItem(noteItem);
                        }
                    }
                    else if(intent.getIntExtra(Config.mode,-1) == 1){
                        if (noteItem != null){
                            Log.i(MainActivity.TAG,"DMMMMMMMMM UPDATE: "+noteItem.getId()+" - "+noteItem.getTitle()+" - "+noteItem.getContent()+" - "+noteItem.getDateCreated());
                            noteItemAdapter.updateItem(noteItem);
                        }
                    }
                }
                else{
                    ToastUtils.showToast(MainActivity.this, "Insert fail");
                }
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.app_name);


        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        activityMainBinding.recyclerViewNotes.setHasFixedSize(true);
        activityMainBinding.recyclerViewNotes.setLayoutManager(linearLayoutManager);
        activityMainBinding.recyclerViewNotes.setAdapter(noteItemAdapter);

        mainViewModel.mutableLiveDataNotes.observe(this, new Observer<List<NoteItem>>() {
            @Override
            public void onChanged(List<NoteItem> noteItems) {
                noteItemAdapter.addItems(noteItems);
            }
        });


        mainViewModel.getAllNoteItems(this);

        simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if(direction == ItemTouchHelper.LEFT){
                    int position = viewHolder.getAdapterPosition();
                    NoteItem noteItem = noteItemAdapter.getNoteItem(position);
                    mainViewModel.deleteItem(noteItem);
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            noteItemAdapter.removeItem(position);
                        }
                    },1000);
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(activityMainBinding.recyclerViewNotes);

        createNotificationChannel();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options_menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.information:
                Log.i(TAG, "Show information");
                break;
            case R.id.addNote:
                Log.i(TAG, "Add note");
                Intent intentAddNote = new Intent(MainActivity.this, AddNoteActivity.class);
                intentAddNote.putExtra(Config.mode,0);
                launcher.launch(intentAddNote);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }


    void launchResultUpdate(NoteItem noteItem){
        Intent intentAddNote = new Intent(MainActivity.this, AddNoteActivity.class);
        intentAddNote.putExtra(Config.mode,1);
        intentAddNote.putExtra(Config.NOTE_ITEM, noteItem);
        launcher.launch(intentAddNote);
    }

    public void createNotificationChannel() {
        NotificationChannel notificationChannel = new NotificationChannel("1111", "channel", NotificationManager.IMPORTANCE_HIGH);
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(notificationChannel);
    }

    @Override
    public void updateItem(NoteItem noteItem) {
        launchResultUpdate(noteItem);
    }
}