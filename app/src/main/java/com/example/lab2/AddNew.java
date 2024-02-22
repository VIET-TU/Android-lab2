package com.example.lab2;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
public class AddNew extends AppCompatActivity {

    EditText editName, phone, idTxt;

    public Button backButton,addNewButton;
    public void changeToViewBack(View view){
        finish();
    }

    public void handleAddNew(View view){
        String fullName = editName.getText().toString();
        String phoneNumber = phone.getText().toString();
        int id = Integer.parseInt(idTxt.getText().toString());

        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("id", id);
        i.putExtra("fullName", fullName);
        i.putExtra("phoneNumber", phoneNumber);
        startActivity(i);

    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_person);
        editName = findViewById(R.id.txtName);
        phone= findViewById(R.id.txtPhoneNumber);
        idTxt = findViewById(R.id.txtID);
        backButton = findViewById(R.id.backButton);
        addNewButton = findViewById(R.id.addNewButton);

        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddNew.this, MainActivity.class);
                startActivity(i);

            }
        });

        addNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = editName.getText().toString();
                String phoneNumber = phone.getText().toString();
                int id = Integer.parseInt(idTxt.getText().toString());

                Intent resultIntent = new Intent();

                resultIntent.putExtra("id", id);
                resultIntent.putExtra("fullName", fullName);
                resultIntent.putExtra("phoneNumber", phoneNumber);

                setResult(RESULT_OK, resultIntent);

                finish();
            }
        });

    }
}

