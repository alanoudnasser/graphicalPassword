package com.example.graphpassword;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private WheelView wheelView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        wheelView = findViewById(R.id.wheelView);
        wheelView.setTextView(textView); // Set the TextView in the WheelView
    }

    public void rotateClockwise(View view) {
        wheelView.rotateClockwise();
    }

    public void rotateCounterClockwise(View view) {
        wheelView.rotateCounterClockwise();
    }
}