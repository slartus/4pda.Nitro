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

public class AddGroopsDialogFragment extends DialogFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        View view = layoutInflater.inflate(R.layout.dialog_rename, null);
        final TextView textView = (TextView) view.findViewById(R.id.name);
        

        builder.setMessage("Новая группа")
			.setView(view)
			.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					ContentValues cv = new ContentValues();
					cv.put(Contract.Groops.title, String.valueOf(textView.getText()));
					getActivity().getContentResolver().insert(Contract.Groops.CONTENT_URI, cv);
				}
			})
			.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					AddGroopsDialogFragment.this.getDialog().cancel();
				}
			});
        return builder.create();
    }
}
