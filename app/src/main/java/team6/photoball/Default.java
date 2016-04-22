package team6.photoball;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import java.io.File;
import java.io.IOException;

/**
 * Created by rosar on 3/31/2016.
 */
public class Default extends DialogFragment {

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
        ed.putInt("background_preference_key", 0xffffffff);
        ed.putInt("ball_preference_key", 0xff006600);
        ed.putBoolean("instruction_preference_key", true);
        ed.apply();

        ((MainActivity)getActivity()).soundOn();

        String appDirectoryName = "Photoball";
        File imageRoot = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES) + "/" + appDirectoryName);
        ContentResolver contentResolver = this.getActivity().getContentResolver();

        if (imageRoot.isDirectory())
        {
            String[] children = imageRoot.list();
            for (int i = 0; i < children.length; i++)
            {
                deleteFileFromMediaStore(contentResolver, new File(imageRoot, children[i]));
            }
        }

        getFragmentManager().popBackStack();
    }

    public void doNegativeClick() {
        //return back to setting screen
    }

    public static void deleteFileFromMediaStore(final ContentResolver contentResolver, final File file) {
        String canonicalPath;
        try {
            canonicalPath = file.getCanonicalPath();
        } catch (IOException e) {
            canonicalPath = file.getAbsolutePath();
        }
        final Uri uri = MediaStore.Files.getContentUri("external");
        final int result = contentResolver.delete(uri,
                MediaStore.Files.FileColumns.DATA + "=?", new String[] {canonicalPath});
        if (result == 0) {
            final String absolutePath = file.getAbsolutePath();
            if (!absolutePath.equals(canonicalPath)) {
                contentResolver.delete(uri,
                        MediaStore.Files.FileColumns.DATA + "=?", new String[]{absolutePath});
            }
        }
    }

}