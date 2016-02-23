package com.github.javalabs.androidlab4.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper implements BaseColumns {

    private static final String DATABASE_NAME = "lab4.db";
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_TABLE_TEMPLATE = "templates";
    public static final String DATABASE_TABLE_TOP = "tops";

    public static final String MESSAGE_COLUMN = "message"; // Для шаблона

    public static final String TEXT_COLUMN = "text"; // для последних отправленных
    public static final String PHONE_COLUMN = "phone";


    private static final String DATABASE_CREATE_SCRIPT_1 = "create table "
            + DATABASE_TABLE_TOP + " (" + BaseColumns._ID
            + " integer primary key autoincrement, "
            + TEXT_COLUMN + " text not null, "
            + PHONE_COLUMN + " text not null"
            + ");";
    private static final String DATABASE_CREATE_SCRIPT_2 = "create table "
            + DATABASE_TABLE_TEMPLATE + " (" + BaseColumns._ID
            + " integer primary key autoincrement, "
            + MESSAGE_COLUMN + " text not null"
            + ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                          int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                          int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_SCRIPT_1);
        db.execSQL(DATABASE_CREATE_SCRIPT_2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Запишем в журнал
        Log.w("SQLite", "Обновляемся с версии " + oldVersion + " на версию " + newVersion);

        // Удаляем старую таблицу и создаём новую
        db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_TABLE_TOP);
        db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_TABLE_TEMPLATE);
        // Создаём новую таблицу
        onCreate(db);
    }
}