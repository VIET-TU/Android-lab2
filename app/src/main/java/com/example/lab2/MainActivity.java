package com.example.lab2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Contact> contactList;
    private ListView listView;
    private Adapter adapter;
    private EditText edtSearch;
    private Button addButton, deleteButton;

    private ContentProvider cp;

    private int SelectedItemId;

    private MyDB db;

//    ConnectionReceiver receiver;
//
//        IntentFilter intentFilter; // register components as Activity, BoardCastReceiver, Service for handle instancec (cho biết các điều kiện nào mà một thành phần nhất định có khả năng xử lý.)



    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;


    public MainActivity() throws IOException {
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int id = data.getIntExtra("id", 0);
        String fullName = data.getStringExtra("name");
        String phoneNumber = data.getStringExtra("phone");
        byte[] byteArray = data.getByteArrayExtra("avatar");
        Bitmap avatarBitmap =  BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        boolean status = data.getBooleanExtra("status", false);
        Contact newContact = new Contact(id, fullName, phoneNumber,avatarBitmap, status);
            if (requestCode == 100 && resultCode == RESULT_OK) {
                // add user

                contactList.add(newContact);
                db.addContact(newContact);
//                WriteContact(newContact);
                adapter.notifyDataSetChanged();

        } else if(requestCode == 200 && resultCode == RESULT_OK) {
                // edit user
                contactList.set(SelectedItemId,newContact);
                db.updateContact(newContact.id,newContact);
                adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
     public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfro){
        super.onCreateContextMenu(menu,v,menuInfro);
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.contextmenu,menu);
    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem  item) {
        Contact c = contactList.get(SelectedItemId);
      if(item.getItemId()== R.id.edtName) {
          Intent intent = new Intent(MainActivity.this, AddNew.class);
          Bundle b = new Bundle();
          b.putInt("id", c.getId());
          b.putString("name", c.getFullName());
          b.putString("phone",c.getPhoneNumber());
          b.putBoolean("status",c.status);
          Bitmap debug = c.getAvartar();

          Bitmap imageBitmap = c.getAvartar();

          ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
          imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
          byte[] byteArray = byteArrayOutputStream.toByteArray();

          b.putByteArray("avatar", byteArray);

          intent.putExtras(b);

          startActivityForResult(intent, 200);
      }
      return true;

    }

        @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item){
            if(item.getItemId()== R.id.mnuSortName){
                Toast.makeText(this,"Sort by name", Toast.LENGTH_SHORT).show();



            } else if (item.getItemId() == R.id.mnuBroadcast) {

            }

            return super.onOptionsItemSelected(item);
        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                this.ShowContact();
            } else {
                Toast.makeText(this,"Until you grant the permission, we canot display the names",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        edtSearch = findViewById(R.id.edtSearch);
        addButton = findViewById(R.id.addButton);
        deleteButton = findViewById(R.id.deleteButton);
        listView = findViewById(R.id.listPerson);

        // Create a list of Contact objects
        contactList = new ArrayList<>();


        //tao moi csdl
           db = new MyDB(this, "ContactTable3",
                    null, 1);


        contactList = db.getAllContact();
        adapter = new Adapter(contactList, this);
        listView.setAdapter(adapter);

//        ShowContact();




        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AddNew.class);
                startActivityForResult(i, 100);
            }
        });


        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Contact> checkedItems = adapter.getCheckedItems();

                for (Contact contact : checkedItems) {
                    contactList.remove(contact);
//                    db.deleteContact(contact.id);
                }

                // Cập nhật lại ListView
                adapter.notifyDataSetChanged();
            }
        });


        registerForContextMenu(listView);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView,
                                           View view, int i, long l) {
                SelectedItemId = i;
                return false;
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s.toString());
                adapter.notifyDataSetChanged();
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
//        receiver = new ConnectionReceiver();
//        intentFilter = new IntentFilter("com.example.listview2023.SOME_ACTION");
//        intentFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
//        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
//        registerReceiver(receiver, intentFilter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(receiver);
    }
    @Override
    protected void onResume() {
        super.onResume();
//        registerReceiver(receiver, intentFilter);
    }

    private void ShowContact()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(android.Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                    PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            cp = new ContentProvider(this);
            contactList = cp.getAllContact();
            adapter = new Adapter(contactList, this);
            listView.setAdapter(adapter);
        }
    }

    private void WriteContact(Contact contact)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.WRITE_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS},
                    PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            cp = new ContentProvider(this);
            cp.writeContact(contact);
        }


    }



    @Override
    protected void onStart() {
        super.onStart();
        String TAG = "MainActivity";
        Log.d(TAG, "ON_CREATE");
    }


}

// content://media/picker/0/com.android.providers.media.photopicker/media/1000000039