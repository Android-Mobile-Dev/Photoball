package team6.photoball;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class MenuDialog extends DialogFragment {

    public static MenuDialog newInstance(int title) {
        MenuDialog frag = new MenuDialog();
        Bundle args = new Bundle();
        args.putInt("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt("title");
        int icon = (title == R.string.updates_title) ? R.mipmap.ic_flag_black_24dp : R.mipmap.ic_info_black_24dp;
        int description = (title == R.string.updates_title) ? R.string.updates_description : R.string.about_description;

        return new AlertDialog.Builder(getActivity())
                .setIcon(icon)
                .setTitle(title)
                .setPositiveButton((R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        ((MainActivity) getActivity()).doPositiveClick();
                    }
                }).setMessage(description)
                .create();
    }
}