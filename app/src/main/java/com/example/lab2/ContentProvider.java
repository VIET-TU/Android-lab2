package com.example.lab2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ContentProvider {
    private Activity activity;

    public ContentProvider(Activity activity) {
        this.activity = activity;
    }

        public ArrayList<Contact> getAllContact(){
            ArrayList<Contact> listContact = new ArrayList<>();
            String[] projection = new String[]{
                    ContactsContract.CommonDataKinds.Phone._ID,
                    ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER,
            };
            Cursor cursor = activity.getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    projection, null, null, null);
            if(cursor.moveToFirst())
            {
                do{

                    int id = cursor.getInt(0);
                    String image = cursor.getString(1);
                    String name = cursor.getString(2);
                    String phone = cursor.getString(3);
                    boolean status_ = false;

                    Bitmap imageBitmap = BitmapUtils.stringToBitmap(image);

                    Contact contact = new Contact(id,name,phone,imageBitmap,status_);
                    listContact.add(contact);
                }while(cursor.moveToNext());
            }
            cursor.close();
            return listContact;
        }

    public void writeContact(Contact newContact) {
        ContentResolver cr = this.activity.getContentResolver();
        ContentValues cv = new ContentValues();

        Uri rawContactUri = cr.insert(ContactsContract.RawContacts.CONTENT_URI, cv);

        long rawContactId = ContentUris.parseId(rawContactUri);

        ContentValues values = new ContentValues();
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, newContact.getFullName());
        cr.insert(ContactsContract.Data.CONTENT_URI, values);

        values.clear();
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, newContact.getPhoneNumber());
        values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
        cr.insert(ContactsContract.Data.CONTENT_URI, values);

        if (newContact.getAvartar() != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            newContact.getAvartar().compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            values.clear();
            values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
            values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE);
            values.put(ContactsContract.CommonDataKinds.Photo.PHOTO, byteArray);
            cr.insert(ContactsContract.Data.CONTENT_URI, values);
        }

    }





}
