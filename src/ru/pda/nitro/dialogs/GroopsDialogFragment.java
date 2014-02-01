package ru.pda.nitro.dialogs;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.graphics.*;
import android.content.pm.*;
import android.util.*;
import android.database.*;
import ru.pda.nitro.database.*;
import android.content.DialogInterface.OnClickListener;
import android.net.*;
import android.provider.*;
import android.support.v4.app.DialogFragment;

public class GroopsDialogFragment extends DialogFragment
{
	private Cursor cursor;
	public static final String GROOPS_ID_KEY = "ru.pda.nitro.dialogs.GroopsDialogFragment.GROOPS_ID_KEY";
	public static final String GROOPS_TITLE_KEY = "ru.pda.nitro.dialogs.GroopsDialogFragment.GROOPS_TITLE_KEY";
	private static long topicBaseId;
	
	
	public static GroopsDialogFragment newInstance(CharSequence topicId, CharSequence topicTitle) {
		GroopsDialogFragment f = new GroopsDialogFragment();

		// Supply num input as an argument.
		Bundle args = new Bundle();
		args.putCharSequence(GROOPS_ID_KEY, topicId);
		args.putCharSequence(GROOPS_TITLE_KEY, topicTitle);
		f.setArguments(args);

		return f;
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
		cursor = getActivity().getContentResolver().query(Contract.Groops.CONTENT_URI, null, null, null, Contract.Groops.DEFAULT_SORT_ORDER);
	
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
	{
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        
		builder.setTitle("Группы");
		builder.setCursor(cursor, myClickListener, Contract.Groops.title);
			
		
		 return builder.create();
	}

	OnClickListener myClickListener = new OnClickListener() {

		@Override
		public void onClick(DialogInterface p1, int p2)
		{
			addToGroup(getArguments().getCharSequence(GROOPS_ID_KEY), p2);			
		}
	
	};
	
	private void addToGroup(final CharSequence id,final int position){
	 	Handler handler = new Handler();
		handler.post(new Runnable(){

				@Override
				public void run()
				{
					if(isAddGroup(getActivity(),id) < 0){
						saveToSelectGroup(position);
					}else{
						Toast.makeText(getActivity(), "Выбрана тема уже добавленна в группу!", Toast.LENGTH_SHORT).show();
					}
					
				}
			});
	}
	
	public static long isAddGroup(Activity activity, CharSequence topicId){
		Cursor cursor = activity.getContentResolver().query(Contract.Groop.CONTENT_URI, null, null, null, Contract.Groop.DEFAULT_SORT_ORDER);
		if(cursor.moveToFirst()){
			do{
				if(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Groop.id)).equals(topicId)){
					topicBaseId = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));
					cursor.close();
					return topicBaseId;
				}
			}while(cursor.moveToNext());
		}
		cursor.close();
		return -1;
	}
	
	private void saveToSelectGroup(int position){
		cursor.moveToPosition(position);
		long l = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));
		ContentValues cv = new ContentValues();
		cv.put(Contract.Groop.id, getArguments().getCharSequence(GROOPS_ID_KEY).toString());
		cv.put(Contract.Groop.title, getArguments().getCharSequence(GROOPS_TITLE_KEY).toString());
		getActivity().getContentResolver().insert(ContentUris.withAppendedId(Contract.Groops.CONTENT_URI, l).buildUpon().appendPath("Groop").build(), cv);
		
	}
	

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		cursor.close();
	}
		
		
		
    
}
