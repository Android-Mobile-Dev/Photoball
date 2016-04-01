package team6.photoball;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

import static team6.photoball.BallAnimation.*;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Gallery.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class Gallery extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Bitmap mBitmap;

    public Gallery() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Gallery.
     */
    // TODO: Rename and change types and number of parameters
    public static Gallery create() {
        Gallery fragment = new Gallery();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EasyImage.openGallery(this, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final Gallery fragment = this;

        final View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        final FloatingActionButton addButton = (FloatingActionButton) view.findViewById(R.id.addButton);
        final FloatingActionButton cameraButton = (FloatingActionButton) view.findViewById(R.id.cameraButton);
        final FloatingActionButton playButton = (FloatingActionButton) view.findViewById(R.id.playButton);

        assert cameraButton != null;
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).moveCamera();
            }
        });

        assert playButton != null;
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).moveMyPicMaps();
            }
        });

        assert addButton != null;
        addButton.setScaleX((float) 1.3);
        addButton.setScaleY((float) 1.3);
        addButton.setY(-100);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyImage.openGallery(fragment, 0);
            }
        });

        LinearLayout container_ = (LinearLayout) view.findViewById(R.id.linearLayoutGallery);

        container_.addView(new MyAnimationView(this.getContext()));

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteractionGallery(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteractionGallery(Uri uri);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this.getActivity(), new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource imagePath, int type) {

                //Handle the image

                File modifiedImage = modifyImage(imageFile);

                Bitmap bitmap = (BitmapFactory.decodeFile(modifiedImage.getAbsolutePath()));

                mBitmap = bitmap;

                String appDirectoryName = "Photoball";

                File imageRoot = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES) + "/" + appDirectoryName);

                if (!imageRoot.exists()) {
                    imageRoot.mkdirs();
                }

                ContentValues values = new ContentValues();

                values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

                String firstPartFileName = MediaStore.Images.Media.DATE_TAKEN;

                File file = new File(new File(imageRoot.toString()), firstPartFileName + "_img.jpg");
                if (file.exists()) {
                    file.delete();
                }

                try {
                    FileOutputStream out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                values.put(MediaStore.MediaColumns.DATA, file.toString());

                getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            }
        });

        ImageView img = (ImageView) this.getView().findViewById(R.id.imageViewGallery);
        img.setImageBitmap(mBitmap);
    }

    private File modifyImage(File imageFile) {

        //Image modification here


        return imageFile;
    }

    public class MyAnimationView extends View {

        private static final int RED = 0xffFF8080;

        private static final int BLUE = 0xff8080FF;

        private static final int CYAN = 0xff80ffff;

        private static final int GREEN = 0xff80ff80;

        public final ArrayList balls = new ArrayList();

        AnimatorSet animation = null;

        public MyAnimationView(Context context) {

            super(context);

            // Animate background color

            // Note that setting the background color will automatically invalidate the

            // view, so that the animated color, and the bouncing balls, get redisplayed on

            // every frame of the animation.

            ValueAnimator colorAnim = ObjectAnimator.ofInt(this, "backgroundColor", RED, BLUE);

            colorAnim.setDuration(3000);

            colorAnim.setEvaluator(new ArgbEvaluator());

            colorAnim.setRepeatCount(1);

            colorAnim.setRepeatMode(ValueAnimator.REVERSE);

            colorAnim.start();
            this.start();
        }

        public void start() {

            ShapeHolder newBall = addBall(100, 100);

            // Bouncing animation with squash and stretch

            float startY = newBall.getY();

            float endY = getHeight() - 50f;

            float h = (float)getHeight();

            float eventY = 0;

            int duration = 10000000;

            ValueAnimator bounceAnim = ObjectAnimator.ofFloat(newBall, "y", startY, endY);

            bounceAnim.setDuration(duration);

            bounceAnim.setInterpolator(new AccelerateInterpolator());

            ValueAnimator squashAnim1 = ObjectAnimator.ofFloat(newBall, "x", newBall.getX(),

                    newBall.getX() - 25f);

            squashAnim1.setDuration(duration/4);

            squashAnim1.setRepeatCount(ValueAnimator.INFINITE);

            squashAnim1.setRepeatMode(ValueAnimator.REVERSE);

            squashAnim1.setInterpolator(new DecelerateInterpolator());

            ValueAnimator squashAnim2 = ObjectAnimator.ofFloat(newBall, "width", newBall.getWidth(),

                    newBall.getWidth() + 50);

            squashAnim2.setDuration(duration/4);

            squashAnim2.setRepeatCount(ValueAnimator.INFINITE);

            squashAnim2.setRepeatMode(ValueAnimator.REVERSE);

            squashAnim2.setInterpolator(new DecelerateInterpolator());

            ValueAnimator stretchAnim1 = ObjectAnimator.ofFloat(newBall, "y", endY,

                    endY + 25f);

            stretchAnim1.setDuration(duration/4);

            stretchAnim1.setRepeatCount(ValueAnimator.INFINITE);

            stretchAnim1.setInterpolator(new DecelerateInterpolator());

            stretchAnim1.setRepeatMode(ValueAnimator.REVERSE);

            ValueAnimator stretchAnim2 = ObjectAnimator.ofFloat(newBall, "height",

                    newBall.getHeight(), newBall.getHeight() - 25);

            stretchAnim2.setDuration(duration/4);

            stretchAnim2.setRepeatCount(ValueAnimator.INFINITE);

            stretchAnim2.setInterpolator(new DecelerateInterpolator());

            stretchAnim2.setRepeatMode(ValueAnimator.REVERSE);

            ValueAnimator bounceBackAnim = ObjectAnimator.ofFloat(newBall, "y", endY,

                    startY);

            bounceBackAnim.setDuration(duration);

            bounceBackAnim.setInterpolator(new DecelerateInterpolator());

            // Sequence the down/squash&stretch/up animations

            AnimatorSet bouncer = new AnimatorSet();

            bouncer.play(bounceAnim).before(squashAnim1);

            bouncer.play(squashAnim1).with(squashAnim2);

            bouncer.play(squashAnim1).with(stretchAnim1);

            bouncer.play(squashAnim1).with(stretchAnim2);

            bouncer.play(bounceBackAnim).after(stretchAnim2);

            // Fading animation - remove the ball when the animation is done

            ValueAnimator fadeAnim = ObjectAnimator.ofFloat(newBall, "alpha", 1f, 0f);

            fadeAnim.setDuration(250);

            fadeAnim.addListener(new AnimatorListenerAdapter() {

                /*@Override

                public void onAnimationEnd(Animator animation) {

                    balls.remove(((ObjectAnimator)animation).getTarget());

                }*/

            });

            // Sequence the two animations to play one after the other

            AnimatorSet animatorSet = new AnimatorSet();

            animatorSet.play(bouncer).before(fadeAnim);

            // Start the animation

            animatorSet.start();

        }

        private ShapeHolder addBall(float x, float y) {

            OvalShape circle = new OvalShape();

            circle.resize(50f, 50f);

            ShapeDrawable drawable = new ShapeDrawable(circle);

            ShapeHolder shapeHolder = new ShapeHolder(drawable);

            shapeHolder.setX(x - 25f);

            shapeHolder.setY(y - 25f);

            int red = (int)(Math.random() * 255);

            int green = (int)(Math.random() * 255);

            int blue = (int)(Math.random() * 255);

            int color = 0xff000000 | red << 16 | green << 8 | blue;

            Paint paint = drawable.getPaint(); //new Paint(Paint.ANTI_ALIAS_FLAG);

            int darkColor = 0xff000000 | red/4 << 16 | green/4 << 8 | blue/4;

            RadialGradient gradient = new RadialGradient(37.5f, 12.5f,

                    50f, color, darkColor, Shader.TileMode.CLAMP);

            paint.setShader(gradient);

            shapeHolder.setPaint(paint);

            balls.add(shapeHolder);

            return shapeHolder;
        }

        @Override

        protected void onDraw(Canvas canvas) {

            for (int i = 0; i < balls.size(); ++i) {

                ShapeHolder shapeHolder = (ShapeHolder) balls.get(i);

                canvas.save();

                canvas.translate(shapeHolder.getX(), shapeHolder.getY());

                shapeHolder.getShape().draw(canvas);

                canvas.restore();

            }

        }

    }

}
