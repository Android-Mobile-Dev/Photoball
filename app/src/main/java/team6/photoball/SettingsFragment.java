package team6.photoball;

import android.os.Bundle;
import android.support.v4.preference.PreferenceFragment;

/**
 * Created by rosar on 3/30/2016.
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

}