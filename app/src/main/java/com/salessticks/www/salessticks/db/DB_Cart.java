package com.salessticks.www.salessticks.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.salessticks.www.salessticks.adapter.POJO_Customer;

import java.util.ArrayList;
import java.util.List;

/*
 * Class Name 	: DB_Cart
 * Description 	: Cart
 * Author 		: Saneeb salam
 */

public class DB_Cart extends SQLiteOpenHelper {

    // Contacts db name
    private final static String DB_ACCOUNT = "DB_Cart";
    // Contacts table name
    private final String TABLE_NAME = "cart";

    private static final String ID = "ID";
    private static final String Name = "Name";

    // Database Version
    private static final int DATABASE_VERSION = 2;
    String selectQuery;
    SQLiteDatabase database;
    Cursor cursor;
    private List<POJO_Customer> detailsList;

    public DB_Cart(Context context) {
        super(context, DB_ACCOUNT, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

//        query1 = "CREATE TABLE " + TABLE_NAME + "(" + ID + "TEXT PRIMARY KEY," + Name + " TEXT )";

        db.execSQL(
                "create table cart " +
                        "(ID integer primary key, Name text)"
        );

//        db.execSQL(query1);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public void adddata(POJO_Customer temp) {

        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID, temp.getId());
        values.put(Name, temp.getName());

        database.insert(TABLE_NAME, null, values);
        database.close();

    }

    public synchronized List<POJO_Customer> getAlldetails(String Phonenumber) {
        detailsList = new ArrayList<>();

        selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + ID
                + " =" + "'" + Name + "'";

        database = this.getReadableDatabase();

        cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                POJO_Customer temp = new POJO_Customer();

                temp.setId(cursor.getString(0));
                temp.setName(cursor.getString(1));

                detailsList.add(temp);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return detailsList;
    }

//    public synchronized void updatename(String Phonenumber, String name,
//                                        String profilepicture, String privacystatus, int currentjobDefault,
//                                        int previousjobDefault, int educationDefault, String doc) {
//
//        database = this.getWritableDatabase();
//        database.execSQL("UPDATE " + TABLE_NAME + " SET " + Name + "=" + "'"
//                + name + "' ," + ProfilePicture + "=" + "'" + profilepicture
//                + "' ," + PrivacyStatus + "=" + "'" + privacystatus + "' ,"
//                + CurrentjobDefault + "=" + "'" + currentjobDefault + "' ,"
//                + PreviousjobDefault + "=" + "'" + previousjobDefault + "' ,"
//                + EducationDefault + "=" + "'" + educationDefault + "',"
//                + Resume + "=" + "'" + doc + "'" + " WHERE " + PhoneNumber
//                + "=" + "'" + Phonenumber + "'");
//        database.close();
//    }


    public synchronized void removecontact(String name) {
        database = this.getWritableDatabase();

        database.delete(this.TABLE_NAME, Name + "=" + name, null);
        database.close();
    }

    public synchronized void removeAll() {
        database = this.getWritableDatabase();
        database.delete(this.TABLE_NAME, null, null);
        database.close();
    }

}
