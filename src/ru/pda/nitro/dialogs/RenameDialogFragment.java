package ru.pda.nitro.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import ru.pda.nitro.database.*;
import ru.pda.nitro.R;

public class RenameDialogFragment extends DialogFragment {

    private Uri mUri;
    private String mName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUri = getArguments().getParcelable("_uri");
        mName = getArguments().getString("_name");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        View view = layoutInflater.inflate(R.layout.dialog_rename, null);
        final TextView textView = (TextView) view.findViewById(R.id.name);
        textView.setText(mName);

        builder.setMessage("Переименовать")
                .setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(Contract.Groops.title, String.valueOf(textView.getText()));
                        getActivity().getContentResolver().update(mUri, contentValues, null, null);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RenameDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
