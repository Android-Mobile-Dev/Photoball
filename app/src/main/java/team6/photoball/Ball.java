package team6.photoball;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

/**
 * Created by nelma on 4/18/2016.
 */
public class Ball {
    float radius = 20;      // Ball's radius
    float x = radius + 20;  // Ball's center (x,y)
    float y = radius + 40;
    float speedX = 35/((float) 2.5);       // Ball's speed (x,y)
    float speedY = 35/((float) 2.5);
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
            x = box.xMax-radius;
            hitBorder = true;
        } else if (x - radius < box.xMin) {
            speedX = -speedX;
            x = box.xMin+radius;
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

        if(hitBorder){
            if(speedX > 0) {
                if(speedY > 0) {
                    direction = BallDirection.SE;
                } else if (speedY == 0){
                    direction = BallDirection.E;
                } else if (speedY < 0){
                    direction = BallDirection.NE;
                }
            } else if(speedX == 0){
                //not checking if speedY == 0 b/c then ball isn't moving
                if(speedY > 0) {
                    direction = BallDirection.S;
                }else if (speedY < 0){
                    direction = BallDirection.N;
                }
            } else if(speedX < 0){
                if(speedY > 0) {
                    direction = BallDirection.SW;
                } else if (speedY == 0){
                    direction = BallDirection.W;
                } else if (speedY < 0){
                    direction = BallDirection.NW;
                }
            }


            return;
        }

        int r = Color.red(img.getPixel((int) x, (int) y));
//        int g = Color.green(img.getPixel((int) x, (int) y));
//        int b = Color.blue(img.getPixel((int) x, (int) y));
//        Log.d("Color @ Pixel", "r: " + r + " " + "g: " + g + " b: " + b); // r == g == b since it's black and white
        //for now if r g or b is below 75, that is considered black

        // Detect collision and react

        if(r < 180){

            //black --> rebound
            //case 1 /
            //case 2 \
            //case 3 |
            //case 4 _
//            speedX = -speedX;
//            speedY = -speedY;
//            return;
            //http://stackoverflow.com/questions/6391777/switch-on-enum-in-java
            switch(direction){
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

    private void checkNorth(Bitmap img){
        //case 1 --> /
        int[] nearbyPixels = new int[4];
        if(hitForwardSlash(nearbyPixels, img)){
            speedX = -speedY;
            speedY = 0;
        } else if(hitBackSlash(nearbyPixels, img)){
            speedX = speedY;
            speedY = 0;
        } else{
            speedY = -speedY;
        }
    }

    private void checkNortheast(Bitmap img){
        int[] nearbyPixels = new int[4];
        if(hitVertical(nearbyPixels, img)){
            speedX = -speedX;
        } else if(hitHorizontal(nearbyPixels, img)){
            speedY = -speedY;
        } else{
            speedX = -speedX;
            speedY = -speedY;
        }
    }

    private void checkEast(Bitmap img){
        int[] nearbyPixels = new int[4];
        if(hitForwardSlash(nearbyPixels, img)){
            speedY = -speedX;
            speedX = 0;
        } else if(hitBackSlash(nearbyPixels, img)){
            speedY = speedX;
            speedX = 0;
        } else{
            speedX = -speedX;
        }
    }

    private void checkSoutheast(Bitmap img){
        int[] nearbyPixels = new int[4];
        if(hitVertical(nearbyPixels, img)){
            speedX = -speedX;
        } else if(hitHorizontal(nearbyPixels, img)){
            speedY = -speedY;
        } else{
            speedX = -speedX;
            speedY = -speedY;
        }
    }

    private void checkSouth(Bitmap img){
        int[] nearbyPixels = new int[4];
        if(hitForwardSlash(nearbyPixels, img)){
            speedX = -speedY;
            speedY = 0;
        } else if(hitBackSlash(nearbyPixels, img)){
            speedX = speedY;
            speedY = 0;
        } else{
            speedY = -speedY;
        }
    }

    private void checkSouthwest(Bitmap img){
        int[] nearbyPixels = new int[4];
        if(hitVertical(nearbyPixels, img)){
            speedX = -speedX;
        } else if(hitHorizontal(nearbyPixels, img)){
            speedY = -speedY;
        } else{
            speedX = -speedX;
            speedY = -speedY;
        }
    }

    private void checkWest(Bitmap img){
        int[] nearbyPixels = new int[4];
        if(hitForwardSlash(nearbyPixels, img)){
            speedY = -speedX;
            speedX = 0;
        } else if(hitBackSlash(nearbyPixels, img)){
            speedY = speedX;
            speedX = 0;
        } else{
            speedX = -speedX;
        }
    }

    private void checkNorthwest(Bitmap img){
        int[] nearbyPixels = new int[4];
        if(hitVertical(nearbyPixels, img)){
            speedX = -speedX;
        } else if(hitHorizontal(nearbyPixels, img)){
            speedY = -speedY;
        } else{
            speedX = -speedX;
            speedY = -speedY;
        }
    }

    private boolean hitForwardSlash(int[] pixels, Bitmap img){
        pixels[0] = Color.red(img.getPixel((int) x-2, (int) y+2));
        pixels[1] = Color.red(img.getPixel((int) x-1, (int) y+1));
        pixels[2] = Color.red(img.getPixel((int) x+1, (int) y+1));
        pixels[3] = Color.red(img.getPixel((int) x+2, (int) y-2));
        return pixels[0] < 180 && pixels[1] < 180 && pixels[2] < 180 && pixels[3] < 180;
    }

    private boolean hitBackSlash(int[] pixels, Bitmap img){
        pixels[0] = Color.red(img.getPixel((int) x+2, (int) y+2));
        pixels[1] = Color.red(img.getPixel((int) x+1, (int) y+1));
        pixels[2] = Color.red(img.getPixel((int) x-1, (int) y-1));
        pixels[3] = Color.red(img.getPixel((int) x-2, (int) y-2));
        return pixels[0] < 180 && pixels[1] < 180 && pixels[2] < 180 && pixels[3] < 180;
    }

    private boolean hitVertical(int[] pixels, Bitmap img){
        pixels[0] = Color.red(img.getPixel((int) x, (int) y+2));
        pixels[1] = Color.red(img.getPixel((int) x, (int) y+1));
        pixels[2] = Color.red(img.getPixel((int) x, (int) y-1));
        pixels[3] = Color.red(img.getPixel((int) x, (int) y-2));
        return pixels[0] < 180 && pixels[1] < 180 && pixels[2] < 180 && pixels[3] < 180;
    }

    private boolean hitHorizontal(int[] pixels, Bitmap img){
        pixels[0] = Color.red(img.getPixel((int) x-2, (int) y));
        pixels[1] = Color.red(img.getPixel((int) x-1, (int) y));
        pixels[2] = Color.red(img.getPixel((int) x+1, (int) y));
        pixels[3] = Color.red(img.getPixel((int) x+2, (int) y));
        return pixels[0] < 180 && pixels[1] < 180 && pixels[2] < 180 && pixels[3] < 180;
    }


    public void draw(Canvas canvas) {
        bounds.set(x-radius, y-radius, x+radius, y+radius);
        canvas.drawOval(bounds, paint);
    }

    public void setColor(int color) {
        paint.setColor(color);
    }
}
