package com.android.example.wordlistsql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class WordListOpenHelper extends SQLiteOpenHelper {

    private static final String TAG = WordListOpenHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "wordlist";
    private static final int DATABASE_VERSION = 1;
    private static final String WORD_LIST_TABLE = "word_entries";

    public static final String KEY_ID = "_id";
    public static final String KEY_WORD = "word";

    private static final String WORD_LIST_TABLE_CREATE =
            "CREATE TABLE " + WORD_LIST_TABLE + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_WORD + " TEXT NOT NULL);";

    private SQLiteDatabase mWritableDB;
    private SQLiteDatabase mReadableDB;

    public WordListOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(WORD_LIST_TABLE_CREATE);
        fillDatabaseWithData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private void fillDatabaseWithData(SQLiteDatabase db) {
        String[] words = {"Android", "Adapter", "ListView", "SQLiteDatabase", "RecyclerView"};
        ContentValues values = new ContentValues();
        for (String word : words) {
            values.put(KEY_WORD, word);
            db.insert(WORD_LIST_TABLE, null, values);
        }
    }

    public long insert(String word) {
        ContentValues values = new ContentValues();
        values.put(KEY_WORD, word);
        return getWritableDatabase().insert(WORD_LIST_TABLE, null, values);
    }

    public int delete(int id) {
        return getWritableDatabase().delete(WORD_LIST_TABLE, KEY_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public int update(int id, String word) {
        ContentValues values = new ContentValues();
        values.put(KEY_WORD, word);
        return getWritableDatabase().update(WORD_LIST_TABLE, values, KEY_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public long count() {
        return DatabaseUtils.queryNumEntries(getReadableDatabase(), WORD_LIST_TABLE);
    }

    public WordItem query(int position) {
        Cursor cursor = getReadableDatabase().query(WORD_LIST_TABLE, null, null, null, null, null, KEY_WORD + " ASC", position + ",1");
        WordItem entry = new WordItem();
        if (cursor.moveToFirst()) {
            entry.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
            entry.setWord(cursor.getString(cursor.getColumnIndexOrThrow(KEY_WORD)));
        }
        cursor.close();
        return entry;
    }
    public Cursor search(String searchString) {
        if (mReadableDB == null) {
            mReadableDB = getReadableDatabase();
        }

        String[] columns = new String[]{KEY_WORD};
        searchString = "%" + searchString + "%"; // Buscar coincidencias parciales
        String where = KEY_WORD + " LIKE ?";
        String[] whereArgs = new String[]{searchString};

        Cursor cursor = null;
        try {
            cursor = mReadableDB.query(
                    WORD_LIST_TABLE, columns, where, whereArgs,
                    null, null, KEY_WORD + " ASC");
        } catch (Exception e) {
            Log.e("WordListOpenHelper", "Error en la búsqueda: " + e.getMessage());
        }
        return cursor;
    }

}
