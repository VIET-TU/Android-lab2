package com.example.lab2;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AddNew extends AppCompatActivity {

    EditText editName, editPhone, idTxt;

    public Button backButton,addNewButton;

    public ImageView imageViewEdt;

    public CheckBox checkbox;

    private static  final int  SELECT_PICTURE = 200;

    public  Bitmap imageBitmap;

    public void changeToViewBack(View view){
        finish();
    }

    /*public void handleAddNew(View view){
        String fullName = editName.getText().toString();
        String phoneNumber = phone.getText().toString();
        int id = Integer.parseInt(idTxt.getText().toString());

        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("id", id);
        i.putExtra("fullName", fullName);
        i.putExtra("phoneNumber", phoneNumber);

        startActivity(i);

    }*/

    private Bitmap uriToBitmap(Uri uri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        return BitmapFactory.decodeStream(inputStream);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       if(requestCode == SELECT_PICTURE) {
           Uri imageUri  = data.getData();
           try {
               // Convert Uri to Bitmap
               imageBitmap = uriToBitmap(imageUri);
               // Assign Bitmap to ImageView
               imageViewEdt.setImageBitmap(imageBitmap);
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_person);
        editName = findViewById(R.id.txtName);
        editPhone= findViewById(R.id.txtPhoneNumber);
        idTxt = findViewById(R.id.txtID);
        backButton = findViewById(R.id.backButton);
        addNewButton = findViewById(R.id.addNewButton);
        imageViewEdt = findViewById(R.id.imageViewEdt);
        checkbox = findViewById(R.id.checkBox);

        //Lấy intent từ MainActivity chuyển sang
        Intent intent = getIntent();
        //Lấy bundle
        Bundle bundle = intent.getExtras();
        if(bundle !=null){



            int id = bundle.getInt("id");
            String image = bundle.getString("avatar");
                String name = bundle.getString("name");
            String phone = bundle.getString("phone");
            Boolean status = bundle.getBoolean("status");
            idTxt.setText(String.valueOf(id));
            editName.setText(name);
            editPhone.setText(phone);
            checkbox.setChecked(status);
//            imageViewEdt.setImageURI(null);

            byte[] byteArray = bundle.getByteArray("avatar");
            imageBitmap =  BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            imageViewEdt.setImageBitmap(imageBitmap);

            addNewButton.setText("Update");
        }


        backButton.setOnClickListener(v -> {
                Intent i = new Intent(AddNew.this, MainActivity.class);
                startActivity(i);

        });

        addNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = editName.getText().toString();
                String phoneNumber = editPhone.getText().toString();
                int id = Integer.parseInt(idTxt.getText().toString());
                boolean status = checkbox.isChecked();

                Intent resultIntent = new Intent();

                Bundle b = new Bundle();

                b.putInt("id", id);
                b.putString("name", fullName);
                b.putString("phone", phoneNumber);
                b.putBoolean("status",status);
//                b.putString("avatar",imageBitmap.toString());


                if (imageBitmap != null) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();

                    b.putByteArray("avatar", byteArray);


                }

                resultIntent.putExtras(b);


                setResult(RESULT_OK, resultIntent);

                finish();
            }
        });

        imageViewEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // intent of the type image
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);

                // pass the constant to compare it
                // with the returned requestCode
                startActivityForResult(i, SELECT_PICTURE);

            }
        });

        ///



    }
}

