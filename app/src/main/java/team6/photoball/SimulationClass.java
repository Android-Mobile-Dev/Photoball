package team6.photoball;

import android.content.Context;
import android.graphics.Canvas;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;

import java.io.Serializable;


public class SimulationClass extends View {

    private Ball ball;
    private Box box;

    // For touch inputs - previous touch (x, y)
    private float previousX;
    private float previousY;

    // Constructor
    public SimulationClass(Context context) {
        super(context);

        box = new Box();  // ARGB
        int ball_color = PreferenceManager.getDefaultSharedPreferences(getContext()).getInt("ball_preference_key",0);
        ball = new Ball(ball_color);

        // To enable keypad
        this.setFocusable(true);
        this.requestFocus();
        // To enable touch mode
        this.setFocusableInTouchMode(true);
    }

    // Called back to draw the view. Also called after invalidate().
    @Override
    protected void onDraw(Canvas canvas) {
        // Draw the components
        box.draw(canvas);
        ball.draw(canvas);

        // Update the position of the ball, including collision detection and reaction.
        ball.moveWithCollisionDetection(box);

        // Delay
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) { }

        invalidate();  // Force a re-draw
    }

    // Called back when the view is first created or its size changes.
    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        // Set the movement bounds for the ball
        box.set(0, 0, w, h);
    }

    // Touch-input handler
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float currentX = event.getX();
        float currentY = event.getY();
        float deltaX, deltaY;
        float scalingFactor = 5.0f / ((box.xMax > box.yMax) ? box.yMax : box.xMax);
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                // Modify rotational angles according to movement
                deltaX = currentX - previousX;
                deltaY = currentY - previousY;
                ball.speedX += deltaX * scalingFactor;
                ball.speedY += deltaY * scalingFactor;
        }
        // Save current x, y
        previousX = currentX;
        previousY = currentY;
        return true;  // Event handled
    }
}

