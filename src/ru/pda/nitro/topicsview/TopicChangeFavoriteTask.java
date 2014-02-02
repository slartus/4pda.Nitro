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

public class TopicChangeFavoriteTask extends AsyncTask<Void, Void, ResultInfo>
{
	private ProgressDialog progressDialog;
	private CharSequence topicId;
	private Context context;
	private boolean status;
	public TopicChangeFavoriteTask(Context context, CharSequence topicId, boolean status)
	{
		this.topicId = topicId;
		this.context = context;
		this.status = status;
	}

	@Override
	protected void onPreExecute()
	{
		super.onPreExecute();
		progressDialog = new ProgressDialog(context);
		progressDialog.setMessage("Отправка запроса");
		progressDialog.show();
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
				if (status)
					return TopicApi.addToFavorites(new HttpHelper(App.getInstance()), forumId, topicId);
				else 
					return TopicApi.removeFromFavorites(new HttpHelper(App.getInstance()), forumId, topicId);
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
		progressDialog.dismiss();
		if (result != null)
		{
			if (status)
			{
			    setToast(result.getMessage());
			}
			else
			{
				if (result.isSuccess())
					setToast("Тема успешно удалена из избранного");
				else
					setToast("Выбраная тема не найденна в избранном");
			}
		}
		else
			setToast("Ошибка подключения");

	}

	private void setToast(String message)
	{
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

	}


}
