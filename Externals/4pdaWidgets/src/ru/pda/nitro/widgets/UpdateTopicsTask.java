package ru.pda.nitro.widgets;
import java.util.*;
import ru.forpda.interfaces.forum.*;
import ru.forpda.api.*;
import java.io.*;
import java.text.*;
import ru.forpda.http.*;
import ru.pda.nitro.*;
import ru.forpda.interfaces.*;
import android.os.*;
import ru.pda.nitro.listfragments.*;
import android.content.*;
import ru.pda.nitro.database.*;
import android.util.*;

public class UpdateTopicsTask
{
	private static ListInfo listInfo;
	private static ArrayList<Topic> topics;
	
	public static void UpdateData(Context context){
		Update update = new Update(context);
		update.execute();
	}
	
	@Override
    private static ArrayList<Topic> getTopicsList() throws ParseException, IOException
	{

        return TopicsApi.getFavorites(new HttpHelper(App.getInstance()), listInfo);
    }
	
	
	private static class Update extends AsyncTask<Void, Void, Boolean>
	{
		private static Context context;
		
		public Update(Context context){
			this.context = context;
			listInfo = new ListInfo();
		}
		
		@Override
		protected Boolean doInBackground(Void[] p1)
		{
			try
			{
			 	 topics = getTopicsList();
				 return true;
			}
			catch (ParseException e)
			{}
			catch (IOException e)
			{}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result)
		{
			super.onPostExecute(result);
			if(result){
				BaseListFragment.deleteAllLocalData(context, Contract.Favorite.CONTENT_URI);
				TopicsListFragment.setLocalData(context, topics, Contract.Favorite.CONTENT_URI);
			}
			context.stopService(new Intent(context, UpdateWidgetsService.class));
			WidgetsHelper.updateAllWidgets(context);
			}
		
		
	}
}
