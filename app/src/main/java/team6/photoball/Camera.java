package team6.photoball;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileOutputStream;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageColorInvertFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilterGroup;
import jp.co.cyberagent.android.gpuimage.GPUImageSobelEdgeDetection;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Camera.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class Camera extends Fragment {

    private Bitmap mBitmap;

    private OnFragmentInteractionListener mListener;

    public Camera() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Camera.
     */
    // TODO: Rename and change types and number of parameters
    public static Camera create() {
        Camera fragment = new Camera();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EasyImage.openCamera(this, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final Camera fragment = this;

        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        final FloatingActionButton addButton = (FloatingActionButton) view.findViewById(R.id.addButton);
        final FloatingActionButton cameraButton = (FloatingActionButton) view.findViewById(R.id.cameraButton);
        final FloatingActionButton playButton = (FloatingActionButton) view.findViewById(R.id.playButton);

        assert addButton != null;
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).moveGallery();
            }
        });

        assert playButton != null;
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).moveMyPicMaps();
            }
        });

        assert cameraButton != null;
        cameraButton.setScaleX((float) 1.3);
        cameraButton.setScaleY((float) 1.3);
        cameraButton.setY(-100);
        assert playButton != null;
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyImage.openCamera(fragment, 0);
            }
        });

        LinearLayout container_ = (LinearLayout) view.findViewById(R.id.linearLayoutCamera);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getContext());

        container_.setBackgroundColor(prefs.getInt("background_preference_key", 0));

        container_.addView(new SimulationClass(getContext()));

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteractionCamera(uri);
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteractionCamera(Uri uri);
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

        ImageView img = (ImageView) this.getView().findViewById(R.id.imageViewCamera);
        img.setImageBitmap(mBitmap);
    }

    public void onCanceled(EasyImage.ImageSource source, int type) {
        //Cancel handling, you might wanna remove taken photo if it was canceled
        if (source == EasyImage.ImageSource.CAMERA) {
            File photoFile = EasyImage.lastlyTakenButCanceledPhoto(this.getContext());
            if (photoFile != null) photoFile.delete();
        }
    }

    private File modifyImage(File imageFile) {

        //Image modification here
        GPUImage gpuImage = new GPUImage(this.getActivity());
        Bitmap bm = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        gpuImage.setImage(bm);
        GPUImageFilterGroup groupFilter = new GPUImageFilterGroup();
        groupFilter.addFilter(new GPUImageSobelEdgeDetection());
        groupFilter.addFilter(new GPUImageColorInvertFilter());
        gpuImage.setFilter(groupFilter);
        Bitmap bmWithFilter = gpuImage.getBitmapWithFilterApplied();
        int q = 100; // quality of compression

        try {
            FileOutputStream fos = new FileOutputStream(imageFile, false);
            bmWithFilter.compress(Bitmap.CompressFormat.PNG, q, fos);
            fos.flush();
            fos.close();

        } catch(Exception e){
            e.printStackTrace();
        }
        return imageFile;
    }

}
