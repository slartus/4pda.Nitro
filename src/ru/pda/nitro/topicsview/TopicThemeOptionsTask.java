package ru.pda.nitro.topicsview;
import android.os.*;
import ru.forpda.api.*;
import ru.forpda.http.*;
import ru.pda.nitro.*;
import java.io.*;
import android.app.*;
import android.content.*;
import android.widget.*;
import ru.forpda.interfaces.*;
import android.util.*;

public class TopicThemeOptionsTask extends AsyncTask<Void, Void, ResultInfo>
{
	public static final int ADD_TO_FAVORITE_KEY = 1;
	public static final int REMOVE_FROM_FAVORITE_KEY = 2;
	public static final int SUBSCRIBE_KEY = 3;
	public static final int UN_SUBSCRIBE_KEY = 4;
	private ProgressDialog progressDialog;

	private CharSequence topicId;
	private CharSequence autchKey;
	private String emailType;
	private Context context;
	private int position;

	public TopicThemeOptionsTask(Context context, int position, CharSequence topicId, CharSequence autchKey, String emailTupe)
	{
		this.topicId = topicId;
		this.emailType = emailTupe;
		this.autchKey = autchKey;
		this.context = context;
		this.position = position;

	}
	
	public static void changeStatus(Context context, int position, CharSequence topicId, CharSequence autchKey, String emailTupe){
	    TopicThemeOptionsTask	taskChangeFavorite = new TopicThemeOptionsTask(context,position, topicId, autchKey, emailTupe);
		taskChangeFavorite.execute();
	}

	@Override
	protected void onPreExecute()
	{
		super.onPreExecute();
		setToast("Добавление...");
	}


	@Override
	protected ResultInfo doInBackground(Void[] p1)
	{
		try
		{
			TopicApi api = new TopicApi();
			CharSequence forumId = api.getForumId(new HttpHelper(App.getInstance()), topicId);
			if (forumId != null)
			{
				switch (position)
				{
					case ADD_TO_FAVORITE_KEY:
						return TopicApi.addToFavorites(new HttpHelper(App.getInstance()), forumId, topicId);
					case REMOVE_FROM_FAVORITE_KEY:
						return TopicApi.removeFromFavorites(new HttpHelper(App.getInstance()), forumId, topicId);
					case SUBSCRIBE_KEY:
						return TopicApi.subscribe(new HttpHelper(App.getInstance()), forumId, autchKey, topicId, emailType);
					case UN_SUBSCRIBE_KEY:
						return TopicApi.unSubscribe(new HttpHelper(App.getInstance()), topicId);
					default:
						return null;

				}
			}
		}
		catch (IOException e)
		{
		}
		return null;
	}

	@Override
	protected void onPostExecute(ResultInfo result)
	{
		super.onPostExecute(result);
		if (result != null)
		{
			if(!result.isSuccess())
				setToast("Не удалось добавить");
		}
		else		
			setToast("Ошибка подключения");

	}


	private void setToast(String message)
	{
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

	}


}
