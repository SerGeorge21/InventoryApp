package com.example.android.inventoryapp;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class BookProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int BOOKS = 100;

    private static final int BOOKS_ID = 101;

    static{

        sUriMatcher.addURI(BookContract.CONTENT_AUTHORITY, BookContract.PATH_BOOKS, BOOKS);
        sUriMatcher.addURI(BookContract.CONTENT_AUTHORITY, BookContract.PATH_BOOKS + "/#", BOOKS_ID);
    }

    public static final String LOG_TAG = BookProvider.class.getSimpleName();

    private BookDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new BookDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        //Get readable Database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        //This cursor will hold the result of the query
        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch(match){
            case BOOKS:
                cursor = database.query(BookContract.BookEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case BOOKS_ID:
                selection = BookContract.BookEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};

                cursor = database.query(BookContract.BookEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI" + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch(match){
            case BOOKS:
                return BookContract.BookEntry.CONTENT_LIST_TYPE;
            case BOOKS_ID:
                return BookContract.BookEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Uknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch(match){
            case BOOKS:
                return insertBook(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertBook(Uri uri, ContentValues contentValues){

        //Check that name not null
        String name = contentValues.getAsString(BookContract.BookEntry.COLUMN_PRODUCT_NAME);
        if(name == null){
            throw new IllegalArgumentException("Book requires a name");
        }
        //Check that price is not null or negative number
        Integer price = contentValues.getAsInteger(BookContract.BookEntry.COLUMN_PRICE);
        if(price == null || price < 0){
            throw new IllegalArgumentException("Book requires valid price");
        }
        Integer quantity = contentValues.getAsInteger(BookContract.BookEntry.COLUMN_QUANTITY);
        if(quantity < 0 ){
            throw new IllegalArgumentException("Book requires non negative quantity");
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(BookContract.BookEntry.TABLE_NAME, null, contentValues);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        //notify all listeners that the data has changed in the content URI
        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int rowsDeleted;
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        switch (match){
            case BOOKS:
                rowsDeleted = database.delete(BookContract.BookEntry.TABLE_NAME, selection, selectionArgs);
                if(rowsDeleted!=0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            case BOOKS_ID:
                selection = BookContract.BookEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(BookContract.BookEntry.TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted != 0)
                    getContext().getContentResolver().notifyChange(uri, null);
                return rowsDeleted;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {

        final int match = sUriMatcher.match(uri);
        switch(match) {
            case BOOKS:
                return updateBook(uri, values, selection, selectionArgs);
            case BOOKS_ID:
                selection = BookContract.BookEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri))};
                return updateBook(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update not supported for " + uri);
        }
    }

    private int updateBook(Uri uri, ContentValues values, String selection, String[] selectionArgs){

        if(values.containsKey(BookContract.BookEntry.COLUMN_PRODUCT_NAME)){
            String name = values.getAsString(BookContract.BookEntry.COLUMN_PRODUCT_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Pet requires a name");
            }
        }

        if(values.containsKey(BookContract.BookEntry.COLUMN_PRICE)){
            Integer price = values.getAsInteger(BookContract.BookEntry.COLUMN_PRICE);
            if(price == null || price < 0){
                throw new IllegalArgumentException("Book requires valid price");
            }
        }

        if(values.containsKey(BookContract.BookEntry.COLUMN_QUANTITY)){
            Integer quantity = values.getAsInteger(BookContract.BookEntry.COLUMN_QUANTITY);
            if(quantity < 0 ){
                throw new IllegalArgumentException("Book requires non negative quantity");
            }
        }

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsUpdated = database.update(BookContract.BookEntry.TABLE_NAME, values, selection, selectionArgs);
        if(rowsUpdated!=0) {
            getContext().getContentResolver().notifyChange(uri, null);
            }

        return rowsUpdated;
        }

    }

