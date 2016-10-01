package com.sapa.solulife.Database;

/**
 * Created by Pooja S on 9/30/2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.sapa.solulife.Notes.Note;
import com.sapa.solulife.Reminders.Reminder;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String LOG = DatabaseHelper.class.getSimpleName();
    private static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "NotesDB";
    public SQLiteDatabase database;

    private final Context context;

    DatabaseHelper databaseHelper;

    public static final String TABLE_NOTES = "notes";
    public static final String TABLE_EXPENSE = "expense";
    public static final String TABLE_BUDGET = "budget";
    public static final String TABLE_REMINDER = "reminder";

    public static final String KEY_ID = "id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_UPDATED_AT = "updated_at";
    public static final String KEY_COLOR = "color";
    public static final String KEY_FAVOURITE = "favourite";

    public static final String KEY_EXPENSE_ID = "expense_id";
    public static final String KEY_EXPENSE_TITLE = "expense_title";
    public static final String KEY_EXPENSE_NOTE = "expense_note";
    public static final String KEY_AMOUNT = "expense_amount";
    public static final String KEY_EXPENSE_DATE = "expense_date";

    public static final String KEY_BUDGET_ID = "expense_budget_id";
    public static final String KEY_BUDGET = "expense_budget";
    public static final String KEY_BUDGET_STATUS = "expense_budget_status";

    public static final String KEY_REMINDER_DATE = "reminder_date";
    public static final String KEY_REMINDER_STATUS = "reminder_status";
    public static final String KEY_REMINDER_TIME = "reminder_time";

    private static final String CREATE_TABLE_REMINDER = "CREATE TABLE "
            + TABLE_NOTES + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_TITLE + " TEXT, "
            + KEY_CONTENT + " TEXT, "
            + KEY_UPDATED_AT + " TEXT, "
            + KEY_COLOR + " INT, "
            + KEY_REMINDER_DATE + " TEXT, "
            + KEY_REMINDER_TIME+" TEXT, "
            + KEY_REMINDER_STATUS+" INT"+" )";

    private static final String CREATE_TABLE_NOTE = "CREATE TABLE "
            + TABLE_NOTES + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_TITLE + " TEXT, "
            + KEY_UPDATED_AT + " TEXT, "
            + KEY_COLOR + " INT, "
            + KEY_FAVOURITE + " INT" + " )";

    private static final String CREATE_TABLE_EXPENSE = "CREATE TABLE "
            + TABLE_EXPENSE + "(" + KEY_EXPENSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_EXPENSE_TITLE + " TEXT, "
            + KEY_EXPENSE_NOTE + " TEXT, "
            + KEY_EXPENSE_DATE + " TEXT, "
            + KEY_AMOUNT + " INT" + " )";

    private static final String CREATE_TABLE_EXPENSE_BUDGET = "CREATE TABLE "
            + TABLE_BUDGET + "(" + KEY_BUDGET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_BUDGET + " INT"+" )";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("onCreate", "onCreate");
        try {
            db.execSQL(CREATE_TABLE_NOTE);
            db.execSQL(CREATE_TABLE_REMINDER);
            db.execSQL(CREATE_TABLE_EXPENSE);
            db.execSQL(CREATE_TABLE_EXPENSE_BUDGET);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    public void onDowngrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2) {
        onUpgrade(paramSQLiteDatabase, paramInt1, paramInt2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public long createNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, note.getTitle());
        values.put(KEY_CONTENT, note.getContent());
        values.put(KEY_UPDATED_AT, note.getUpdatedAt());
        values.put(KEY_COLOR, note.getColor());
        values.put(KEY_FAVOURITE, note.getFavourite());
        return db.insert(TABLE_NOTES, null, values);
    }

    public long createReminder(Reminder note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, note.getTitle());
        values.put(KEY_UPDATED_AT, note.getUpdatedAt());
        values.put(KEY_COLOR, note.getColor());
        values.put(KEY_REMINDER_DATE, note.getReminderDate());
        values.put(KEY_REMINDER_TIME, note.getReminderTime());
        values.put(KEY_REMINDER_STATUS, note.getReminderStatus());
        return db.insert(TABLE_REMINDER, null, values);
    }

    public long createExpenseNote(Expense expense){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_EXPENSE_TITLE, expense.getTitle());
        values.put(KEY_EXPENSE_NOTE, expense.getNote());
        values.put(KEY_EXPENSE_DATE, expense.getDate());
        values.put(KEY_AMOUNT, expense.getAmount());
        return db.insert(TABLE_EXPENSE, null, values);
    }

    public void createBudget(float budget){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(KEY_BUDGET_ID, 1);
        values.put(KEY_BUDGET, budget);
        db.insert(TABLE_BUDGET, null, values);
    }

    public float getBudget(){
        float result=0;
        SQLiteDatabase db=this.getReadableDatabase();
        String selectQuery="SELECT "+KEY_BUDGET+" FROM "+ TABLE_BUDGET+" WHERE "+KEY_BUDGET_ID+" = 1";
        Log.e(LOG, selectQuery);
        Cursor cursor=db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            do {
                result = cursor.getFloat(cursor.getColumnIndex(KEY_BUDGET));
            }while (cursor.moveToNext());
        }
        return result;
    }

    public void updateBudget(float expense) {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(KEY_BUDGET_ID, 1);
        values.put(KEY_BUDGET, expense);
        db.update(TABLE_BUDGET, values, KEY_BUDGET_ID + " = ?", new String[]{String.valueOf(1)});
    }

    public void deleteBudget(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+TABLE_BUDGET+" WHERE "+KEY_BUDGET_ID+ " = 1");
    }

    public List<Note> getAllNotes() {
        List<Note> result = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NOTES;
        Log.e(LOG, selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Note note = new Note();
                    note.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
                    note.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
                    note.setContent(cursor.getString(cursor.getColumnIndex(KEY_CONTENT)));
                    note.setUpdatedAt(cursor.getString(cursor.getColumnIndex(KEY_UPDATED_AT)));
                    note.setColor(cursor.getInt(cursor.getColumnIndex(KEY_COLOR)));
                    note.setFavourite(cursor.getInt(cursor.getColumnIndex(KEY_FAVOURITE)));
                    result.add(note);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }

    public List<Reminder> getReminderNotes() {
        List<Reminder> result = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_REMINDER;
        Log.e(LOG, selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Reminder note = new Reminder();
                    note.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
                    note.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
                    note.setUpdatedAt(cursor.getString(cursor.getColumnIndex(KEY_UPDATED_AT)));
                    note.setColor(cursor.getInt(cursor.getColumnIndex(KEY_COLOR)));
                    note.setReminderDate(cursor.getString(cursor.getColumnIndex(KEY_REMINDER_DATE)));
                    note.setReminderTime(cursor.getString(cursor.getColumnIndex(KEY_REMINDER_TIME)));
                    note.setReminderStatus(cursor.getInt(cursor.getColumnIndex(KEY_BUDGET_STATUS)));
                    result.add(note);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }

    public List<Expense> getAllExpenses() {
        List<Expense> result = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_EXPENSE;
        Log.e(LOG, selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Expense expense = new Expense();
                    expense.setId(cursor.getLong(cursor.getColumnIndex(KEY_EXPENSE_ID)));
                    expense.setTitle(cursor.getString(cursor.getColumnIndex(KEY_EXPENSE_TITLE)));
                    expense.setNote(cursor.getString(cursor.getColumnIndex(KEY_EXPENSE_NOTE)));
                    expense.setDate(cursor.getString(cursor.getColumnIndex(KEY_EXPENSE_DATE)));
                    expense.setAmount(cursor.getFloat(cursor.getColumnIndex(KEY_AMOUNT)));
                    result.add(expense);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }

    public void updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, note.getTitle());
        values.put(KEY_CONTENT, note.getContent());
        values.put(KEY_UPDATED_AT, note.getUpdatedAt());
        values.put(KEY_COLOR, note.getColor());
        values.put(KEY_FAVOURITE, note.getFavourite());
        db.update(TABLE_REMINDER, values, KEY_ID + " = ?", new String[]{String.valueOf(note.getId())});
    }

    public void updateReminder(Reminder note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, note.getTitle());
        values.put(KEY_UPDATED_AT, note.getUpdatedAt());
        values.put(KEY_COLOR, note.getColor());
        values.put(KEY_REMINDER_DATE, note.getReminderDate());
        values.put(KEY_REMINDER_TIME, note.getReminderTime());
        values.put(KEY_REMINDER_STATUS, note.getReminderStatus());
        db.update(TABLE_NOTES, values, KEY_ID + " = ?", new String[]{String.valueOf(note.getId())});
    }

    public void updateExpense(Expense expense) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_EXPENSE_TITLE, expense.getTitle());
        values.put(KEY_EXPENSE_NOTE, expense.getNote());
        values.put(KEY_EXPENSE_DATE, expense.getDate());
        values.put(KEY_AMOUNT, expense.getAmount());
        db.update(TABLE_EXPENSE, values, KEY_EXPENSE_ID + " = ?", new String[]{String.valueOf(expense.getId())});
    }

    public void deleteNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTES, KEY_ID + " = ?", new String[]{String.valueOf(note.getId())});
    }

    public void deleteReminder(Reminder note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_REMINDER, KEY_ID + " = ?", new String[]{String.valueOf(note.getId())});
    }

    public void deleteExpense(Expense expense) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EXPENSE, KEY_EXPENSE_ID + " = ?", new String[]{String.valueOf(expense.getId())});
    }

    public void reset() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTES, null, null);
        db.close();
    }

    @Override
    public synchronized void close() {

        if (database != null)
            database.close();

        super.close();

    }
}