package team6.photoball;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyPicMapsDetail.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class MyPicMapsDetail extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String EXTRA_IMAGE = "team6.photoball.extraImage";
    private static final String EXTRA_TITLE = "team6.photoball.extraTitle";

    // TODO: Rename and change types of parameters
    private String mFilePath = null;
    private ImageView mImageView = null;
    private Bitmap mBitmap = null;

    public MyPicMapsDetail() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MyPicMapsDetail create(String filePath) {
        MyPicMapsDetail fragment = new MyPicMapsDetail();
        fragment.setExtras(filePath);
        return fragment;
    }

    public void setExtras (String filePath) {
        mFilePath = filePath;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_pic_maps_detail, container, false);

        view.setId(view.generateViewId());

        File iFile = new File(mFilePath);

        mImageView = (ImageView) view.findViewById(R.id.imageViewMyPicMapsDetail);

        mBitmap = BitmapFactory.decodeFile(iFile.getAbsolutePath());

        ((MainActivity)this.getActivity()).updateMenu();

        LinearLayout background = (LinearLayout) view.findViewById(R.id.linearLayoutMyPicMapsDetail);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getContext());

        background.setBackgroundColor(prefs.getInt("background_preference_key",0));

        final FloatingActionButton addButton = (FloatingActionButton) view.findViewById(R.id.addButton);
        final FloatingActionButton cameraButton = (FloatingActionButton) view.findViewById(R.id.cameraButton);
        final FloatingActionButton playButton = (FloatingActionButton) view.findViewById(R.id.playButton);

        assert addButton != null;
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).moveToGallery();
            }
        });

        assert cameraButton != null;
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).moveToCamera();
            }
        });

        assert playButton != null;
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).moveMyToPicMaps();
            }
        });

        assert playButton != null;
        playButton.setScaleX((float) 1.3);
        playButton.setScaleY((float) 1.3);
        playButton.setY(-100);

        if (mBitmap != null)
            try {
                initRotateImageIfRequired();
                setBallLayoutAnimation (this.getContext(), view);
            } catch (IOException e) {
                e.printStackTrace();
            }

        return view;
    }

    private static void setBallLayoutAnimation (final Context context, View view) {

        final LinearLayout container_ = (LinearLayout) view.findViewById(R.id.ball);

        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                SimulationClass bouncingBallView = new SimulationClass(context, event.getX(), event.getY());
                container_.addView(bouncingBallView, 0);
                return true;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBitmap.recycle();
        mBitmap = null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof OnFragmentInteractionListener))
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteractionMyPicMapsDetail(Uri uri);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mBitmap != null) {
            try {
                setRotateImageIfRequired(newConfig);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setRotateImageIfRequired(Configuration newConfig) throws IOException {
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (mBitmap.getWidth() < mBitmap.getHeight())
                rotateImage(270);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            if (mBitmap.getWidth() > mBitmap.getHeight())
                rotateImage(90);
        }
        mImageView.setImageBitmap(mBitmap);
    }

    private void initRotateImageIfRequired() throws IOException {
        int orientation = this.getContext().getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (mBitmap.getWidth() < mBitmap.getHeight())
                rotateImage(90);
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT){
            if (mBitmap.getWidth() > mBitmap.getHeight())
                rotateImage(90);
        }
        mImageView.setImageBitmap(mBitmap);
    }

    private void rotateImage(int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
    }

}
