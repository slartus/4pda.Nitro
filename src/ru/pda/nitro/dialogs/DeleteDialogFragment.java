package ru.pda.nitro.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import ru.pda.nitro.database.Contract;

public class DeleteDialogFragment extends DialogFragment {

    private static final String DELETE_URI_KEY = "ru.pda.nitro.dialogs.DeleteDialogsFragment.DELETE_URI_KEY";
    private static final String DELETE_ID_KEY = "ru.pda.nitro.dialogs.DeleteDialogsFragment.DELETE_ID_KEY";

    private Uri mUri;
    private long id;

    public static DeleteDialogFragment newInstance(Uri mUri, long id) {
        DeleteDialogFragment dialog = new DeleteDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(DELETE_URI_KEY, mUri);
        args.putLong(DELETE_ID_KEY, id);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUri = getArguments().getParcelable(DELETE_URI_KEY);
        id = getArguments().getLong(DELETE_ID_KEY);
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
                        String[] selectionArgs = {String.valueOf(id)};
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
