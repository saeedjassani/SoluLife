package com.sapa.solulife.Notes;

/**
 * Created by Pooja S on 9/30/2016.
 */
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.sapa.solulife.R;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class EditNoteActivity extends AppCompatActivity {

    private static final String EXTRA_NOTE = "EXTRA_NOTE";
    private ActionMode mActionMode;
    public EditText editTitle;
    public TextInputLayout inputlayoutTitle,inputlayoutContent;
    public EditText editContent;
    private Note note;
    private TextView editNote;

    Toolbar toolbar;
    private Boolean fabdrwble;
    private List<Note> notesData;
    private DatabaseHelper databaseHelper;
    SystemBarTintManager systemBarTintManager;

    private Boolean save;
    private FloatingActionButton saveButton;
    private static final int SPEECH_REQUEST_CODE = 0;
    private ColorStateList tint;

    ColorGenerator colorGenerator = ColorGenerator.MATERIAL;
    int selectedColor;
    int preselect = colorGenerator.getRandomColor();

    LinearLayout ll;
    LinearLayout ll1;

    Long mRowId;

    Spinner spinner;
    String spinnertext;
    boolean amoled;

    int fav;

    boolean mSharedFromIntentFilter = false;
    private static final DateFormat DATETIME_FORMAT = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        toolbar = (Toolbar) findViewById(R.id.toolbar_edit);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseHelper = new DatabaseHelper(getApplicationContext());

        inputlayoutTitle = (TextInputLayout) findViewById(R.id.inputlayoutTitle);
        inputlayoutContent = (TextInputLayout) findViewById(R.id.inputlayoutContent);
        editTitle = (EditText) findViewById(R.id.note_title);
        editContent = (EditText) findViewById(R.id.note_content);

        Bundle bundle = getIntent().getExtras();
        String s = bundle.getString("edit");

        if(s.equals("add")){
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }else if(s.equals("editv")){
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }

        note = (Note) getIntent().getSerializableExtra(EXTRA_NOTE);
        if (note != null) {
            editTitle.setText(note.getTitle());
            editContent.setText(note.getContent());
            note.getColor();
        } else {
            note = new Note();
            note.setUpdatedAt(DATETIME_FORMAT.format(new Date()));
        }

        saveButton = (FloatingActionButton) findViewById(R.id.add_edit_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNoteFormOk()) {
                    setNoteResult();
                    //overridePendingTransition(R.anim.slide_in_child_bottom, R.anim.slide_out);
                    finish();
                } else
                    validateNoteForm();
            }
        });

        saveButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(getApplicationContext(), "Save Note", Toast.LENGTH_SHORT).show();
                return true;
            }
        });


        ll = (LinearLayout) findViewById(R.id.llmain);
        ll1 = (LinearLayout) findViewById(R.id.ll1);

        if(note.getColor() == Color.TRANSPARENT){
            selectedColor = preselect;
        }else {
            selectedColor = note.getColor();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        systemBarTintManager = new SystemBarTintManager(this);
        systemBarTintManager.setStatusBarTintEnabled(true);
        ll.setBackgroundColor(selectedColor);
        ll1.setBackgroundColor(selectedColor);
        toolbar.setBackgroundColor(note.getColor());
        systemBarTintManager.setStatusBarTintColor(selectedColor);

    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case android.R.id.home:
                onBack();
                return true;

            case R.id.speech:
                try {
                    displaySpeechRecognizer();
                } catch (ActivityNotFoundException e) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://market.android.com/details?id=com.google.android.googlequicksearchbox"));
                    startActivity(browserIntent);
                }
                return true;

            default: return super.onOptionsItemSelected(item);
        }
    }

    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            editContent.setText(spokenText);
        }
        if (requestCode == RequestResultCode.REQUEST_CODE_ADD_NOTE) {
            if (resultCode == RESULT_OK) {
                addNote(data);
            }
        }
    }


    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    private boolean isNoteFormOk() {
        String title=editTitle.getText().toString();
        return !(title==null || title.trim().length()==0);
    }

    private void validateNoteForm() {
        String msg=null;
        if (isNullOrBlank(editTitle.getText().toString())){
            msg="Title";
            inputlayoutTitle.setError("Title is Missing");
        }
        if (msg!=null){
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        }
    }

    private void setNoteResult() {
        note.setTitle(editTitle.getText().toString().trim());
        note.setContent(editContent.getText().toString().trim());
        note.setUpdatedAt(DATETIME_FORMAT.format(new Date()));
        note.setColor(selectedColor);
        Intent intent=new Intent();
        intent.putExtra(EXTRA_NOTE, note);
        setResult(RESULT_OK, intent);
        //addNote(intent);

        Toast.makeText(EditNoteActivity.this, "Note Saved.", Toast.LENGTH_LONG).show();
    }

    private void onBack(){
            if (isNoteFormOk()) {
                if ((editTitle.getText().toString().equals(note.getTitle())) && (editContent.getText().toString().equals(note.getContent())) && selectedColor == note.getColor()) {
                    setResult(RESULT_CANCELED, new Intent());
                    finish();
                }else {
                    setNoteResult();
                    finish();
                }
            } else {
                setResult(RESULT_CANCELED, new Intent());
                finish();
            }
    }

    private void addNote(Intent data) {
        Note note = (Note) data.getSerializableExtra(EXTRA_NOTE);
        long noteId = databaseHelper.createNote(note);
        note.setId(noteId);
    }

    @Override
    public void onBackPressed() {
        onBack();
        Intent intentHome = new Intent(EditNoteActivity.this, NotesActivity.class);
        intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intentHome.putExtra(EXTRA_NOTE, note);
        setResult(RESULT_OK, intentHome);
    }

    public static boolean isNullOrBlank(String str) {
        return str == null || str.trim().length() == 0;
    }

}
