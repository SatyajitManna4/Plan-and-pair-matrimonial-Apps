
package com.example.planpair;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

public class CompatibilityScoreView extends View {
    private Paint borderPaint;
    private Paint progressPaint;
    private Paint textPaint;
    private RectF oval;
    private float compatibilityScore = 5; // Example default score

    public CompatibilityScoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // Border Paint (Light Gray)
        borderPaint = new Paint();
        borderPaint.setColor(0xFFE0E0E0);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(20);
        borderPaint.setAntiAlias(true);

        // Progress Paint (sagegreen)
        progressPaint = new Paint();
        progressPaint.setColor(0xFFAFAB38);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(20);
        progressPaint.setAntiAlias(true);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);

        // Text Paint (White, Centered)
        textPaint = new Paint();
        textPaint.setColor(0xFF0E0E0E);
        textPaint.setTextSize(60);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);

        oval = new RectF();
    }

    public void setCompatibilityScore(float score) {
        this.compatibilityScore = score;
        invalidate(); // Refresh the view
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        float centerX = width / 2f;
        float centerY = height / 2f;

        // Draw background color (if needed)
        canvas.drawColor(0x00FFFFFF); // Transparent background

        // Define the oval area
        oval.set(20, 20, width - 20, height - 20);

        // Draw the gray border first
        canvas.drawArc(oval, 0, 360, false, borderPaint);


        // Draw compatibility arc
        float sweepAngle = (compatibilityScore / 100f) * 360;
        canvas.drawArc(20, 20, width - 20, height - 20, -90, sweepAngle, false, progressPaint);

        // Text setup
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(0xFF0E0E0E); // Black text
        textPaint.setTextSize(50);
        textPaint.setTextAlign(Paint.Align.CENTER);


        // Draw text
        String percentageText = (int) compatibilityScore + "%\nCompatibility";
        float textY = centerY - ((textPaint.descent() + textPaint.ascent()) / 2);
        for (String line : percentageText.split("\n")) {
            canvas.drawText(line, centerX, textY, textPaint);
            textY += textPaint.getTextSize(); // Move to the next line
        }
    }

}

/*
//satya
package com.example.planpair;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class CompatibilityScoreView extends View {
    private Paint borderPaint;
    private Paint progressPaint;
    private Paint textPaint; // Paint for drawing text
    private RectF oval;
    private float compatibilityScore = 75; // Default score for testing

    public CompatibilityScoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        borderPaint = new Paint();
        borderPaint.setColor(0xFFC0C0C0); // Light gray border
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(20);
        borderPaint.setAntiAlias(true);

        progressPaint = new Paint();
        progressPaint.setColor(0xFF4CAF50); // Green progress arc
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(20);
        progressPaint.setAntiAlias(true);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);

        textPaint = new Paint();
        textPaint.setColor(0xFF000000); // Black text
        textPaint.setTextSize(60); // Adjust size as needed
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);

        oval = new RectF();
    }

    public void setCompatibilityScore(float score) {
        this.compatibilityScore = score;
        invalidate(); // Refresh the view
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Set the background color
        canvas.drawColor(0xF1EDED); // Sagegreen background

        int width = getWidth();
        int height = getHeight();
        int radius = Math.min(width, height) / 2 - 20;
        float sweepAngle = (compatibilityScore / 100f) * 360;

        // Draw the border (full circle)
        canvas.drawArc(20, 20, width - 20, height - 20, 0, 360, false, borderPaint);

        // Draw the compatibility progress arc
        canvas.drawArc(20, 20, width - 20, height - 20, -90, sweepAngle, false, progressPaint);

        // Draw the compatibility score text in the center
        String scoreText = (int) compatibilityScore + "%"; // Display as integer percentage
        float centerX = width / 2f;
        float centerY = height / 2f;
        canvas.drawText(scoreText, centerX, centerY + (textPaint.getTextSize() / 3), textPaint);
    }
}*/
