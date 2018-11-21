package com.rainingkitkat.mobilepos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    //databaseHandler db;


    private Button barcodeBtn;
    private Button signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        barcodeBtn = findViewById(R.id.barcodeBtn);
        signup = findViewById(R.id.sign_up);

        //db = new databaseHandler(this);
        dbHandler db = new dbHandler(this);
    }

    public void toBarcodeScanner(View view){
        Intent intent = new Intent(getApplicationContext(), BarcodeScanner.class);
        startActivity(intent);
    }

    public void SignUp(View view){
        Intent intent = new Intent(getApplicationContext(), SignUp.class);
        startActivity(intent);
    }
}
