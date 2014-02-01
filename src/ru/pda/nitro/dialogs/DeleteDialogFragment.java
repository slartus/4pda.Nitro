package ru.pda.nitro.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import ru.pda.nitro.database.*;
import android.content.*;
import android.widget.*;

public class DeleteDialogFragment extends DialogFragment {

    private Uri mUri;
	private long l;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUri = getArguments().getParcelable("_uri");
		l = getArguments().getLong("_id");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Улалить?")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().getContentResolver().delete(mUri, null, null);
						String selection = Contract.Groop.groop + "=?";
						String[] selectionArgs = { String.valueOf(l) };
						mUri = Contract.Groop.CONTENT_URI;
						getActivity().getContentResolver().delete(mUri, selection, selectionArgs);
							
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DeleteDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
