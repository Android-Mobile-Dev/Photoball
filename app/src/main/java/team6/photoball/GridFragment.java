package team6.photoball;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GridFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GridFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GridFragment extends Fragment implements MyPicMapsPageAdapter.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView mRecyclerView;

    public static List<ImageModel> items = new ArrayList<>();

    static private String mAppDirectoryName = "Photoball";

    static private File mImageRoot = null;

    static private File[] mDirFiles = null;

    static {
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
    }

    private OnFragmentInteractionListener mListener;

    public GridFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GridFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GridFragment newInstance(String param1, String param2) {
        GridFragment fragment = new GridFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void initRecyclerView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mRecyclerView.setHasFixedSize(true); // Helps improve performance
    }

    private void setRecyclerAdapter(RecyclerView recyclerView) {
        MyPicMapsPageAdapter adapter = new MyPicMapsPageAdapter(this.getContext(), items);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.grid_recycler_view, container, false);

        initRecyclerView(view);

        setRecyclerAdapter(mRecyclerView);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getContext());

        mRecyclerView.setBackgroundColor(prefs.getInt("background_preference_key",0));

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteractionGrid(uri);
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
        void onFragmentInteractionGrid(Uri uri);
    }

    @Override
    public void onItemClick(View view, ImageModel viewModel) {
        ((MainActivity)getActivity()).moveToMyPicMapsDetail(view, viewModel);
    }
}
