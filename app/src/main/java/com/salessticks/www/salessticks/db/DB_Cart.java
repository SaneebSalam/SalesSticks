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
    private static final String CustomerID = "CustomerID";
    private static final String Name = "Name";
    private static final String Quantity = "Quantity";
    private static final String Price = "Price";

    // Database Version
    private static final int DATABASE_VERSION = 4;
    String selectQuery;
    SQLiteDatabase database;
    Cursor cursor;
    private List<POJO_Customer> detailsList;

    public DB_Cart(Context context) {
        super(context, DB_ACCOUNT, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL(
                "create table cart " +
                        "(CustomerID integer , ID integer , Name text,  Quantity integer ,  Price double )"
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
        values.put(CustomerID, temp.getCustomerid());
        values.put(ID, temp.getId());
        values.put(Name, temp.getName());
        values.put(Quantity, temp.getQuantity());
        values.put(Price, temp.getPrice());

        database.insert(TABLE_NAME, null, values);
        database.close();

    }

    public synchronized List<POJO_Customer> getAlldetails(String customerID) {
        detailsList = new ArrayList<>();

        selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + CustomerID
                + " =" + "'" + customerID + "'";

        database = this.getReadableDatabase();

        cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                POJO_Customer temp = new POJO_Customer();

                temp.setCustomerid(cursor.getString(0));
                temp.setId(cursor.getString(1));
                temp.setName(cursor.getString(2));
                temp.setQuantity(cursor.getInt(3));
                temp.setPrice(cursor.getDouble(4));

                detailsList.add(temp);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return detailsList;
    }


    public synchronized List<POJO_Customer> getdetailsof_Item(String id) {
        detailsList = new ArrayList<>();

        selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + ID
                + " =" + "'" + id + "'";

        database = this.getReadableDatabase();

        cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                POJO_Customer temp = new POJO_Customer();

                temp.setCustomerid(cursor.getString(0));
                temp.setId(cursor.getString(1));
                temp.setName(cursor.getString(2));
                temp.setQuantity(cursor.getInt(3));
                temp.setPrice(cursor.getDouble(4));

                detailsList.add(temp);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return detailsList;
    }


    public synchronized void Update_Item(POJO_Customer temp) {

        database = this.getWritableDatabase();

        database.execSQL("UPDATE " + TABLE_NAME + " " + "SET" + " " + Name
                + "=" + "'" + temp.getName() + "'," + Quantity + "='"
                + temp.getQuantity() + "' " + "WHERE " + ID + "=" + "'" + temp.getId()
                + "'");

        database.close();
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


    public synchronized void removeitem(String id) {
        database = this.getWritableDatabase();

        database.delete(this.TABLE_NAME, ID + "=" + id, null);
        database.close();
    }

    public synchronized void removeAll() {
        database = this.getWritableDatabase();
        database.delete(this.TABLE_NAME, null, null);
        database.close();
    }

}
