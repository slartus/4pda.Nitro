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
import ru.pda.nitro.topicsview.*;
import ru.pda.nitro.R;
import ru.forpda.api.*;
import ru.pda.nitro.*;

public class ThemeOptionsDialogFragment extends DialogFragment
{
	private Cursor cursor;
	public static final String THEME_OPTIONS_ID_KEY = "ru.pda.nitro.dialogs.ThemeOptionsDialogFragment.THEME_OPTIONS_ID_KEY";

	

	public static ThemeOptionsDialogFragment newInstance(CharSequence topicId) {
		ThemeOptionsDialogFragment f = new ThemeOptionsDialogFragment();

		// Supply num input as an argument.
		Bundle args = new Bundle();
		args.putCharSequence(THEME_OPTIONS_ID_KEY, topicId);
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
		String[] data = {"Добавить в избранное", "Удалить из избранного", "Подписаться", "Отписаться"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),	android.R.layout.simple_list_item_1, data);
		builder.setAdapter(adapter, myClickListener);

		return builder.create();
	}

	OnClickListener myClickListener = new OnClickListener() {

		@Override
		public void onClick(DialogInterface p1, int p2)
		{
			switch(p2){
			case 0:
				TopicChangeStatusTask.changeStatus(getActivity(), TopicChangeStatusTask.ADD_TO_FAVORITE_KEY, getArguments().getCharSequence(THEME_OPTIONS_ID_KEY), null, null);
				break;
			case 1:
				TopicChangeStatusTask.changeStatus(getActivity(), TopicChangeStatusTask.REMOVE_FROM_FAVORITE_KEY, getArguments().getCharSequence(THEME_OPTIONS_ID_KEY), null, null);
				break;
			case 2:
				showSelectEmailIdDialog();
				break;
			case 3:
				TopicChangeStatusTask.changeStatus(getActivity(), TopicChangeStatusTask.UN_SUBSCRIBE_KEY, getArguments().getCharSequence(THEME_OPTIONS_ID_KEY), null, null);
				break;
			}
		}

	};

	private void showSelectEmailIdDialog(){
		DialogFragment dialog = new SelectEmailId().newInstance(getArguments().getCharSequence(THEME_OPTIONS_ID_KEY));
		dialog.show(getFragmentManager().beginTransaction(), "dislogs");
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		cursor.close();
	}

	
	public static class SelectEmailId extends DialogFragment{

		
		public static SelectEmailId newInstance(CharSequence topicId) {
			SelectEmailId f = new SelectEmailId();

			// Supply num input as an argument.
			Bundle args = new Bundle();
			args.putCharSequence(THEME_OPTIONS_ID_KEY, topicId);
			f.setArguments(args);

			return f;
		}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		CharSequence[] titles = new CharSequence[]{"Не уведомлять", "Уведомление с задержкой", "Немедленное уведомление", "Ежедневное уведомление", "Еженедельное уведомление"};
		final String[] values = new String[]{TopicApi.SUBSCRIBE_EMAIL_TYPE_NONE, TopicApi.SUBSCRIBE_EMAIL_TYPE_DELAYED, TopicApi.SUBSCRIBE_EMAIL_TYPE_IMMEDIATE, TopicApi.SUBSCRIBE_EMAIL_TYPE_DAILY, TopicApi.SUBSCRIBE_EMAIL_TYPE_WEEKLY};
		final int[] selected = {2};
		return new AlertDialog.Builder(getActivity())
			.setSingleChoiceItems(titles, selected[0], new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
					selected[0] = i;
				}
			})
			.setTitle(R.string.default_action)
			.setPositiveButton("Подписаться",
			new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					UserProfile profile = new UserProfile();
					if(profile.isLogined()){
					TopicChangeStatusTask.changeStatus(getActivity(), TopicChangeStatusTask.SUBSCRIBE_KEY, getArguments().getCharSequence(THEME_OPTIONS_ID_KEY) ,profile.getAutchKey(),values[selected[0]] );	
					}
				}
			}
		)
			.setNeutralButton("Отмена",
			new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
															
				}
			}
		)
			.create();
	}
	}
}
