package team6.photoball;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyPicMaps.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class MyPicMaps extends Fragment {

    private OnFragmentInteractionListener mListener;

    public MyPicMaps() {

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
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (((MainActivity)getActivity()).getSupportActionBar() != null) {
            ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        View view = inflater.inflate(R.layout.fragment_my_pic_maps, container, false);

        Fragment gridFragment = GridFragment.newInstance("","");
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.addToBackStack("fragment_my_pic_maps_detail");
        transaction.add(R.id.my_pic_maps_screens, gridFragment).commit();

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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteractionMyPicMaps(uri);
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
        void onFragmentInteractionMyPicMaps(Uri uri);
    }
}