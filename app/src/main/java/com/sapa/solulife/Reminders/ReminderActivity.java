package com.sapa.solulife.Reminders;

import android.app.NotificationManager;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.sapa.solulife.Database.DatabaseHelper;
import com.sapa.solulife.Notes.*;
import com.sapa.solulife.R;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Created by Pooja S on 10/1/2016.
 */

public class ReminderActivity extends AppCompatActivity {

    private DrawerLayout mDrawer;

    private TextView textEmpty;
    private TextView textEmpty1;
    private List<Reminder> notesData;
    private ReminderAdapter notesAdapter;
    private SearchView searchView;
    private MenuItem searchMenuItem;
    private static Context context;
    private DatabaseHelper databaseHelper;

    TextToSpeech tts;
    String ttsContent;


    NotificationManager mNotificationManager;
    int mNotificationId = 001;
    int mmNotificationId = 002;
    int mmnNotificationId = 003;

    public Toolbar toolbar;
    boolean homePressed = false;

    private final static String MENU_SELECTED = "selected";
    private int selected = -1;

    private RecyclerView recyclerView;

    private Boolean fabdrwble;
    private Boolean grid1;
    private Boolean bottombar;
    private Boolean scroll;
    private Boolean swipe;
    private Boolean quickl;
    private Boolean notifyd;
    Boolean dark;
    Boolean amoled;

    private static final int REQUEST_STORAGE_PERMISSION = 0;

    String notes = "notes";
    String notes1 = "notes1";

    int exportTextPos;

    FloatingActionButton fab;

    CoordinatorLayout coordinatorLayout;

    LinearLayout ll1;

    public static FrameLayout noResults;

    String STAGGER_CONTENT = "EXTRA CONTENT";
    String sortl;
    String add = "add";

    Reminder fingerprint,note1;

    String folderLocation;

    View revealView;

    private static final String EXTRA_NOTE = "EXTRA_NOTE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        context = getApplicationContext();

        toolbar = (Toolbar) findViewById(R.id.toolbar_note);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        noResults = (FrameLayout) findViewById(R.id.framemain);
        databaseHelper = new DatabaseHelper(getApplicationContext());

        textEmpty = (TextView) findViewById(R.id.textEmpty);
        textEmpty.setText("No Notes");
        textEmpty1 = (TextView) findViewById(R.id.textEmpty1);
        textEmpty1.setVisibility(View.GONE);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.clayout);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        recyclerView = (RecyclerView) findViewById(R.id.listNotes);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        setupNotesAdapter();
        updateView();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReminderActivity.this, EditNoteActivity.class);
                intent.putExtra("edit", add);
                startActivityForResult(intent, RequestResultCode.REQUEST_CODE_ADD_NOTE);
                //overridePendingTransition(R.anim.slide_in_child_bottom, R.anim.slide_out);
            }
        });


        sortList1(ReminderAdapter.newestFirstComparator);


        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.UK);
                }
            }
        });

        setListeners();
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    private void setListeners() {
        NotesAdapter.setOnItemClickListener(new NotesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                final Reminder note = notesData.get(position);

                new AlertDialog.Builder(ReminderActivity.this)
                        .setTitle("Reminder")
                        .setMessage("Do you want to Cancel the Reminder?")
                        .setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                note.setReminderTime("");
                                note.setReminderDate("");
                                note.setReminderStatus(0);
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();

            }
        });

        NotesAdapter.setOnLongItemClickListener(new NotesAdapter.OnLongItemClickListener() {
            @Override
            public void onLongItemClick(final int position, View v) {
                note1 = notesData.get(position);
                exportTextPos = position;
                fingerprint = notesData.get(position);
                getLongItemList();
            }
        });
    }

    public void getLongItemList(){

        final CharSequence[] items = {
                "Delete  Note", "Listen Content", "Copy Content"
        };

        new AlertDialog.Builder(this)
                .setTitle("Make your selection")
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item){
                            case 0:
                                databaseHelper.deleteReminder(note1);
                                notesData.remove(note1);
                                updateView();
                                notesAdapter.notifyDataSetChanged();
                                break;

                            case 1:
                                ttsContent = note1.getContent();
                                tts.speak(ttsContent, 0, null);
                                break;

                            case 2:

                                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                                clipboard.setText("Title:\n" + note1.getTitle() + "\nContent:\n" + note1.getContent());
                                Snackbar.make(coordinatorLayout, "Note Copied", Snackbar.LENGTH_SHORT).show();

                                break;
                        }
                    }
                }).show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        notesAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void sortList1(Comparator<Reminder> noteComparator) {
        Collections.sort(notesData, noteComparator);
        notesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(MENU_SELECTED, selected);
        super.onSaveInstanceState(savedInstanceState);
    }

    public static Context getContext() {
        return context;
    }

    private void setupNotesAdapter() {
        notesData = databaseHelper.getReminderNotes();
        notesAdapter = new ReminderAdapter(notesData, context, ReminderActivity.this);
        recyclerView.setAdapter(notesAdapter);
    }

    private void updateView() {
        if (notesData.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            textEmpty.setVisibility(View.VISIBLE);
            textEmpty1.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            textEmpty.setVisibility(View.GONE);
            textEmpty1.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestResultCode.REQUEST_CODE_VIEW_NOTE) {
            if (resultCode == RESULT_OK) {
                updateNote(data);
                notesAdapter.notifyDataSetChanged();
            } else if (resultCode == RequestResultCode.RESULT_CODE_DELETE_NOTE) {
                deleteNote(data);
            }
        }

        if (requestCode == RequestResultCode.REQUEST_CODE_ADD_NOTE) {
            if (resultCode == RESULT_OK) {
                Reminder note = (Reminder) data.getSerializableExtra(EXTRA_NOTE);
                long noteId = databaseHelper.createReminder(note);
                note.setId(noteId);
                notesData.add(0, note);
                updateView();
                notesAdapter.notifyDataSetChanged();
            } else if (resultCode == RESULT_FIRST_USER) {
                addNote(data);
            }
        }

        if(resultCode == RequestResultCode.RESULT_CODE_SEARCH){
            setupNotesAdapter();
            notesAdapter.notifyDataSetChanged();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void addNote(Intent data) {
        Reminder note = (Reminder) data.getSerializableExtra(EXTRA_NOTE);
        notesData.add(0, note);
        updateView();
        notesAdapter.notifyDataSetChanged();
    }

    private void updateNote(Intent data) {
        Reminder updatedNote = (Reminder) data.getSerializableExtra(EXTRA_NOTE);
        databaseHelper.updateReminder(updatedNote);
        for (Reminder note : notesData) {
            if (note.getId().equals(updatedNote.getId())) {
                note.setTitle(updatedNote.getTitle());
                note.setUpdatedAt(updatedNote.getUpdatedAt());
                note.setColor(updatedNote.getColor());
                note.setReminderDate(updatedNote.getReminderDate());
                note.setReminderTime(updatedNote.getReminderTime());
                note.setReminderStatus(updatedNote.getReminderStatus());
            }
        }
        notesAdapter.notifyDataSetChanged();
    }

    private void deleteNote(Intent data) {
        Note deletedNote = (Note) data.getSerializableExtra(EXTRA_NOTE);
        databaseHelper.deleteNote(deletedNote);
        notesData.remove(deletedNote);
        updateView();
        notesAdapter.notifyDataSetChanged();
        Toast.makeText(ReminderActivity.this, "Note Deleted.", Toast.LENGTH_LONG).show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                super.onBackPressed();
                break;
        }
        return true;
    }

}