package com.sapa.solulife.Reminders;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.sapa.solulife.Database.DatabaseHelper;
import com.sapa.solulife.Notes.*;
import com.sapa.solulife.R;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Pooja S on 10/1/2016.
 */

public class EditReminder extends AppCompatActivity {

    private static final String EXTRA_NOTE = "EXTRA_NOTE";
    private ActionMode mActionMode;
    public EditText editTitle;
    public TextInputLayout inputlayoutTitle,inputlayoutContent;
    public EditText editContent;
    private Reminder note;
    private TextView editNote, reminderDateText, reminderTimeText;

    Toolbar toolbar;
    private Boolean fabdrwble;
    private List<Reminder> notesData;
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
    private int mDay;
    private int mHour;
    private int mMinute;
    private int mMonth;
    private int mYear;
    Menu menu;
    String myDate;
    String myTime;
    Spinner spinner;
    String spinnertext;
    boolean amoled;

    int fav;

    boolean mSharedFromIntentFilter = false;
    private static final DateFormat DATETIME_FORMAT = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reminder);

        toolbar = (Toolbar) findViewById(R.id.toolbar_edit);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseHelper = new DatabaseHelper(getApplicationContext());

        inputlayoutTitle = (TextInputLayout) findViewById(R.id.inputlayoutTitle);
        inputlayoutContent = (TextInputLayout) findViewById(R.id.inputlayoutContent);
        editTitle = (EditText) findViewById(R.id.note_title);
        editContent = (EditText) findViewById(R.id.note_content);

        reminderDateText = (TextView) findViewById(R.id.reminderDate);
        reminderTimeText = (TextView) findViewById(R.id.reminderTime);

        Bundle bundle = getIntent().getExtras();
        String s = bundle.getString("edit");

        if(s.equals("add")){
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }else if(s.equals("editv")){
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }

        note = (Reminder) getIntent().getSerializableExtra(EXTRA_NOTE);
        if (note != null) {
            editTitle.setText(note.getTitle());
            editContent.setText(note.getContent());
            note.getColor();
        } else {
            note = new Reminder();
            note.setUpdatedAt(DATETIME_FORMAT.format(new Date()));
            note.setReminderDate(note.getReminderDate());
            note.setReminderTime(note.getReminderTime());
            note.setReminderStatus(note.getReminderStatus());
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
        getMenuInflater().inflate(R.menu.menu_edit_reminder, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case android.R.id.home:
                onBack();
                return true;

            case R.id.reminder:
                setReminder();
                return true;

            default: return super.onOptionsItemSelected(item);
        }
    }

    private void setReminder() {
        Calendar localCalendar = Calendar.getInstance();
        this.mYear = localCalendar.get(Calendar.YEAR);
        this.mMonth = localCalendar.get(Calendar.MONTH);
        this.mDay = localCalendar.get(Calendar.DAY_OF_MONTH);
        this.mHour = localCalendar.get(Calendar.HOUR_OF_DAY);
        this.mMinute = localCalendar.get(Calendar.MINUTE);
        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener()
        {
            public void onTimeSet(TimePicker paramTimePicker, int paramInt1, int paramInt2)
            {
                reminderTimeText.setText(paramInt1 + ":" + paramInt2);
            }
        }
                , this.mHour, this.mMinute, false).show();
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener()
        {
            public void onDateSet(DatePicker paramDatePicker, int paramInt1, int paramInt2, int paramInt3)
            {
                reminderDateText.setText(paramInt3 + "-" + (paramInt2 + 1) + "-" + paramInt1);
            }
        }, this.mYear, this.mMonth, this.mDay).show();
    }

    public void validateReminderDateTime()
    {
        String date = reminderDateText.getText().toString();
        String time = reminderTimeText.getText().toString();
        String[] dateArray = date.split("-");
        int day = Integer.parseInt(dateArray[0]);
        int month = Integer.parseInt(dateArray[1]) - 1;
        int year = Integer.parseInt(dateArray[2]);

        String[] timeArray = time.split(":");
        int hour = Integer.parseInt(timeArray[0]);
        int minutes = Integer.parseInt(timeArray[1]);


        Calendar current = Calendar.getInstance();

        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day, hour, minutes, 00);

        if (cal.compareTo(current) <= 0) {
            //The set Date/Time already passed
            Toast.makeText(getApplicationContext(),
                    "Invalid Date/Time",
                    Toast.LENGTH_LONG).show();
        } else {
            setAlarm(cal, note);
        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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

    private void setAlarm(Calendar paramCalendar, Reminder paramNote)
    {
        Date localDate = paramCalendar.getTime();
        Toast.makeText(getApplicationContext(), "Reminder Set For " + DATETIME_FORMAT.format(localDate), Toast.LENGTH_SHORT).show();
        int i = (int)System.currentTimeMillis();
        Intent localIntent = new Intent(getBaseContext(), ReceiverService.class);
        localIntent.putExtra("title", paramNote.getTitle());
        localIntent.putExtra("content", paramNote.getContent());
        localIntent.putExtra("color", paramNote.getColor());
        localIntent.putExtra("code", i);
        PendingIntent localPendingIntent = PendingIntent.getBroadcast(getBaseContext(), i, localIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, paramCalendar.getTimeInMillis(), localPendingIntent);

    }

    private void setNoteResult() {
        note.setTitle(editTitle.getText().toString().trim());
        note.setContent(editContent.getText().toString().trim());
        note.setUpdatedAt(DATETIME_FORMAT.format(new Date()));
        note.setColor(selectedColor);
        note.setReminderDate(reminderDateText.getText().toString());
        note.setReminderTime(reminderTimeText.getText().toString());
        note.setReminderStatus(1);
        Intent intent=new Intent();
        intent.putExtra(EXTRA_NOTE, note);
        setResult(RESULT_OK, intent);
        //addNote(intent);

        Toast.makeText(EditReminder.this, "Reminder Saved.", Toast.LENGTH_LONG).show();
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
        Intent intentHome = new Intent(EditReminder.this, NotesActivity.class);
        intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intentHome.putExtra(EXTRA_NOTE, note);
        setResult(RESULT_OK, intentHome);
    }

    public static boolean isNullOrBlank(String str) {
        return str == null || str.trim().length() == 0;
    }

}
