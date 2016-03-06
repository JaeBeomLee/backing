package com.mondaychicken.bacving;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by leejaebeom on 2015. 12. 14..
 */
public class pushManager extends SQLiteOpenHelper {
    public pushManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table push(_id Integer Primary Key AutoIncrement, title TEXT, message TEXT, profileimg TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(String query){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
        db.close();
    }

    public void delete(String query){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
        db.close();
    }

    public void update(String query){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
        db.close();
    }

    public String[][] select(){
        SQLiteDatabase db = getReadableDatabase();
        String str[][] = null;
        int i = 0;

        Cursor cursor = db.rawQuery("select * from push", null);
        if (cursor.moveToFirst()){
            while (cursor.moveToNext()){
                str[i][1] = cursor.getString(1);   //title
                str[i][2] = cursor.getString(2);   //message
                str[i][3] = cursor.getString(3);   //profileimg
                i++;
            }
        }
        return str;
    }
}
