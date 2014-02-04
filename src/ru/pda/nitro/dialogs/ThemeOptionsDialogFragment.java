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

public class ThemeOptionsDialogFragment extends BaseDialogFragment
{
	public static final String THEME_OPTIONS_ID_KEY = "ru.pda.nitro.dialogs.ThemeOptionsDialogFragment.THEME_OPTIONS_ID_KEY";
	public static final String THEME_OPTIONS_TITLE_KEY = "ru.pda.nitro.dialogs.ThemeOptionsDialogFragment.THEME_OPTIONS_TITLE_KEY";
	

	public static ThemeOptionsDialogFragment newInstance(CharSequence topicId, CharSequence topicTitle) {
		ThemeOptionsDialogFragment f = new ThemeOptionsDialogFragment();

		// Supply num input as an argument.
		Bundle args = new Bundle();
		args.putCharSequence(THEME_OPTIONS_ID_KEY, topicId);
		args.putCharSequence(THEME_OPTIONS_TITLE_KEY, topicTitle);
		f.setArguments(args);

		return f;
	}

    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
		setTopicId(getArguments().getCharSequence(THEME_OPTIONS_ID_KEY));
		setTopicTitle(getArguments().getCharSequence(THEME_OPTIONS_TITLE_KEY));
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		String[] data = {"Добавить в избранное", "Удалить из избранного", "Подписаться", "Отписаться", "Добавить в группу", "Удалить из группы"};
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
				TopicThemeOptionsTask.changeStatus(getActivity(), TopicThemeOptionsTask.ADD_TO_FAVORITE_KEY, getTopicId(), null, null);
				break;
			case 1:
				TopicThemeOptionsTask.changeStatus(getActivity(), TopicThemeOptionsTask.REMOVE_FROM_FAVORITE_KEY, getTopicId(), null, null);
				break;
			case 2:
				showSelectEmailIdDialog();
				break;
			case 3:
				TopicThemeOptionsTask.changeStatus(getActivity(), TopicThemeOptionsTask.UN_SUBSCRIBE_KEY, getTopicId(), null, null);
				break;
			case 4:
				showGroopsDialog(getTopicId(), getTopicTitle());
				break;
			case 5:
				removeFromGroup();
				break;
			}
		}

	};
	
	private void removeFromGroup(){
		
		long id = GroopsDialogFragment.isAddGroup(getActivity(), getTopicId());
		if(id > 0){
			getActivity().getContentResolver().delete(ContentUris.withAppendedId(Contract.Groop.CONTENT_URI, id), null,null);
			Toast.makeText(getActivity(), "Тема удалена из группы", Toast.LENGTH_SHORT).show();	
	}
	}

	private void showGroopsDialog(CharSequence topicId, CharSequence topicTitle){

        DialogFragment dialogFrag = GroopsDialogFragment.newInstance(topicId, topicTitle);
        dialogFrag.show(getFragmentManager().beginTransaction(), "dialog");

	}

	private void showSelectEmailIdDialog(){
		DialogFragment dialog = SelectEmailId.newInstance(getTopicId());
		dialog.show(getFragmentManager().beginTransaction(), "dislogs");
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
					TopicThemeOptionsTask.changeStatus(getActivity(), TopicThemeOptionsTask.SUBSCRIBE_KEY, getArguments().getCharSequence(THEME_OPTIONS_ID_KEY) ,profile.getAutchKey(),values[selected[0]] );	
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
