package team6.photoball;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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

import pl.aprilapps.easyphotopicker.EasyImage;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Gallery.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class Gallery extends Fragment {

    private OnFragmentInteractionListener mListener;
    public File mImageFile;
    public boolean b = false;

    public Gallery() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Gallery.
     */
    // TODO: Rename and change types and number of parameters
    public static Gallery create() {
        Gallery fragment = new Gallery();
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

        ((MainActivity)this.getActivity()).updateMenu();

        MainActivity.mImageView = (ImageView) view.findViewById(R.id.imageViewGallery);

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

        MainActivity.mContainer = null;
        MainActivity.mContainer = (LinearLayout) view.findViewById(R.id.ball);

        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                MainActivity.mBouncingBallView = new SimulationClass(getContext(), event.getX(), event.getY());
                MainActivity.mContainer.addView(MainActivity.mBouncingBallView, 0);
                return true;
            }
        });

        if (MainActivity.mBitmap != null && !MainActivity.mBitmap.isRecycled())
            try {
                ProcessTask.initRotateImageIfRequired();
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
            new ProcessTask(getContext(), this, requestCode, resultCode, data, R.id.imageViewGallery).execute();
        } else {
            this.getFragmentManager().popBackStack();
            ((MainActivity)getActivity()).moveToHome();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mImageFile != null && getView() != null && MainActivity.mBitmap != null && !MainActivity.mBitmap.isRecycled())
            try {
                ProcessTask.setRotateImageIfRequired(newConfig);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (b) {
            try {
                if (mImageFile != null && getView() != null && MainActivity.mBitmap != null && !MainActivity.mBitmap.isRecycled()) {
                    MainActivity.mBitmap = BitmapFactory.decodeFile(this.mImageFile.getAbsolutePath());
                    ProcessTask.initRotateImageIfRequired();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        b = false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        b = true;
    }
}
