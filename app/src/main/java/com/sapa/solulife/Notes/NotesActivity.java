package com.sapa.solulife.Notes;
/**
 * Created by prasad on 2/22/2015.
 * Updated by prasad on 6/28/2015
 */

import android.app.NotificationManager;
import android.content.*;
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
import android.view.*;


import android.widget.*;
import com.sapa.solulife.R;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class NotesActivity extends AppCompatActivity {

    private DrawerLayout mDrawer;

    private TextView textEmpty;
    private TextView textEmpty1;
    private List<Note> notesData;
    private NotesAdapter notesAdapter;
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

    Note fingerprint,note1;

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

                    recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        

        setupNotesAdapter();
        updateView();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotesActivity.this, EditNoteActivity.class);
                intent.putExtra("edit", add);
                startActivityForResult(intent, RequestResultCode.REQUEST_CODE_ADD_NOTE);
                //overridePendingTransition(R.anim.slide_in_child_bottom, R.anim.slide_out);
            }
        });
        
       
                sortList1(NotesAdapter.newestFirstComparator);
               

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
                final Note note = notesData.get(position);
                
                    Intent intentn = new Intent(NotesActivity.this, ViewNoteCollapseActivity.class);
                    intentn.putExtra(EXTRA_NOTE, note);
                    startActivityForResult(intentn, RequestResultCode.REQUEST_CODE_VIEW_NOTE);
                    //overridePendingTransition(R.anim.slide_in_child_bottom, R.anim.slide_out);
                    intentn.putExtra(STAGGER_CONTENT, note);
                
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
                        databaseHelper.deleteNote(note1);
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

    public void sortList1(Comparator<Note> noteComparator) {
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
        notesData = databaseHelper.getAllNotes();
        notesAdapter = new NotesAdapter(notesData, context, NotesActivity.this);
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
                Note note = (Note) data.getSerializableExtra(EXTRA_NOTE);
                long noteId = databaseHelper.createNote(note);
                note.setId(noteId);
                if (sortl.equals("Newest First") || sortl.equals("Sort By Title(Aescending)") || sortl.equals("Sort By Title(Descending)")) {
                    notesData.add(0, note);
                } else {
                    notesData.add(note);
                }
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
        Note note = (Note) data.getSerializableExtra(EXTRA_NOTE);
        if (sortl.equals("Newest First") || sortl.equals("Sort By Title(Aescending)") || sortl.equals("Sort By Title(Descending)")) {
            notesData.add(0, note);
        } else {
            notesData.add(note);
        }
        updateView();
        notesAdapter.notifyDataSetChanged();
    }

    private void updateNote(Intent data) {
        Note updatedNote = (Note) data.getSerializableExtra(EXTRA_NOTE);
        databaseHelper.updateNote(updatedNote);
        for (Note note : notesData) {
            if (note.getId().equals(updatedNote.getId())) {
                note.setTitle(updatedNote.getTitle());
                note.setContent(updatedNote.getContent());
                note.setUpdatedAt(updatedNote.getUpdatedAt());
                note.setColor(updatedNote.getColor());
                note.setFavourite(updatedNote.getFavourite());
                note.setLock_status(updatedNote.getLock_status());
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
        Toast.makeText(NotesActivity.this, "Note Deleted.", Toast.LENGTH_LONG).show();
    }

}