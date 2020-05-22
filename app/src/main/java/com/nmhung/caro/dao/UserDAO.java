package com.nmhung.caro.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.nmhung.caro.model.UserModel;

public class UserDAO extends BaseDAO {

    public UserDAO(Context context) {
        super(context);
    }

    @Override
    public String createTable() {
        String createTable = " create table user ( ";
        createTable += " id INTEGER PRIMARY KEY, ";
        createTable += " username TEXT ,";
        createTable += " password TEXT ";
        createTable += " ) ";
        return createTable;
    }

    public void insert(UserModel userModel) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", userModel.getUsername());
        contentValues.put("password", userModel.getPassword());
        db.insert("user", null, contentValues);
    }

    public Cursor fetch() {
        String[] columns = new String[]{"id", "username", "password"};
        Cursor cursor = db.rawQuery("SELECT * FROM username where username = ?", new String[]{"admin"});
        if (cursor != null) {
            cursor.moveToFirst();
            cursor.getString(cursor.getColumnIndex("username"));

            UserModel userModel = new UserModel();
            System.out.println();
        }
        return cursor;
    }

}
