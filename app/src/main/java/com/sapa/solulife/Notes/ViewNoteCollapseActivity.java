package com.sapa.solulife.Notes;

/**
 * Created by Pooja S on 9/30/2016.
 */
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.*;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.sapa.solulife.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by prasad on 1/8/2016.
 */

public class ViewNoteCollapseActivity extends AppCompatActivity {

    private static final String EXTRA_NOTE = "EXTRA_NOTE";

    private TextView textUpdated;
    private TextView textContent,textTag;
    private FloatingActionButton editButton;
    private CardView cardView;
    private Note note;
    TextToSpeech tts;
    private List<Note> notesData;

    NotesAdapter notesAdapter;
    SharedPreferences preferences;
    NotificationManager mNotificationManager;
    int mNotificationId = 001;
    private DatabaseHelper databaseHelper;

    Boolean dark;
    Boolean cardedit;
    boolean amoled;
    String ttsContent;
    String titlen;
    CollapsingToolbarLayout collapsingToolbar;
    Toolbar toolbar;
    CoordinatorLayout coordinatorLayout;

    String folderLocation;

    int fav;
    int lock_status = 0;
    boolean navpreff;
    String edit = "editv";

    private static DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
    private static DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collapselayout);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        notesData = databaseHelper.getAllNotes();

        note = (Note) getIntent().getSerializableExtra(EXTRA_NOTE);

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        collapsingToolbar.setTitle(note.getTitle());
        collapsingToolbar.setExpandedTitleColor(Color.WHITE);

        toolbar = (Toolbar) findViewById(R.id.toolbar_collapse);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.clayoutcl);

        databaseHelper = new DatabaseHelper(getApplicationContext());

        textContent = (TextView) findViewById(R.id.note_content);
        textUpdated = (TextView) findViewById(R.id.textUpdated);
        textUpdated.setText(note.getUpdatedAt());

        textContent.setMovementMethod(LinkMovementMethod.getInstance());

        SpannableString hashText = new SpannableString(note.getContent());
        Matcher matcher = Pattern.compile("#([A-Za-z0-9_-]+)").matcher(hashText);
        while (matcher.find()){
            hashText.setSpan(new ForegroundColorSpan(Color.parseColor("#FF5722")),matcher.start(),matcher.end(),0);
        }
        textContent.setText(hashText);
        ttsContent = textContent.getText().toString();
        titlen = collapsingToolbar.getTitle().toString();

        /*URLSpan[] sdad = textContent.getUrls();
        for(URLSpan url: sdad){
            //asdjbasdbah(url.getURL());
        }*/

        cardView = (CardView) findViewById(R.id.content);
        cardView.setCardBackgroundColor(getResources().getColor(R.color.cardWhite));

        editButton = (FloatingActionButton) findViewById(R.id.edit_note_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editNote();
                //overridePendingTransition(R.anim.slide_in_child_bottom, R.anim.slide_out);
            }
        });

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.ENGLISH);
                }
            }
        });

        preferences = getSharedPreferences("MYPREFS", MODE_PRIVATE);
        int type = preferences.getInt("FONT", 2);

        if(type == 1){
            textContent.setTextSize(14);
        }else if(type == 2){
            textContent.setTextSize(18);
        }else if(type == 3){
            textContent.setTextSize(22);
        }else if(type == 4){
            textContent.setTextSize(24);
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        collapsingToolbar.setBackgroundColor(note.getColor());
        toolbar.setBackgroundColor(note.getColor());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("ViewNoteActivity", "onActivityResult");
        if (requestCode== RequestResultCode.REQUEST_CODE_EDIT_NOTE){
            if (resultCode==RESULT_OK){
                Log.d("ViewNoteActivity", "RESULT_OK");
                note = (Note) data.getSerializableExtra(EXTRA_NOTE);
                collapsingToolbar.setTitle(note.getTitle());
                textContent.setText(note.getContent());
                collapsingToolbar.setBackgroundColor(note.getColor());
                toolbar.setBackgroundColor(note.getColor());
                note.getFavourite();
            }
        }
        if (resultCode == RESULT_OK) {
            updateNote(data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_note, menu);

        if(note.getFavourite() == 1) {
            menu.findItem(R.id.favourite).setIcon(R.drawable.home);
            return true;
        }
        menu.findItem(R.id.favourite).setIcon(R.drawable.home);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.favourite:
                if(note.getFavourite() == 0){
                    fav = 1;
                    note.setFavourite(fav);
                    item.setIcon(getResources().getDrawable(R.drawable.home));
                    Toast.makeText(ViewNoteCollapseActivity.this, "Note Added to Favorite", Toast.LENGTH_SHORT).show();
                }else if(note.getFavourite() == 1){
                    fav = 0;
                    note.setFavourite(fav);
                    item.setIcon(getResources().getDrawable(R.drawable.home));
                    Toast.makeText(ViewNoteCollapseActivity.this, "Note Removed to Favorite", Toast.LENGTH_SHORT).show();
                }
                return true;

            case R.id.delete_note:
                deleteNote();
                return true;

            case R.id.share_note:
                shareNote();
                return true;

            case R.id.copy_note:
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                clipboard.setText(textContent.getText());
                Toast.makeText(getApplicationContext(),"Content Copied",Toast.LENGTH_SHORT).show();
                return true;

            case R.id.read:
                tts.speak(ttsContent,0, null);
                return true;

            case R.id.submenu1:
                if(item.isChecked() == false) {
                    item.setChecked(true);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("FONT", 1);
                    editor.apply();
                }else{
                    item.setChecked(false);
                }
                recreate();
                return true;

            case R.id.submenu2:
                if(!item.isChecked()) {
                    item.setChecked(true);
                    SharedPreferences.Editor editor1 = preferences.edit();
                    editor1.putInt("FONT", 2);
                    editor1.apply();
                }else{
                    item.setChecked(false);
                }
                recreate();
                return true;

            case R.id.submenu3:
                if(!item.isChecked()) {
                    item.setChecked(true);
                    SharedPreferences.Editor editor2 = preferences.edit();
                    editor2.putInt("FONT", 3);
                    editor2.commit();
                }else{
                    item.setChecked(false);
                }
                recreate();
                return true;

            case R.id.submenu4:
                if(!item.isChecked()) {
                    item.setChecked(true);
                    SharedPreferences.Editor editor3 = preferences.edit();
                    editor3.putInt("FONT", 4);
                    editor3.commit();
                }else{
                    item.setChecked(false);
                }
                recreate();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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
            }
        }
    }

    public void onPause(){
        super.onPause();
        if(tts !=null){
            tts.stop();
            tts.shutdown();
        }
        Intent intentHome = new Intent(this, ViewNoteCollapseActivity.class);
        intentHome.setFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        intentHome.putExtra(EXTRA_NOTE, note);
        setResult(RESULT_OK, intentHome);
    }

    @Override
    public void onBackPressed() {
        Intent intentHome = new Intent(this, NotesActivity.class);
        intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intentHome.putExtra(EXTRA_NOTE, note);
        setResult(RESULT_OK, intentHome);
        finish();
    }

    private void editNote(){
        Intent intent=new Intent(ViewNoteCollapseActivity.this, EditNoteActivity.class);
        intent.putExtra(EXTRA_NOTE, note);
        intent.putExtra("edit",edit);
        startActivityForResult(intent, RequestResultCode.REQUEST_CODE_EDIT_NOTE);
    }

    public void deleteNote() {
        new AlertDialog.Builder(this)
                .setTitle("Delete")
                .setMessage("Do You Want to Delete the Note?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intentHome = new Intent(ViewNoteCollapseActivity.this,NotesActivity.class);
                        intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intentHome.putExtra(EXTRA_NOTE, note);
                        setResult(RequestResultCode.RESULT_CODE_DELETE_NOTE, intentHome);
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();

    }

    private void shareNote(){
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT,"Title: "+note.getTitle()+"\nContent: "+note.getContent());
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent,"Share Via"));
    }

}
