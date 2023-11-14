package com.example.graphpassword;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class WheelView extends View {
    private static final int NUM_SECTORS = 8;
    private static final int EDGE_COLOR = Color.RED;
    private static final int EDGE_WIDTH = 20;
    private static final String[] ALPHABETS = {
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
            "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
            "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            ".", "/"
    };    private static final int[] SECTOR_COLORS = {
            Color.BLUE, Color.GREEN, Color.YELLOW,
            Color.MAGENTA, Color.CYAN, Color.RED,
            Color.GRAY, Color.DKGRAY
    };
    private static final int LINE_COLOR = Color.BLACK;
    private static final int LINE_WIDTH = 5;
    private int I =0;


    private Paint edgePaint;
    private Paint sectorPaint;
    private Paint linePaint;
    private Paint textPaint;
    private Random random;
    private RectF sectorRect;
    private float rotationAngle;
    private int selectedSector = -1;
    private TextView textView;

    public WheelView(Context context) {
        super(context);
        init();
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }


    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        edgePaint = new Paint();
        edgePaint.setStyle(Paint.Style.STROKE);
        edgePaint.setStrokeWidth(EDGE_WIDTH);
        edgePaint.setColor(EDGE_COLOR);

        sectorPaint = new Paint();
        sectorPaint.setStyle(Paint.Style.FILL);

        linePaint = new Paint();
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(LINE_WIDTH);
        linePaint.setColor(LINE_COLOR);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(50);
//        textPaint.setTextAlign(Paint.Align.LEFT);

        random = new Random();
        sectorRect = new RectF();
        shuffleAlphabets();

    }
    private void shuffleAlphabets() {
        List<String> alphabetList = new ArrayList<>(Arrays.asList(ALPHABETS));
        Collections.shuffle(alphabetList);
        alphabetList.toArray(ALPHABETS);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int radius = Math.min(width, height) / 2;

        // Set the background color to white
        canvas.drawColor(Color.WHITE);

        int startAngle = 0;
        int sweepAngle = 360 / NUM_SECTORS;

        for (int i = 0; i < NUM_SECTORS; i++) {
            int endAngle = startAngle + sweepAngle;

      //     edgePaint.setColor(SECTOR_COLORS[i % SECTOR_COLORS.length]);

            sectorRect.set(
                    width / 2 - radius,
                    height / 2 - radius,
                    width / 2 + radius,
                    height / 2 + radius
            );

         //   canvas.drawArc(sectorRect, startAngle, sweepAngle, true, edgePaint);     shadow

            linePaint.setColor(Color.BLACK);
           // linePaint.setStrokeWidth(1);

            Path linePath = new Path();
            linePath.arcTo(sectorRect, startAngle, sweepAngle);
            canvas.drawPath(linePath, linePaint);

            float centerX = width / 2f;
            float centerY = height / 2f;

            float angle = (startAngle + endAngle) / 2f;

            float[] letterCoordinates = new float[32]; // Increase the array size to 32
            int[] letterIndices = new int[8]; // Move the declaration here

            for (int j = 0; j < 8; j++) {
                int row = j / 4; // Calculate the row based on the index
                float letterspacing=20.0f;
               angle = (startAngle + endAngle) / 2f - rotationAngle;
                float x = centerX + (float) (radius * ((j +5) / 9.0) * Math.cos(Math.toRadians(angle ))) +(3/4)  * letterspacing;
                float y = centerY + (float) (radius * ((j + 4) /9.0) * Math.sin(Math.toRadians(angle )))+( 3/4)  * letterspacing;

                textPaint.setTextAlign(Paint.Align.LEFT);
                // Adjust the y-coordinate based on the row
                if (row == 1) {
                    //y -= textPaint.getTextSize();
                    x = centerX + (float) (radius * ((j ) / 9.0) * Math.cos(Math.toRadians(angle ))) +(2)  * letterspacing;
                     y = centerY + (float) (radius * ((j ) /9.0) * Math.sin(Math.toRadians(angle ))) +(2)  * letterspacing;
//                    x = centerX + (float) (radius * ((j + 1) / 9.0) * Math.cos(Math.toRadians(angle - rotationAngle))) + (j % 4) * letterSpacing;
//                    y = centerY + (float) (radius * ((j + 1) / 9.0) * Math.sin(Math.toRadians(angle - rotationAngle))) + (j % 4) * letterSpacing;
                    textPaint.setTextAlign(Paint.Align.RIGHT);
                    // Use j*2 and j*2+1 to properly fill the coordinates array

                }
                letterCoordinates[j * 4] = x;
                letterCoordinates[j * 4+ 1] = y;
            }


            for (int j = 0; j < 8; j++) {

                letterIndices[j] = (i * 8 + j) % ALPHABETS.length;
            }

           // int colorIndex = (i + (NUM_SECTORS - (int) (rotationAngle / (360f / NUM_SECTORS)))) % NUM_SECTORS;
            //textPaint.setColor(SECTOR_COLORS[colorIndex % SECTOR_COLORS.length]);

            for (int j = 0; j < 8; j++) {
                String letter = ALPHABETS[letterIndices[j]]; // Get the letter from the ALPHABETS array

                // Check if the letter is uppercase
                boolean isUppercase = Character.isUpperCase(letter.charAt(0));
                boolean isNumber = Character.isDigit(letter.charAt(0));
                // Apply bold formatting if it is uppercase
                boolean isSpecialCharacter = letter.equals(".") || letter.equals("\\");
                // Apply appropriate formatting based on the letter type
                if (isUppercase || isSpecialCharacter) {
                    textPaint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                } else if (isNumber) {
                    textPaint.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
                } else {
                    textPaint.setTypeface(Typeface.DEFAULT);
                }

                // Draw the letter on the canvas
                canvas.drawText(letter, letterCoordinates[j * 4], letterCoordinates[j * 4 + 1], textPaint);
            //    canvas.drawText(ALPHABETS[letterIndices[j]], letterCoordinates[j * 4], letterCoordinates[j * 4 + 1], textPaint);

            }

            startAngle = endAngle;
        }

        edgePaint.setStrokeWidth(7);
        edgePaint.setStyle(Paint.Style.STROKE);

        for (int i = 0; i < NUM_SECTORS; i++) {
            edgePaint.setColor(SECTOR_COLORS[i % SECTOR_COLORS.length]);
            canvas.drawArc(sectorRect, i * sweepAngle, sweepAngle, true, edgePaint);
        }
    }




    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();

            int newlySelectedSector = getTouchedSector(x, y);

            if (newlySelectedSector != -1) {
                int colorIndex = (newlySelectedSector + (NUM_SECTORS - (int) (rotationAngle / (360f / NUM_SECTORS)))) % NUM_SECTORS;
                int sectorColor = SECTOR_COLORS[colorIndex % SECTOR_COLORS.length];

                if (sectorColor == Color.BLUE) {
                    String colorName = getColorName(sectorColor);

                    // Calculate the starting index of the selected sector in ALPHABETS
                    int sectorStartIndex = newlySelectedSector * 8;

                    StringBuilder selectedLetters = new StringBuilder();
                    for (int i = 0; i < 8; i++) {
                        int letterIndex = (sectorStartIndex + i) % ALPHABETS.length;
                        selectedLetters.append(ALPHABETS[letterIndex]+"-");
                    }

                    String message = "Selected letters: " + selectedLetters + ", Color: " + colorName;

                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                    if (textView != null) {
                        String currentText = textView.getText().toString();
                        String newText = currentText + selectedLetters.toString();

                        String realPass = "Ss12.";
                        if (checkpassword(newText, I)) {
                            if (realPass.length() - 1 <= I) {
                                Toast.makeText(getContext(), "complete", Toast.LENGTH_SHORT).show();
                                textView.post(() -> textView.setText(realPass));
                            } else {
                                I++;
                                Toast.makeText(getContext(), "True", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            I = 0;
                        }
                    }
                }

                return true;
            }
        }

        return super.onTouchEvent(event);
    }

    private int getTouchedSector(float x, float y) {
        int width = getWidth();
        int height = getHeight();
        int radius = Math.min(width, height) / 2;

        float centerX = width / 2f;
        float centerY = height / 2f;

        // Calculate the angle of the touched point with respect to the center of the wheel
        double angle = Math.toDegrees(Math.atan2(y - centerY, x - centerX));
        angle = (angle < 0) ? (360 + angle) : angle;

        // Adjust for the current rotation angle
        angle += rotationAngle;
        angle = (angle < 0) ? (360 + angle) : angle;

        // Calculate the selected sector based on the angle
        int sector = (int) (angle * NUM_SECTORS / 360);
        return sector;
    }





    private String getColorName(int color) {
        if (color == Color.BLUE) {
            return "Blue";
        } else if (color == Color.GREEN) {
            return "Green";
        } else if (color == Color.YELLOW) {
            return "Yellow";
        } else if (color == Color.MAGENTA) {
            return "Magenta";
        } else if (color == Color.CYAN) {
            return "Cyan";
        } else if (color == Color.RED) {
            return "Red";
        } else if (color == Color.GRAY) {
            return "Gray";
        } else if (color == Color.DKGRAY) {
            return "Dark Gray";
        } else {
            return "Unknown";
        }
    }







    public void rotateClockwise() {
        ValueAnimator animator = ValueAnimator.ofFloat(rotationAngle, rotationAngle + 45);
        animator.setDuration(500); // Set the duration of the animation in milliseconds

        animator.addUpdateListener(animation -> {
            rotationAngle = (float) animation.getAnimatedValue();
            invalidate(); // Invalidate the view to trigger redraw
        });

        animator.start();
    }

    public void rotateCounterClockwise() {
        ValueAnimator animator = ValueAnimator.ofFloat(rotationAngle, rotationAngle - 45);
        animator.setDuration(500); // Set the duration of the animation in milliseconds

        animator.addUpdateListener(animation -> {
            rotationAngle = (float) animation.getAnimatedValue();
            invalidate(); // Invalidate the view to trigger redraw
        });

        animator.start();
    }

    public boolean checkpassword(String x,int y){
        int i =y;
        String realpass = "Ss12.";
        String pass = x;
        if (realpass.length() > 0 && pass.length() > 0 &&i<=realpass.length()) {
            char firstLetter = realpass.charAt(i);
            return pass.contains(String.valueOf(firstLetter));
        }
        return false;
    }





}