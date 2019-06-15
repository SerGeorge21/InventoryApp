package com.example.android.inventoryapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.Integer.valueOf;

public class BookCursorAdapter extends CursorAdapter {

    public BookCursorAdapter(Context context, Cursor c){
        super(context, c, 0);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        //Find all the Views
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);
        TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);
        TextView sellButton = (TextView) view.findViewById(R.id.sell_button);

        //Find the columns of pet attributes that we are interested in
        int nameColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_PRICE);
        final int quantityColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_QUANTITY);

        //Reading book attributes from the Cursor for the current Book
        final String bookName = cursor.getString(nameColumnIndex);
        final String bookPrice = String.valueOf(cursor.getInt(priceColumnIndex)) + "$";
        String bookQuantity = String.valueOf(cursor.getString(quantityColumnIndex)) + " left";
        final int bookQuantityInteger  = cursor. getInt(quantityColumnIndex);

        //Update TextViews with the attributes for current book
        nameTextView.setText(bookName);
        priceTextView.setText(bookPrice);
        quantityTextView.setText(bookQuantity);



        sellButton.setClickable(true);
        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                /*values.put(BookContract.BookEntry.COLUMN_PRODUCT_NAME, bookName);
                values.put(BookContract.BookEntry.COLUMN_PRICE, Integer.parseInt(bookPrice)); */
                if(bookQuantityInteger > 0) {
                    values.put(BookContract.BookEntry.COLUMN_QUANTITY, bookQuantityInteger - 1);

                    context.getContentResolver().update(BookContract.BookEntry.CONTENT_URI,
                            values,
                            null,
                            null);
                }

                if(bookQuantityInteger==0) {
                    Toast toast = Toast.makeText(context.getApplicationContext(), "Quantity is already 0", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }); //TODO instead of changing the quantity on the item selected, it sets the same quantity for all items

    }
}
