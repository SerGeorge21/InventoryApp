package com.example.android.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private BookDbHelper mDbHelper;

    private BookCursorAdapter mCursorAdapter;

    private static final int BOOK_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addProductButton = (Button) findViewById(R.id.add_button);
        addProductButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
        // Find the ListView which will be populated with the book data
        ListView bookListView = (ListView) findViewById(R.id.list);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        bookListView.setEmptyView(emptyView);

        mCursorAdapter = new BookCursorAdapter(this, null);
        bookListView.setAdapter(mCursorAdapter);

        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, EditorActivity.class);

                //Uri that represents the clicked item
                Uri contentBookUri = ContentUris.withAppendedId(BookContract.BookEntry.CONTENT_URI, id);
                //Set Uri on the data filed of the Intent so that it's passed from one
                //activity to the other
                i.setData(contentBookUri);
                startActivity(i);
            }
        });

        getLoaderManager().initLoader(BOOK_LOADER, null, this);
    }

    /**
     * class that inserts dummy data to Database
     */
    public void insertBook(){

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(BookContract.BookEntry.COLUMN_PRODUCT_NAME, "You Do You");
        values.put(BookContract.BookEntry.COLUMN_PRICE, 10);
        values.put(BookContract.BookEntry.COLUMN_QUANTITY, 20);
        values.put(BookContract.BookEntry.COLUMN_SUPPLIER_NAME, "Kedros");
        values.put(BookContract.BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER, "210 27 10 48");

        Uri newUri = getContentResolver().insert(BookContract.BookEntry.CONTENT_URI, values);
    }



     /**
     * Helper method to delete all books from DB
     */

     private void deleteAllBooks(){
         int rowsDeleted = getContentResolver().delete(BookContract.BookEntry.CONTENT_URI, null, null);
         Log.v("MainActivity", rowsDeleted + " rows deleted from database");
     }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertBook();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllBooks();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                BookContract.BookEntry._ID,
                BookContract.BookEntry.COLUMN_PRODUCT_NAME,
                BookContract.BookEntry.COLUMN_PRICE,
                BookContract.BookEntry.COLUMN_QUANTITY
        };

        return new CursorLoader(this,
                BookContract.BookEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

         mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

         mCursorAdapter.swapCursor(null);
    }
}
