package com.example.android.inventoryapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BookDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "books.db";

    private static final int DATABASE_VERSION = 1;//If you change database schema you need to update the version

    public BookDbHelper(Context context){
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_BOOKS_TABLE =
                "CREATE TABLE " + BookContract.BookEntry.TABLE_NAME + " ("
                + BookContract.BookEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BookContract.BookEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
                + BookContract.BookEntry.COLUMN_PRICE + " INTEGER NOT NULL, "
                + BookContract.BookEntry.COLUMN_QUANTITY + " INTEGER NOT NULL, "
                + BookContract.BookEntry.COLUMN_SUPPLIER_NAME + " TEXT, "
                + BookContract.BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER + " TEXT);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_BOOKS_TABLE);
    }
}
