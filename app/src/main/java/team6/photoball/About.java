package team6.photoball;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class About extends DialogFragment {

    public static About newInstance(int title) {
        About frag = new About();
        Bundle args = new Bundle();
        args.putInt("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt("title");

        return new AlertDialog.Builder(getActivity())
                .setIcon(R.mipmap.ic_info_black_24dp)
                .setTitle(title)
                .setPositiveButton((R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        ((MainActivity) getActivity()).doPositiveClick();
                    }
                }).setMessage(R.string.about_description)
                .create();
    }
}