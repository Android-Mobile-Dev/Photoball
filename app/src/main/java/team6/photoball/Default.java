package team6.photoball;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.preference.PreferenceFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by rosar on 3/31/2016.
 */
public class Default extends DialogFragment {

    private Settings settings = new Settings();

    public static Default newInstance(int title) {
        Default frag = new Default();
        Bundle args = new Bundle();
        args.putInt("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt("title");

        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setPositiveButton((R.string.setting_reset),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                doPositiveClick();
                            }
                        }).setNegativeButton((R.string.setting_cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                doNegativeClick();
                            }
                        }).setMessage(R.string.setting_default_description)
                .create();

    }

    public void doPositiveClick() {
        //reset to default value and clear stored images
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        SharedPreferences.Editor ed = prefs.edit();
        ed.putBoolean("sound_preference_key", true);
        ed.putString("preset_preference_key", getResources().getString(R.string.setting_preset_3d));
        ed.apply();
        getFragmentManager().beginTransaction().replace(R.id.the_screens, new Settings()).commit();
    }

    public void doNegativeClick() {
        //return back to setting screen
    }
}