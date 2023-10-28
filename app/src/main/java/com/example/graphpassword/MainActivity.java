package com.example.graphpassword;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private WheelView wheelView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wheelView = findViewById(R.id.wheelView);
    }

    public void rotateClockwise(View view) {
        wheelView.rotateClockwise();
    }

    public void rotateCounterClockwise(View view) {
        wheelView.rotateCounterClockwise();
    }
}