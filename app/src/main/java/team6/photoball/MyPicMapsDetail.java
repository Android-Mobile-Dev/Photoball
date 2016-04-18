package team6.photoball;

import android.content.Context;
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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
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
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String EXTRA_IMAGE = "team6.photoball.extraImage";
    private static final String EXTRA_TITLE = "team6.photoball.extraTitle";

    // TODO: Rename and change types of parameters
    private View mFromView;
    private ImageModel mViewModel;
    ImageView mImageView = null;
    private Bitmap mBitmap = null;

    private OnFragmentInteractionListener mListener;

    public MyPicMapsDetail() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyPicMapsDetail.
     */
    // TODO: Rename and change types and number of parameters
    public static MyPicMapsDetail create(View fromView, ImageModel viewModel) {
        MyPicMapsDetail fragment = new MyPicMapsDetail();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.setExtras(fromView, viewModel);
        return fragment;
    }

    public void setExtras (View fromView, ImageModel viewModel) {
        mFromView = fromView;
        mViewModel = viewModel;
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

        File iFile = new File(mViewModel.getImage());

        view.setBackgroundColor(getResources().getColor(R.color.black_transparent));

        mImageView = (ImageView) view.findViewById(R.id.imageViewMyPicMapsDetail);

        mBitmap = BitmapFactory.decodeFile(iFile.getAbsolutePath());

        ((MainActivity)this.getActivity()).updateMenu();

        mImageView.setImageBitmap(null);
        Picasso.with(this.getContext())
                .load(iFile)
                .into(mImageView);

        LinearLayout container_ = (LinearLayout) view.findViewById(R.id.linearLayoutMyPicMapsDetail);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getContext());

        container_.setBackgroundColor(prefs.getInt("background_preference_key",0));

        container_.addView(new SimulationClass(this.getContext()));

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

        if (savedInstanceState != null) {
            mBitmap = stringToBitMap(savedInstanceState.getString("mpm_bitmap"));
        }

        if (mBitmap != null)
            try {
                initRotateImageIfRequired();
            } catch (IOException e) {
                e.printStackTrace();
            }

        return view;
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        try {
            return this.getActivity().dispatchTouchEvent(motionEvent);
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteractionMyPicMapsDetail(uri);
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
        void onFragmentInteractionMyPicMapsDetail(Uri uri);
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
        if (mBitmap != null) outState.putString("mpm_bitmap", bitMapToString(mBitmap));
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

    private void initRotateImageIfRequired() throws IOException {
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
