package com.sapa.solulife.Expense;

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
import android.support.v7.widget.*;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.sapa.solulife.Database.DatabaseHelper;
import com.sapa.solulife.Database.Expense;
import com.sapa.solulife.Notes.*;
import com.sapa.solulife.R;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Created by Pooja S on 9/30/2016.
 */

public class ExpenseActivity extends AppCompatActivity {

    private DrawerLayout mDrawer;

    private TextView textEmpty;
    private TextView textEmpty1, budget_amount;
    private List<Expense> notesData;
    private ExpenseAdapter expenseAdapter;
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

    Expense fingerprint,note1;

    String folderLocation;

    View revealView;
    AlertDialog.Builder dialogBuilder;
    View dialogView;
    AlertDialog alertDialog;

    Button addExpense;

    private static final String EXTRA_NOTE = "EXTRA_NOTE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        context = getApplicationContext();

        toolbar = (Toolbar) findViewById(R.id.toolbar_note);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        noResults = (FrameLayout) findViewById(R.id.framemain);
        databaseHelper = new DatabaseHelper(getApplicationContext());

        textEmpty = (TextView) findViewById(R.id.textEmpty);
        textEmpty1 = (TextView) findViewById(R.id.textEmpty1);
        textEmpty1.setVisibility(View.GONE);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.clayout);

        budget_amount = (TextView) findViewById(R.id.budget_amount);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        addExpense = (Button) findViewById(R.id.add_expense);
        addExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder = new AlertDialog.Builder(ExpenseActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                dialogView = inflater.inflate(R.layout.custom_dialog, null);
                final EditText text = (EditText)dialogView.findViewById(R.id.budget_text);
                dialogBuilder.setView(dialogView);
                dialogBuilder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseHelper.createBudget(Float.parseFloat(text.getText().toString()), 1);
                        budget_amount.setText(String.valueOf(text.getText().toString()));
                        if(databaseHelper.getBudgetStatus() == 1){
                            databaseHelper.updateBudget(Float.parseFloat(text.getText().toString()));
                            budget_amount.setText(String.valueOf(text.getText().toString()));
                        }
                    }
                });
                dialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.hide();
                    }
                });

                alertDialog = dialogBuilder.create();
                alertDialog.show();
            }
        });

        budget_amount.setText(String.valueOf(databaseHelper.getBudget()));

        recyclerView = (RecyclerView) findViewById(R.id.listNotes);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        setupNotesAdapter();
        updateView();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExpenseActivity.this, NewExpenseActivity.class);
                intent.putExtra("edit", add);
                startActivityForResult(intent, RequestResultCode.REQUEST_CODE_ADD_NOTE);
            }
        });


        sortList1(ExpenseAdapter.newestFirstComparator);

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
                final Expense note = notesData.get(position);
                getLongItemList();
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
                "Delete  Note"
        };

        new AlertDialog.Builder(this)
                .setTitle("Options")
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item){
                            case 0:
                                databaseHelper.deleteExpense(note1);
                                notesData.remove(note1);
                                updateView();
                                expenseAdapter.notifyDataSetChanged();
                                break;


                        }
                    }
                }).show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        expenseAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void sortList1(Comparator<Expense> noteComparator) {
        Collections.sort(notesData, noteComparator);
        expenseAdapter.notifyDataSetChanged();
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
        notesData = databaseHelper.getAllExpenses();
        expenseAdapter = new ExpenseAdapter(notesData, context, ExpenseActivity.this);
        recyclerView.setAdapter(expenseAdapter);
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
                expenseAdapter.notifyDataSetChanged();
            } else if (resultCode == RequestResultCode.RESULT_CODE_DELETE_NOTE) {
                deleteNote(data);
            }
        }

        if (requestCode == RequestResultCode.REQUEST_CODE_ADD_NOTE) {
            if (resultCode == RESULT_OK) {
                Expense note = (Expense) data.getSerializableExtra(EXTRA_NOTE);
                long noteId = databaseHelper.createExpenseNote(note);
                note.setId(noteId);
                notesData.add(0, note);
                updateView();
                expenseAdapter.notifyDataSetChanged();
                databaseHelper.getBudget();
                budget_amount.setText(String.valueOf(databaseHelper.getBudget()));
            } else if (resultCode == RESULT_FIRST_USER) {
                addNote(data);
            }
        }

        if(resultCode == RequestResultCode.RESULT_CODE_SEARCH){
            setupNotesAdapter();
            expenseAdapter.notifyDataSetChanged();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void addNote(Intent data) {
        Expense note = (Expense) data.getSerializableExtra(EXTRA_NOTE);
        notesData.add(0, note);
        notesData.add(note);
        updateView();
        expenseAdapter.notifyDataSetChanged();
    }

    private void updateNote(Intent data) {
        Expense updatedNote = (Expense) data.getSerializableExtra(EXTRA_NOTE);
        databaseHelper.updateExpense(updatedNote);
        for (Expense note : notesData) {
            if (note.getId().equals(updatedNote.getId())) {
                note.setTitle(updatedNote.getTitle());
                note.setNote(updatedNote.getNote());
                note.setDate(updatedNote.getDate());
                note.setAmount(updatedNote.getAmount());
                note.setBudget(updatedNote.getBudget());
            }
        }
        expenseAdapter.notifyDataSetChanged();
    }

    private void deleteNote(Intent data) {
        Expense deletedNote = (Expense) data.getSerializableExtra(EXTRA_NOTE);
        databaseHelper.deleteExpense(deletedNote);
        notesData.remove(deletedNote);
        updateView();
        expenseAdapter.notifyDataSetChanged();
        Toast.makeText(ExpenseActivity.this, "Note Deleted.", Toast.LENGTH_LONG).show();
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