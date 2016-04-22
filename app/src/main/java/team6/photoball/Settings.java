package team6.photoball;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.support.v4.app.DialogFragment;
import android.support.v4.preference.PreferenceFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.pavelsikun.seekbarpreference.SeekBarPreference;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Settings.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Settings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Settings extends PreferenceFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private CheckBoxPreference mInstructionPreference;
    private CheckBoxPreference mSoundPreference;
    private Preference mDefaultPreference;
    private SeekBarPreference mSizePreference;
    private SeekBarPreference mSpeedPreference;

    public Settings() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Settings.
     */
    // TODO: Rename and change types and number of parameters
    public static Settings newInstance(String param1, String param2) {
        Settings fragment = new Settings();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onPrepareOptionsMenu (Menu menu) {
        MenuItem item = menu.findItem(R.id.action_settings);
        item.setEnabled(false);
        ColorFilter filter = new LightingColorFilter(Color.BLACK, Color.GRAY);
        item.getIcon().setColorFilter(filter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final SharedPreferences prefs = getPreferenceManager().getDefaultSharedPreferences(this.getActivity());

        onPrepareOptionsMenu(((MainActivity)this.getActivity()).mMenu);

        Preference mBackgroundPreference = getPreferenceManager().findPreference("background_preference_key");
        final int background_color = prefs.getInt("background_preference_key",0);
        if(background_color==0xffffffff)
            mBackgroundPreference.setSummary(getResources().getString(R.string.setting_color_value));
        mBackgroundPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference pref) {
                pref.setSummary("");
                return true;
            }
        });

        mSoundPreference = (CheckBoxPreference) getPreferenceManager().findPreference("sound_preference_key");
        Boolean b = prefs.getBoolean("sound_preference_key", true);
        if (b) {
            mSoundPreference.setSummary(getResources().getString(R.string.setting_sound_on));
            ((MainActivity)getActivity()).soundOn();
        }
        else {
            mSoundPreference.setSummary(getResources().getString(R.string.setting_sound_off));
            ((MainActivity)getActivity()).soundOff();
        }
        mSoundPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if ((boolean) newValue) {
                    mSoundPreference.setSummary(getResources().getString(R.string.setting_sound_on));
                    ((MainActivity)getActivity()).soundOn();
                }
                else {
                    mSoundPreference.setSummary(getResources().getString(R.string.setting_sound_off));
                    ((MainActivity)getActivity()).soundOff();
                }
                SharedPreferences.Editor ed = prefs.edit();
                ed.putBoolean("sound_preference_key", (boolean) newValue);
                ed.apply();
                return true;
            }
        });

        mInstructionPreference = (CheckBoxPreference) getPreferenceManager().findPreference("instruction_preference_key");
        Boolean x = prefs.getBoolean("instruction_preference_key", true);
        mInstructionPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
//                if ((boolean) newValue) {
//                    mSoundPreference.setSummary(getResources().getString(R.string.setting_sound_on));
//                    ((MainActivity)getActivity()).soundOn();
//                }
//                else {
//                    mSoundPreference.setSummary(getResources().getString(R.string.setting_sound_off));
//                    ((MainActivity)getActivity()).soundOff();
//                }
                SharedPreferences.Editor ed = prefs.edit();
                ed.putBoolean("instruction_preference_key", (boolean) newValue);
                ed.apply();
                return true;
            }
        });

        mDefaultPreference = getPreferenceManager().findPreference("default_preference_key");
        mDefaultPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                showDialog();
                return true;
            }
        });
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteractionSettings(uri);
        }
    }

    public void onAttach(Context context) {
        super.onAttach((MainActivity) context);
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

    void showDialog() {
        DialogFragment newFragment = Default.newInstance(
                R.string.setting_default);
        newFragment.show(this.getActivity().getSupportFragmentManager(), "dialog");
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
        void onFragmentInteractionSettings(Uri uri);
    }
}
