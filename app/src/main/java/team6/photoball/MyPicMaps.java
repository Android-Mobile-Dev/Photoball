package team6.photoball;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyPicMaps.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class MyPicMaps extends Fragment implements MyPicMapsPageAdapter.OnItemClickListener {

    private RecyclerView mRecyclerView;

    public static List<ImageModel> items = new ArrayList<>();

    static private String mAppDirectoryName = "Photoball";

    static private File[] mDirFiles = null;

    private OnFragmentInteractionListener mListener;

    public MyPicMaps() {
        setRetainInstance(true);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MyPicMaps.
     */
    // TODO: Rename and change types and number of parameters
    public static MyPicMaps create() {
        MyPicMaps fragment = new MyPicMaps();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_pic_maps, container, false);

        if (MainActivity.mBitmap != null && !MainActivity.mBitmap.isRecycled()) {

            MainActivity.mBitmap.recycle();

            MainActivity.mBitmap = null;
        }

        items.clear();

        try {
            File mImageRoot = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES) + "/" + mAppDirectoryName + "/");
            mDirFiles = mImageRoot.listFiles();

            for (int i = 0; i < mDirFiles.length; i++) {
                int t = i + 1;
                items.add(new ImageModel("Item " + t, mDirFiles[i].getAbsolutePath()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (items.isEmpty()) view.findViewById(R.id.noMedia).setVisibility(View.VISIBLE);
        else view.findViewById(R.id.noMedia).setVisibility(View.INVISIBLE);

        initRecyclerView(view);

        setRecyclerAdapter(mRecyclerView);

        ((MainActivity)this.getActivity()).updateMenu();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getContext());

        mRecyclerView.setBackgroundColor(prefs.getInt("background_preference_key",0));

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
        playButton.setScaleX((float) 1.3);
        playButton.setScaleY((float) 1.3);
        playButton.setY(-100);

        return view;
    }

    private void initRecyclerView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mRecyclerView.setHasFixedSize(true); // Helps improve performance
    }

    private void setRecyclerAdapter(RecyclerView recyclerView) {
        MyPicMapsPageAdapter adapter = new MyPicMapsPageAdapter(this.getContext());
        adapter.setOnItemClickListener(this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(String filePath) {
        ((MainActivity)getActivity()).moveToMyPicMapsDetail(filePath);
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
        void onFragmentInteractionMyPicMaps(Uri uri);
    }
}