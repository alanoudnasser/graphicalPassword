package com.example.graphpassword;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
public class MainActivity2 extends AppCompatActivity {
    private TextView t1,t2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
       t1 = findViewById(R.id.textView2);
        t2 = findViewById(R.id.textView3);

        // Example usage of PasswordEncryptor
        String userPassword = "sarah";

        // Encrypt the password
        String encryptedPassword = PasswordEncryptor.encryptPassword(userPassword);
        t1.setText(encryptedPassword);

        // Decrypt the password
        String decryptedPassword = PasswordEncryptor.decryptPassword(encryptedPassword);
        t2.setText(decryptedPassword);
    }



}