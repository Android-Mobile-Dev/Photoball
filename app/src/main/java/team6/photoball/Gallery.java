package team6.photoball;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import pl.aprilapps.easyphotopicker.EasyImage;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Gallery.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class Gallery extends Fragment {

    private OnFragmentInteractionListener mListener;
    private ImageView mImageView = null;
    public Bitmap mBitmap = null;

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

        mImageView = (ImageView) view.findViewById(R.id.imageViewGallery);

        ((MainActivity)this.getActivity()).updateMenu();

        final FloatingActionButton addButton = (FloatingActionButton) view.findViewById(R.id.addButton);
        final FloatingActionButton cameraButton = (FloatingActionButton) view.findViewById(R.id.cameraButton);
        final FloatingActionButton playButton = (FloatingActionButton) view.findViewById(R.id.playButton);

        assert cameraButton != null;
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).moveToCamera();
            }
        });

        assert playButton != null;
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).moveMyToPicMaps();
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

        LinearLayout background = (LinearLayout) view.findViewById(R.id.linearLayoutGallery);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getContext());

        background.setBackgroundColor(prefs.getInt("background_preference_key",0));

        LinearLayout container_ = (LinearLayout) view.findViewById(R.id.ball);

        View bouncingBallView = new SimulationClass(this.getContext());

        container_.addView(bouncingBallView);

        if (savedInstanceState != null) {
            mBitmap = stringToBitMap(savedInstanceState.getString("gallery_bitmap"));
        }

        if (mBitmap != null)
            try {
                initRotateImageIfRequired();
            } catch (IOException e) {
                e.printStackTrace();
            }

        return view;
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteractionGallery(Uri uri);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != 0) {
            new ProcessTask(this.getContext(), (Fragment) this, requestCode, resultCode, data, R.id.imageViewGallery).execute();
        } else {
            this.getFragmentManager().popBackStack();
            ((MainActivity)getActivity()).moveToHome();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mBitmap != null)
            try {
                setRotateImageIfRequired(newConfig);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mBitmap != null) outState.putString("gallery_bitmap", bitMapToString(mBitmap));
    }

    public String bitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos = new  ByteArrayOutputStream();
        byte [] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public Bitmap stringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    private void setRotateImageIfRequired(Configuration newConfig) throws IOException {
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (mBitmap.getWidth() < mBitmap.getHeight())
                mBitmap = rotateImage(mBitmap, 270);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            if (mBitmap.getWidth() > mBitmap.getHeight())
                mBitmap = rotateImage(mBitmap, 90);
        }

        mImageView.setImageBitmap(mBitmap);
    }

    public void initRotateImageIfRequired() throws IOException {
        int orientation = this.getContext().getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (mBitmap.getWidth() < mBitmap.getHeight())
                mBitmap = rotateImage(mBitmap, 90);
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT){
            if (mBitmap.getWidth() > mBitmap.getHeight())
                mBitmap = rotateImage(mBitmap, 90);
        }

        mImageView.setImageBitmap(mBitmap);
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

}
