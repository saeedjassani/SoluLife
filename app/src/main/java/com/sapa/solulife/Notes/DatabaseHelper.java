package com.sapa.solulife.Notes;

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

import java.io.StringReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String LOG = DatabaseHelper.class.getSimpleName();
    private static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "NotesDB";
    public SQLiteDatabase database;

    private final Context context;

    DatabaseHelper databaseHelper;

    public static final String TABLE_NOTES = "notes";
    public static final String TABLE_PASSCODE = "passcodetable";
    public static final String TABLE_DEFAULT_COLOR = "defaultcolortable";

    public static final String KEY_ID = "id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_UPDATED_AT = "updated_at";
    public static final String KEY_COLOR = "color";
    public static final String KEY_TAG = "tag";
    public static final String KEY_FAVOURITE = "favourite";

    public static final String KEY_LOCKSTATUS = "lock_status";
    public static final String KEY_PASSCODE_ID = "passcode_id";
    public static final String KEY_PASSOCDE = "passcode";
    public static final String KEY_PASSCODE_STATUS = "passcode_status";

    public static final String KEY_REMINDER_TIME = "reminder_time";
    public static final String KEY_REMINDER_DATE = "reminder_date";
    public static final String KEY_REMINDER_STATUS = "reminder_status";

    public static final String KEY_DEFAULTCOLOR_ID = "default_color_id";
    public static final String KEY_DEFAULT_COLOR = "default_color";

    private static final String CREATE_TABLE_NOTE = "CREATE TABLE "
            + TABLE_NOTES + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_TITLE + " TEXT, "
            + KEY_CONTENT + " TEXT, "
            + KEY_UPDATED_AT + " TEXT, "
            + KEY_COLOR + " INT, "
            + KEY_FAVOURITE + " INT" + " )";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("onCreate", "onCreate");
        try {
            db.execSQL(CREATE_TABLE_NOTE);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    public void onDowngrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2) {
        onUpgrade(paramSQLiteDatabase, paramInt1, paramInt2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

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

    public List<Note> getFavouriteNotes() {
        List<Note> result = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NOTES + " WHERE " + KEY_FAVOURITE + " == 1";
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

    public void updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, note.getTitle());
        values.put(KEY_CONTENT, note.getContent());
        values.put(KEY_UPDATED_AT, note.getUpdatedAt());
        values.put(KEY_COLOR, note.getColor());
        values.put(KEY_FAVOURITE, note.getFavourite());
        db.update(TABLE_NOTES, values, KEY_ID + " = ?", new String[]{String.valueOf(note.getId())});
    }

    public void deleteNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTES, KEY_ID + " = ?", new String[]{String.valueOf(note.getId())});
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