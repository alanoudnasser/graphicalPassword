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
import android.widget.Button;

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

        edgePaint.setStrokeWidth(10);  // Adjust the stroke width to make the edges thicker

        for (int i = 0; i < NUM_SECTORS; i++) {
            int endAngle = startAngle + sweepAngle;

            // Draw the sector edge with different colors
            edgePaint.setColor(SECTOR_COLORS[i % SECTOR_COLORS.length]);

            sectorRect.set(
                    width / 2 - radius,
                    height / 2 - radius,
                    width / 2 + radius,
                    height / 2 + radius
            );

            // Draw the sector edge without filling
            canvas.drawArc(sectorRect, startAngle, sweepAngle, false, edgePaint);

            // Draw black lines to divide the sector
            linePaint.setColor(Color.BLACK);
            linePaint.setStrokeWidth(2);  // Adjust the stroke width for the lines

            Path linePath = new Path();
            linePath.arcTo(sectorRect, startAngle, sweepAngle);
            canvas.drawPath(linePath, linePaint);

            // Draw the alphabet in the center of the sector with rotation
            float centerX = width / 2f;
            float centerY = height / 2f;
            float angle = (startAngle + endAngle) / 2f;
            float x = centerX + (float) (radius * 0.75 * Math.cos(Math.toRadians(angle)));
            float y = centerY + (float) (radius * 0.75 * Math.sin(Math.toRadians(angle)));

            canvas.save();  // Save the canvas state
            canvas.rotate(rotationAngle, centerX, centerY);  // Apply rotation transformation
            canvas.drawText(ALPHABETS[i], x, y, textPaint);
            canvas.restore();  // Restore the canvas state

            // Draw lines to divide the sector
            float lineStartX = width / 2f;
            float lineStartY = height / 2f;
            float lineEndX = (float) (lineStartX + radius * Math.cos(Math.toRadians(startAngle)));
            float lineEndY = (float) (lineStartY + radius * Math.sin(Math.toRadians(startAngle)));

            canvas.drawLine(lineStartX, lineStartY, lineEndX, lineEndY, linePaint);

            // Update the start angle for the next sector
            startAngle = endAngle;
        }

        // Draw an outer stroke for the circle with different colors
        edgePaint.setStrokeWidth(5);  // Adjust the stroke width as needed
        edgePaint.setStyle(Paint.Style.STROKE);

        for (int i = 0; i < NUM_SECTORS; i++) {
            edgePaint.setColor(SECTOR_COLORS[i % SECTOR_COLORS.length]);
            canvas.drawArc(sectorRect, i * sweepAngle, sweepAngle, false, edgePaint);
        }
    }
    public void rotateClockwise() {
        rotationAngle += 10;
        invalidate();
    }

    public void rotateCounterClockwise() {
        rotationAngle -= 10;
        invalidate();
    }
}