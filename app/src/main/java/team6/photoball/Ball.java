package team6.photoball;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Created by nelma on 4/18/2016.
 */
public class Ball {
    int radius = 20;      // Ball's radius
    int x = radius + 20;  // Ball's center (x,y)
    int y = radius + 40;
    float speedX = 35 / ((float) 2.5);       // Ball's speed (x,y)
    float speedY = 35 / ((float) 2.5);
    private RectF bounds;   // Needed for Canvas.drawOval
    private Paint paint;    // The paint style, color used for drawing

    public enum BallDirection {SE, S, SW, W, NW, N, NE, E}

    private BallDirection direction = BallDirection.SE;

    // Constructor
    public Ball(int color) {
        bounds = new RectF();
        paint = new Paint();
        paint.setColor(color);
    }

    public void moveWithCollisionDetection(Box box, Bitmap img) {
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

        boolean collision = false;
        int c = radius;
        for(int i = -c; i < c; ++i){
            try{
                int r = Color.red(img.getPixel( x + i,  y + i));
                if (r < 180){collision = true;}
            } catch (IllegalArgumentException e){

            }
        }

        if(x < 800 && x > 300 && y < 900 && y > 500) {
            int r = Color.red(img.getPixel(x, y));
            Log.d("COLOR", "X: " + x + "Y: " + y + " RED --> " + r);
        }
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
                    checkNorth(img);
                    break;
                case NE:
                    checkNortheast(img);
                    break;
                case E:
                    checkEast(img);
                    break;
                case SE:
                    checkSoutheast(img);
                    break;
                case S:
                    checkSouth(img);
                    break;
                case SW:
                    checkSouthwest(img);
                    break;
                case W:
                    checkWest(img);
                    break;
                case NW:
                    checkNorthwest(img);
                    break;
                default:
                    speedX = -speedX;
                    speedY = -speedY;
                    break;

            }

        }

    }

    private void checkNorth(Bitmap img) {
        //case 1 --> /
        int[] nearbyPixels = new int[2 * radius];
        if (hitForwardSlash(nearbyPixels, img)) {
            speedX = -speedY;
            speedY = 0;
        } else if (hitBackSlash(nearbyPixels, img)) {
            speedX = speedY;
            speedY = 0;
        } else {
            speedY = -speedY;
        }
    }

    private void checkNortheast(Bitmap img) {
        int[] nearbyPixels = new int[2 * radius];
        if (hitVertical(nearbyPixels, img)) {
            speedX = -speedX;
        } else if (hitHorizontal(nearbyPixels, img)) {
            speedY = -speedY;
        } else {
            speedX = -speedX;
            speedY = -speedY;
        }
    }

    private void checkEast(Bitmap img) {
        int[] nearbyPixels = new int[2 * radius];
        if (hitForwardSlash(nearbyPixels, img)) {
            speedY = -speedX;
            speedX = 0;
        } else if (hitBackSlash(nearbyPixels, img)) {
            speedY = speedX;
            speedX = 0;
        } else {
            speedX = -speedX;
        }
    }

    private void checkSoutheast(Bitmap img) {
        int[] nearbyPixels = new int[2 * radius];
        if (hitVertical(nearbyPixels, img)) {
            speedX = -speedX;
        } else if (hitHorizontal(nearbyPixels, img)) {
            speedY = -speedY;
        } else {
            speedX = -speedX;
            speedY = -speedY;
        }
    }

    private void checkSouth(Bitmap img) {
        int[] nearbyPixels = new int[2 * radius];
        if (hitForwardSlash(nearbyPixels, img)) {
            speedX = -speedY;
            speedY = 0;
        } else if (hitBackSlash(nearbyPixels, img)) {
            speedX = speedY;
            speedY = 0;
        } else {
            speedY = -speedY;
        }
    }

    private void checkSouthwest(Bitmap img) {
        int[] nearbyPixels = new int[2 * radius];
        if (hitVertical(nearbyPixels, img)) {
            speedX = -speedX;
        } else if (hitHorizontal(nearbyPixels, img)) {
            speedY = -speedY;
        } else {
            speedX = -speedX;
            speedY = -speedY;
        }
    }

    private void checkWest(Bitmap img) {
        int[] nearbyPixels = new int[2 * radius];
        if (hitForwardSlash(nearbyPixels, img)) {
            speedY = -speedX;
            speedX = 0;
        } else if (hitBackSlash(nearbyPixels, img)) {
            speedY = speedX;
            speedX = 0;
        } else {
            speedX = -speedX;
        }
    }

    private void checkNorthwest(Bitmap img) {
        int[] nearbyPixels = new int[2 * radius];
        if (hitVertical(nearbyPixels, img)) {
            speedX = -speedX;
        } else if (hitHorizontal(nearbyPixels, img)) {
            speedY = -speedY;
        } else {
            speedX = -speedX;
            speedY = -speedY;
        }
    }

    private boolean hitForwardSlash(int[] pixels, Bitmap img) {
        int c = radius;
        int numTrue = 0;
        int ind = 0;
        for (int i = -c; i <= c; ++i) {
            if (i == 0) {
                continue;
            }
            try {
                pixels[ind] = Color.red(img.getPixel( x + c, y - c));
                numTrue += pixels[ind] < 180 ? 1 : 0;
            } catch (IllegalArgumentException e) {//from going out of bounds

            }
            ++ind;
        }

        return numTrue > (radius / 2);
    }

    private boolean hitBackSlash(int[] pixels, Bitmap img) {

        int c = radius;
        int numTrue = 0;
        int ind = 0;
        for (int i = -c; i <= c; ++i) {
            if (i == 0) {
                continue;
            }
            try {
                pixels[ind] = Color.red(img.getPixel( x + c,  y + c));
                numTrue += pixels[ind] < 180 ? 1 : 0;
            } catch (IllegalArgumentException e) {//from going out of bounds

            }
            ++ind;
        }

        return numTrue > (radius / 2);
    }

    private boolean hitVertical(int[] pixels, Bitmap img) {
        int c = radius;
        int numTrue = 0;
        int ind = 0;
        for (int i = -c; i <= c; ++i) {
            if (i == 0) {
                continue;
            }
            try {
                pixels[ind] = Color.red(img.getPixel( x, y + c));
                numTrue += pixels[ind] < 180 ? 1 : 0;
            } catch (IllegalArgumentException e) {//from going out of bounds

            }
            ++ind;
        }

        return numTrue > (radius / 2);
    }

    private boolean hitHorizontal(int[] pixels, Bitmap img) {
        int c = radius;
        int numTrue = 0;
        int ind = 0;
        for (int i = -c; i <= c; ++i) {
            if (i == 0) {
                continue;
            }
            try {
                pixels[ind] = Color.red(img.getPixel( x + c, y));
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
}
