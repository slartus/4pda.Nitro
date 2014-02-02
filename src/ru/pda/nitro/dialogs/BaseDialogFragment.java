package ru.pda.nitro.dialogs;
import android.support.v4.app.DialogFragment;

public class BaseDialogFragment extends DialogFragment
{
	private CharSequence topicId;
	private CharSequence topicTitle;

	public void setTopicTitle(CharSequence topicTitle)
	{
		this.topicTitle = topicTitle;
	}

	public CharSequence getTopicTitle()
	{
		return topicTitle;
	}
	


	public void setTopicId(CharSequence topicId)
	{
		this.topicId = topicId;
	}

	public CharSequence getTopicId()
	{
		return topicId;
	}}
