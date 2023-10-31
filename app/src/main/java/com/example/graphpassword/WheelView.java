package com.example.graphpassword;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
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
    private static final String[] ALPHABETS = {"A", "B", "C", "D", "E", "F", "G", "H"};
    private static final int[] SECTOR_COLORS = {
            Color.BLUE, Color.GREEN, Color.YELLOW,
            Color.MAGENTA, Color.CYAN, Color.RED,
            Color.GRAY, Color.DKGRAY
    };
    private static final int LINE_COLOR = Color.BLACK;
    private static final int LINE_WIDTH = 5;

    private Paint edgePaint;
    private Paint sectorPaint;
    private Paint linePaint;
    private Paint textPaint;
    private Random random;
    private RectF sectorRect;
    private float rotationAngle;
    private int selectedSector = -1;

    public WheelView(Context context) {
        super(context);
        init();
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
        textPaint.setTextSize(40);
        textPaint.setTextAlign(Paint.Align.CENTER);

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

            edgePaint.setColor(SECTOR_COLORS[i % SECTOR_COLORS.length]);

            sectorRect.set(
                    width / 2 - radius,
                    height / 2 - radius,
                    width / 2 + radius,
                    height / 2 + radius
            );

            canvas.drawArc(sectorRect, startAngle, sweepAngle, false, edgePaint);

            linePaint.setColor(Color.BLACK);
            linePaint.setStrokeWidth(2);

            Path linePath = new Path();
            linePath.arcTo(sectorRect, startAngle, sweepAngle);
            canvas.drawPath(linePath, linePaint);

            float centerX = width / 2f;
            float centerY = height / 2f;
            float angle = (startAngle + endAngle) / 2f;
            float x = centerX + (float) (radius * 0.75 * Math.cos(Math.toRadians(angle)));
            float y = centerY + (float) (radius * 0.75 * Math.sin(Math.toRadians(angle)));

            canvas.save();
            canvas.rotate(rotationAngle, centerX, centerY);
            canvas.drawText(ALPHABETS[i], x, y, textPaint);
            canvas.restore();

            float lineStartX = width / 2f;
            float lineStartY = height / 2f;
            float lineEndX = (float) (lineStartX + radius * Math.cos(Math.toRadians(startAngle)));
            float lineEndY = (float) (lineStartY + radius * Math.sin(Math.toRadians(startAngle)));

            canvas.drawLine(lineStartX, lineStartY, lineEndX, lineEndY, linePaint);

            startAngle = endAngle;
        }

        edgePaint.setStrokeWidth(5);
        edgePaint.setStyle(Paint.Style.STROKE);

        for (int i = 0; i < NUM_SECTORS; i++) {
            edgePaint.setColor(SECTOR_COLORS[i % SECTOR_COLORS.length]);
            canvas.drawArc(sectorRect, i * sweepAngle, sweepAngle, false, edgePaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int newlySelectedSector = getTouchedSector(x, y);
                if (newlySelectedSector != -1) {
                    String selectedLetter = ALPHABETS[newlySelectedSector];
                    int colorIndex = newlySelectedSector % SECTOR_COLORS.length;
                    int sectorColor = SECTOR_COLORS[colorIndex];
                    String colorName = getColorName(sectorColor);
                    Toast.makeText(getContext(), "Selected letter: " + selectedLetter + ", Color: " + colorName, Toast.LENGTH_SHORT).show();

                }
                break;
        }

        return super.onTouchEvent(event);
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

    private int getTouchedSector(float x, float y) {
        int width = getWidth();
        int height = getHeight();
        int radius = Math.min(width, height) / 2;

        float centerX = width / 2f;
        float centerY = height / 2f;

        // Calculate the angle based on the touch coordinates
        double angle = Math.toDegrees(Math.atan2(y - centerY, x - centerX));
        angle = (angle < 0) ? (360 + angle) : angle;

        // Adjust for the current rotation angle
        angle -= rotationAngle;
        if (angle < 0) {
            angle += 360;
        }

        // Compensate for the selected sector based on the current angle and rotation
        int sector = (int) (angle * NUM_SECTORS / 360);

        return sector;
    }





    public void rotateClockwise() {
        rotationAngle += 45;
        invalidate();
    }

    public void rotateCounterClockwise() {
        rotationAngle -= 45;
        invalidate();
    }
}