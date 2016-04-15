package team6.photoball;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.io.File;


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

        final ImageView image = (ImageView) view.findViewById(R.id.imageViewMyPicMapsDetail);
        image.setImageBitmap(null);
        Picasso.with(this.getContext())
                .load(iFile)
                .into(image);

        LinearLayout container_ = (LinearLayout) view.findViewById(R.id.linearLayoutMyPicMapsDetail);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getContext());

        container_.setBackgroundColor(prefs.getInt("background_preference_key",0));

        container_.addView(new SimulationClass(this.getContext()));

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

}
