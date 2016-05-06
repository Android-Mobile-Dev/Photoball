package team6.photoball;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by nelma on 4/18/2016.
 */
public class Ball {
    int radius = 20;   // Ball's radius
    int x = radius;  // Ball's center (x,y)
    int y = radius;
    float currentX = 0;
    float currentY = 0;
    int xBitmap;
    int yBitmap;
    float speedX = 35 / ((float) 2.5);       // Ball's speed (x,y)
    float speedY = 35 / ((float) 2.5);
    private RectF bounds;   // Needed for Canvas.drawOval
    private Paint paint;    // The paint style, color used for drawing

    public enum BallDirection {SE, S, SW, W, NW, N, NE, E}

    private BallDirection direction = BallDirection.SE;

    // Constructor
    public Ball(int color, float currentX, float currentY) {
        bounds = new RectF();
        paint = new Paint();
        paint.setColor(color);
        this.currentX = currentX;
        this.currentY = currentY;
        x += currentX;
        y += currentY;
    }

    public void moveWithCollisionDetection(Box box) {
        // Get new (x,y) position
        x += speedX;
        y += speedY;

        boolean hitBorder = false;

        if (x + radius > box.xMax) {
            speedX = -speedX;
            x = box.xMax - radius;
            hitBorder = true;
        } else if (x - radius < box.xMin) {
            speedX = -speedX;
            x = box.xMin + radius;
            hitBorder = true;
        }
        if (y + radius > box.yMax) {
            speedY = -speedY;
            y = box.yMax - radius;
            hitBorder = true;
        } else if (y - radius < box.yMin) {
            speedY = -speedY;
            y = box.yMin + radius;
            hitBorder = true;
        }

        if (hitBorder) {
            if (speedX > 0) {
                if (speedY > 0) {
                    direction = BallDirection.SE;
                } else if (speedY == 0) {
                    direction = BallDirection.E;
                } else if (speedY < 0) {
                    direction = BallDirection.NE;
                }
            } else if (speedX == 0) {
                //not checking if speedY == 0 b/c then ball isn't moving
                if (speedY > 0) {
                    direction = BallDirection.S;
                } else if (speedY < 0) {
                    direction = BallDirection.N;
                }
            } else if (speedX < 0) {
                if (speedY > 0) {
                    direction = BallDirection.SW;
                } else if (speedY == 0) {
                    direction = BallDirection.W;
                } else if (speedY < 0) {
                    direction = BallDirection.NW;
                }
            }
            return;
        }

        if (MainActivity.mBitmap != null && !MainActivity.mBitmap.isRecycled()) {
            boolean collision = false;
            int c = radius;
            double xScale = ((double) MainActivity.mBitmap.getWidth() / MainActivity.mImageView.getWidth());
            double yScale = ((double) MainActivity.mBitmap.getHeight() / MainActivity.mImageView.getHeight());
            xBitmap = (int) (x * xScale);
            yBitmap = (int) (y * yScale);
            if(xBitmap > MainActivity.mBitmap.getWidth()){
                xBitmap = MainActivity.mBitmap.getWidth();
            } else if(xBitmap < 0){
                xBitmap = 0;
            }
            if(yBitmap > MainActivity.mBitmap.getHeight()){
                yBitmap = MainActivity.mBitmap.getHeight();
            } else if(yBitmap < 0){
                yBitmap = 0;
            }
            int r = Color.red(MainActivity.mBitmap.getPixel(xBitmap, yBitmap));
            if (r < 180) {
                collision = true;
            }
            //
            //        return;
            //        for(int i = -c; i < c; ++i){
            //            try{
            //                int r = Color.red(img.getPixel( xBitmap + i,  yBitmap + i));
            //                if (r < 180){collision = true;}
            //            } catch (IllegalArgumentException e){
            //
            //            }
            //        }

            //        if(x < 800 && x > 300 && y < 900 && y > 500) {
            //            int r = Color.red(img.getPixel(x, y));
            //            Log.d("COLOR", "X: " + x + "Y: " + y + " RED --> " + r);
            //        }
            //        int g = Color.green(img.getPixel((int) x, (int) y));
            //        int b = Color.blue(img.getPixel((int) x, (int) y));
            //        Log.d("Color @ Pixel", "r: " + r + " " + "g: " + g + " b: " + b); // r == g == b since it's black and white
            //for now if r g or b is below 75, that is considered black

            // Detect collision and react

            if (collision) {

                //black --> rebound
                //case 1 /
                //case 2 \
                //case 3 |
                //case 4 _
                //            speedX = -speedX;
                //            speedY = -speedY;
                //            return;
                //http://stackoverflow.com/questions/6391777/switch-on-enum-in-java

                switch (direction) {
                    case N:
                        checkNorth(xScale, yScale);
                        break;
                    case NE:
                        checkNortheast(xScale, yScale);
                        break;
                    case E:
                        checkEast(xScale, yScale);
                        break;
                    case SE:
                        checkSoutheast(xScale, yScale);
                        break;
                    case S:
                        checkSouth(xScale, yScale);
                        break;
                    case SW:
                        checkSouthwest(xScale, yScale);
                        break;
                    case W:
                        checkWest(xScale, yScale);
                        break;
                    case NW:
                        checkNorthwest(xScale, yScale);
                        break;
                    default:
                        speedX = -speedX;
                        speedY = -speedY;
                        break;

                }


            }
        }
    }

    private void checkNorth(double xScale, double yScale) {
        //case 1 --> /
        int[] nearbyPixels = new int[2 * radius];
        if (hitForwardSlash(nearbyPixels, xScale, yScale)) {
            speedX = -speedY;
            speedY = 0;
            direction = BallDirection.E;
        } else if (hitBackSlash(nearbyPixels, xScale, yScale)) {
            speedX = speedY;
            speedY = 0;
            direction = BallDirection.W;
        } else {
            speedY = -speedY;
            direction = BallDirection.S;
        }
    }

    private void checkNortheast(double xScale, double yScale) {
        int[] nearbyPixels = new int[2 * radius];
        if (hitVertical(nearbyPixels, xScale, yScale)) {
            speedX = -speedX;
            direction = BallDirection.NW;
        } else if (hitHorizontal(nearbyPixels, xScale, yScale)) {
            speedY = -speedY;
            direction = BallDirection.SE;
        } else {
            speedX = -speedX;
            speedY = -speedY;
            direction = BallDirection.SW;
        }
    }

    private void checkEast(double xScale, double yScale) {
        int[] nearbyPixels = new int[2 * radius];
        if (hitForwardSlash(nearbyPixels, xScale, yScale)) {
            speedY = -speedX;
            speedX = 0;
            direction = BallDirection.N;
        } else if (hitBackSlash(nearbyPixels, xScale, yScale)) {
            speedY = speedX;
            speedX = 0;
            direction = BallDirection.S;
        } else {
            speedX = -speedX;
            direction = BallDirection.W;
        }
    }

    private void checkSoutheast(double xScale, double yScale) {
        int[] nearbyPixels = new int[2 * radius];
        if (hitVertical(nearbyPixels, xScale, yScale)) {
            speedX = -speedX;
            direction = BallDirection.SW;
        } else if (hitHorizontal(nearbyPixels, xScale, yScale)) {
            speedY = -speedY;
            direction = BallDirection.NE;
        } else {
            speedX = -speedX;
            speedY = -speedY;
            direction = BallDirection.NW;
        }
    }

    private void checkSouth(double xScale, double yScale) {
        int[] nearbyPixels = new int[2 * radius];
        if (hitForwardSlash(nearbyPixels, xScale, yScale)) {
            speedX = -speedY;
            speedY = 0;
            direction = BallDirection.W;
        } else if (hitBackSlash(nearbyPixels, xScale, yScale)) {
            speedX = speedY;
            speedY = 0;
            direction = BallDirection.E;
        } else {
            speedY = -speedY;
            direction = BallDirection.N;
        }
    }

    private void checkSouthwest(double xScale, double yScale) {
        int[] nearbyPixels = new int[2 * radius];
        if (hitVertical(nearbyPixels, xScale, yScale)) {
            speedX = -speedX;
            direction = BallDirection.SE;
        } else if (hitHorizontal(nearbyPixels, xScale, yScale)) {
            speedY = -speedY;
            direction = BallDirection.NW;
        } else {
            speedX = -speedX;
            speedY = -speedY;
            direction = BallDirection.NE;
        }
    }

    private void checkWest(double xScale, double yScale) {
        int[] nearbyPixels = new int[2 * radius];
        if (hitForwardSlash(nearbyPixels, xScale, yScale)) {
            speedY = -speedX;
            speedX = 0;
            direction = BallDirection.S;
        } else if (hitBackSlash(nearbyPixels, xScale, yScale)) {
            speedY = speedX;
            speedX = 0;
            direction = BallDirection.N;
        } else {
            speedX = -speedX;
            direction = BallDirection.E;
        }
    }

    private void checkNorthwest(double xScale, double yScale) {
        int[] nearbyPixels = new int[2 * radius];
        if (hitVertical(nearbyPixels, xScale, yScale)) {
            speedX = -speedX;
            direction = BallDirection.NE;
        } else if (hitHorizontal(nearbyPixels, xScale, yScale)) {
            speedY = -speedY;
            direction = BallDirection.SW;
        } else {
            speedX = -speedX;
            speedY = -speedY;
            direction = BallDirection.SE;
        }
    }

    private boolean hitForwardSlash(int[] pixels, double xScale, double yScale) {
        int c = radius;
        int numTrue = 0;
        int ind = 0;
        for (int i = -c; i <= c; ++i) {
            if (i == 0) {
                continue;
            }
            try {
                pixels[ind] = Color.red(MainActivity.mBitmap.getPixel( xBitmap + c, yBitmap - c));
                numTrue += pixels[ind] < 180 ? 1 : 0;
            } catch (IllegalArgumentException e) {//from going out of bounds

            }
            ++ind;
        }

        return numTrue > (radius / 2);
    }

    private boolean hitBackSlash(int[] pixels, double xScale, double yScale) {
        int c = radius;
        int numTrue = 0;
        int ind = 0;
        for (int i = -c; i <= c; ++i) {
            if (i == 0) {
                continue;
            }
            try {
                pixels[ind] = Color.red(MainActivity.mBitmap.getPixel( xBitmap + c,  yBitmap + c));
                numTrue += pixels[ind] < 180 ? 1 : 0;
            } catch (IllegalArgumentException e) {//from going out of bounds

            }
            ++ind;
        }

        return numTrue > (radius / 2);
    }

    private boolean hitVertical(int[] pixels, double xScale, double yScale) {
        int c = radius;
        int numTrue = 0;
        int ind = 0;
        for (int i = -c; i <= c; ++i) {
            if (i == 0) {
                continue;
            }
            try {
                pixels[ind] = Color.red(MainActivity.mBitmap.getPixel( xBitmap, yBitmap + c));
                numTrue += pixels[ind] < 180 ? 1 : 0;
            } catch (IllegalArgumentException e) {//from going out of bounds

            }
            ++ind;
        }

        return numTrue > (radius / 2);
    }

    private boolean hitHorizontal(int[] pixels, double xScale, double yScale) {
        int c = radius;
        int numTrue = 0;
        int ind = 0;
        for (int i = -c; i <= c; ++i) {
            if (i == 0) {
                continue;
            }
            try {
                pixels[ind] = Color.red(MainActivity.mBitmap.getPixel( xBitmap + c, yBitmap));
                numTrue += pixels[ind] < 180 ? 1 : 0;
            } catch (IllegalArgumentException e) {//from going out of bounds

            }
            ++ind;
        }

        return numTrue > (radius / 2);
    }


    public void draw(Canvas canvas) {
        bounds.set(x - radius, y - radius, x + radius, y + radius);
        canvas.drawOval(bounds, paint);
    }

    public void setColor(int color) {
        paint.setColor(color);
    }

    public void setRadius(int value) {
        radius = value;      // Ball's radius
        x = radius;  // Ball's center (x,y)
        y = radius;
        x += this.currentX;
        y += this.currentY;
    }
    public void setSpeed(float value) {
        speedX = value / ((float) 2.5);       // Ball's speed (x,y)
        speedY = value / ((float) 2.5);
    }

    public void setPosition (float currentX, float currentY) {
        this.currentX = currentX;
        this.currentY = currentY;
        x += this.currentX;
        y += this.currentY;
    }
}