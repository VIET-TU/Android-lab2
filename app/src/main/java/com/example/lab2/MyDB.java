package com.example.lab2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteAbortException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.net.Uri;
import android.renderscript.Sampler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyDB extends SQLiteOpenHelper {

    public static final String TableName = "ContactTable3";
    public static final String Id = "Id";
    public static final String Name = "Fullname";
    public static final String Phone = "Phonenumber";
    public static final String Image = "Image";

    public static final String Status = "Status";

    public MyDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //tạo câu sql để tạo bảng TableContact
        String sqlCreate = "Create table if not exists " + TableName + "("
                + Id + " Integer Primary key , "
                + Image + " Text, "
                + Name + " Text, "
                + Status + " Status, "
                + Phone + " Text)";
        //chạy câu truy vấn SQL để tạo bảng
        db.execSQL(sqlCreate);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //xóa bảng TableContact đã có
        db.execSQL("Drop table if exists " + TableName);
        //tạo lại
        onCreate(db);
    }


    //lấy tất cả các dòng của bảng TableContact trả về dạng ArrayList
     public ArrayList<Contact> getAllContact()
    {
        ArrayList<Contact> list = new ArrayList<>();
        //câu truy vấn
        String sql = "Select * from " + TableName;
        //lấy đối tượng csdl sqlite
        SQLiteDatabase db = this.getReadableDatabase();
        //chạy câu truy vấn trả về dạng Cursor
        Cursor cursor = db.rawQuery(sql,null);
        //tạo ArrayList<Contact> để trả về;
        if(cursor!=null)
            while (cursor.moveToNext())
            {
                int id = cursor.getInt(0);
                String image = cursor.getString(1);
                String name = cursor.getString(2);
                boolean status_ = cursor.getInt(3) == 1 ? true :  false;
                String phone = cursor.getString(4);

                Bitmap imageBitmap = BitmapUtils.stringToBitmap(image);

                Contact contact = new Contact(id,name,phone,imageBitmap,status_);
                list.add(contact);
            }
        return list;
    }

    //hàm thêm một contact vào bảng TableContact
    public void addContact(Contact contact)
    {
            SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(Id, contact.getId());
        value.put(Image, BitmapUtils.bitmapToString(contact.getAvartar()));
        value.put(Name, contact.getFullName());
        value.put(Status, contact.status ? 1 : 0);
        value.put(Phone,contact.getPhoneNumber());
        db.insert(TableName,null,value);
        db.close();
    }
        public void updateContact(int id, Contact contact)
        {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues value = new ContentValues();
            value.put(Id, contact.getId());
            value.put(Image, BitmapUtils.bitmapToString(contact.getAvartar()));
            value.put(Name, contact.getFullName());
            value.put(Status, contact.status ? 1 : 0);
            value.put(Phone,contact.getPhoneNumber());
            db.insert(TableName,null,value);
            db.update(TableName, value,Id + "=?",
                    new String[]{String.valueOf(id)});
            db.close();
        }

    public void deleteContact(int id)
    {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "Delete From " + TableName + " Where ID=" + id;
        //db.delete(TblName, ID + "=?",new String[]{String.valueOf(id)});
        db.execSQL(sql);
        db.close();
    }
}
