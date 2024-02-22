package com.example.lab2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Contact> contactList;
    private ListView listView;
    private Adapter adapter;
    private EditText inputName;
    private Button addButton, deleteButton;




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK) {


                int id = data.getIntExtra("id", 0);
                String fullName = data.getStringExtra("fullName");
                String phoneNumber = data.getStringExtra("phoneNumber");

                Contact newContact = new Contact(id, fullName, phoneNumber, false);

                contactList.add(newContact);
                adapter.notifyDataSetChanged();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        inputName = findViewById(R.id.inputName);
        addButton = findViewById(R.id.addButton);
        deleteButton = findViewById(R.id.deleteButton);
        listView = findViewById(R.id.listPerson);

        // Create a list of Contact objects
        contactList = new ArrayList<>();
        contactList.add(new Contact(1, "John Doe", "1234567890", false));
        contactList.add(new Contact(2, "Jane Smith", "0987654321", false));
        // Add more contacts as needed

        // Create the adapter and set it to the ListView
        adapter = new Adapter(contactList, this);
        listView.setAdapter(adapter);

        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        //
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AddNew.class);
                startActivityForResult(i, 1);
            }
        });


        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Contact> checkedItems = adapter.getCheckedItems();

                for (Contact contact : checkedItems) {
                    contactList.remove(contact);
                }

                // Cập nhật lại ListView
                adapter.notifyDataSetChanged();
            }
        });



    }


}