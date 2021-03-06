package com.rainingkitkat.mobilepos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Home extends AppCompatActivity {
    Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        utils = new Utils();
    }

    public void toBarcodeScanner(View view){
        Intent intent = new Intent(getApplicationContext(), BarcodeScanner.class);
        startActivity(intent);
    }

    public void toCart(View view){
        Intent intent = new Intent(getApplicationContext(), Cart.class);
        startActivity(intent);
    }

    public void signOut(View view){
        finish();
        Utils.isNotLoggedIn(this);
    }

    //ToDO : Disable back button
}
