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

public class GroopsDialogFragment extends BaseDialogFragment
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
		setTopicId(getArguments().getCharSequence(GROOPS_ID_KEY));
		setTopicTitle(getArguments().getCharSequence(GROOPS_TITLE_KEY));
		
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
			if(getTopicId() != null && getTopicTitle() != null)
			addToGroup(p2);	
			else{
			Toast.makeText(getActivity(), "Ошибка!", Toast.LENGTH_SHORT).show();
			}
		}
	
	};
	
	private void addToGroup(final int position){
	 	Handler handler = new Handler();
		handler.post(new Runnable(){

				@Override
				public void run()
				{
					if(isAddGroup(getActivity(),getTopicId()) < 0){
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
		cv.put(Contract.Groop.id, getTopicId().toString());
		cv.put(Contract.Groop.title, getTopicTitle().toString());
		getActivity().getContentResolver().insert(ContentUris.withAppendedId(Contract.Groops.CONTENT_URI, l).buildUpon().appendPath("Groop").build(), cv);
		Toast.makeText(getActivity(), "Тема успешно добавленна в группу", Toast.LENGTH_SHORT).show();
		
		}
	

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		cursor.close();
	}
		
		
		
    
}
