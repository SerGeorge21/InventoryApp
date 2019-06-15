package com.example.android.inventoryapp;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class BookContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.inventoryapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_BOOKS = "books";

    //We don't ever want to have a BookContract object so we give it
    //an empty Constructor
    private  BookContract(){}

    /**
     * Inner class that defines constant values for the Books database table.
     * Each entry in the table represents a single book.
     */
    public static final class BookEntry implements BaseColumns {


        /**
         * The MIME type of the CONTENT_URI for a list of pets.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;

        /**
         * The MIME type of the CONTENT_URI for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BOOKS);


        public final static String TABLE_NAME = "books";
        public static final String _ID  = BaseColumns._ID; //INTEGER
        public static final String COLUMN_PRODUCT_NAME = "Product_Name";//TEXT
        public static final String COLUMN_PRICE = "Price";//INTEGER
        public static final String COLUMN_QUANTITY = "Quantity";//INTEGER
        public static final String COLUMN_SUPPLIER_NAME = "Supplier_Name";//TEXT
        public static final String COLUMN_SUPPLIER_PHONE_NUMBER = "Supplier_Phone_Number";//TEXT




    }
}
