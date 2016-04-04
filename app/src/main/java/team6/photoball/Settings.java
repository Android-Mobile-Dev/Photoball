package team6.photoball;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.preference.PreferenceFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

import yuku.ambilwarna.widget.AmbilWarnaPreference;


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
    private ListPreference mPresetListPreference;
    private CheckBoxPreference mSoundPreference;
    private Preference mDefaultPreference;

    private SoundPool mSounds;
    private HashMap<Integer, Integer> mSoundIDMap;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final SharedPreferences prefs = getPreferenceManager().getDefaultSharedPreferences(this.getActivity());

        Preference mBackgroundPreference = getPreferenceManager().findPreference("background_preference_key");
        final int background_color = prefs.getInt("background_preference_key",0);
        if(background_color==0xffffffff)
            mBackgroundPreference.setSummary(getResources().getString(R.string.setting_color_value));
        mBackgroundPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            public boolean onPreferenceClick(Preference pref)
            {
                pref.setSummary("");
                SharedPreferences.Editor ed = prefs.edit();
                ed.putInt("red_background_key", background_color & 0xffff0000);
                ed.putInt("green_background_key", background_color & 0xff00ff00);
                ed.putInt("blue_background_key", background_color & 0xff0000ff);
                ed.apply();
                return true;
            }
        });

        Preference mBallPreference = getPreferenceManager().findPreference("ball_preference_key");
        final int ball_color = prefs.getInt("ball_preference_key",0);
        mBallPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            public boolean onPreferenceClick(Preference pref)
            {
                SharedPreferences.Editor ed = prefs.edit();
                ed.putInt("red_ball_key", ball_color & 0xffff0000);
                ed.putInt("green_ball_key", ball_color & 0xff00ff00);
                ed.putInt("blue_ball_key", ball_color & 0xff0000ff);
                ed.apply();
                return true;
            }
        });

        mPresetListPreference = (ListPreference) getPreferenceManager().findPreference("preset_preference_key");
        mPresetListPreference.setSummary(prefs.getString("preset_preference_key", getResources().getString(R.string.setting_preset_3d)));
        mPresetListPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                mPresetListPreference.setSummary((CharSequence) newValue);
                SharedPreferences.Editor ed = prefs.edit();
                ed.putString("preset_preference_key", newValue.toString());
                ed.apply();
                return true;
            }
        });

        mSoundPreference = (CheckBoxPreference) getPreferenceManager().findPreference("sound_preference_key");
        Boolean b = prefs.getBoolean("sound_preference_key", true);
        if (b) {
            mSoundPreference.setSummary(getResources().getString(R.string.setting_sound_on));
        }
        else
            mSoundPreference.setSummary(getResources().getString(R.string.setting_sound_off));
        mSoundPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if ((boolean) newValue) {
                    mSoundPreference.setSummary(getResources().getString(R.string.setting_sound_on));

                }
                else {
                    mSoundPreference.setSummary(getResources().getString(R.string.setting_sound_off));
                }
                SharedPreferences.Editor ed = prefs.edit();
                ed.putBoolean("sound_preference_key", (boolean) newValue);
                ed.apply();
                return true;
            }
        });

        mDefaultPreference = (Preference) getPreferenceManager().findPreference("default_preference_key");
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
