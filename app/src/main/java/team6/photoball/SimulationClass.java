package team6.photoball;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.io.Serializable;


public class SimulationClass extends View {

    private Ball ball;
    private Box box;

    // For touch inputs - previous touch (x, y)
    private float previousX;
    private float previousY;

    private Bitmap img;

    // Constructor
    public SimulationClass(Context context, Bitmap bm) {
        super(context);

        if(bm != null) {
            img = bm;
        }
        box = new Box();  // ARGB
        int ball_color = PreferenceManager.getDefaultSharedPreferences(getContext()).getInt("ball_preference_key",0);
        ball = new Ball(ball_color);

        // To enable keypad
        this.setFocusable(true);
        this.requestFocus();
        // To enable touch mode
        this.setFocusableInTouchMode(true);
    }

    public void setBitmap(Bitmap bm){
        img = bm;
    }

    // Called back to draw the view. Also called after invalidate().
    @Override
    protected void onDraw(Canvas canvas) {
        if(img != null) {
            // Draw the components
            box.draw(canvas);
            ball.draw(canvas);

            // Update the position of the ball, including collision detection and reaction.
            ball.moveWithCollisionDetection(box, img);

            // Delay
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
            }
        }
        invalidate();
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

