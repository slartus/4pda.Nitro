package ru.pda.nitro.widgets;
import android.app.Service;
import android.content.*;
import android.os.*;
import java.io.*;
import android.widget.*;
import java.util.*;
import ru.forpda.interfaces.*;
import ru.pda.nitro.WidgetsHelper;
import android.util.*;

public class UpdateWidgetsService extends Service
{
	public MyBinder binder = new MyBinder();
	
	@Override
	public void onCreate()
	{
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		
		return Service.START_STICKY;
	}
	
	public void UpdateData(Context context, String listKey){
		Update update = new Update(context, listKey);
		update.execute();
	}


	public class Update extends AsyncTask<Void, Void, ArrayList<? extends ListInfo>>
	{

		private Context context;
		private String listKey;

		public Update(Context context, String listKey){
			this.context = context;
			this.listKey = listKey;
		}
		
		@Override
		protected ArrayList<? extends ListInfo> doInBackground(Void[] p1)
		{
			switch(listKey){
				case TopicsWidget.TOPICS_WIDGET_KEY:
					return TopicsWidget.inBackground();
				case NewsWidget.NEWS_WIDGET_KEY:
					return NewsWidget.inBackground();
			}
			return null;
		}
		

		@Override
		protected void onPostExecute(ArrayList<ListInfo> result)
		{
			super.onPostExecute(result);
			if(result != null){
				switch(listKey){
					case TopicsWidget.TOPICS_WIDGET_KEY:
						TopicsWidget.inPostExecute(context, result);
					break;
					case NewsWidget.NEWS_WIDGET_KEY:
					   NewsWidget.inPostExecute(context, result);
					break;
				}
			}
		WidgetsHelper.updateAllWidgets(context);
		}


	}
	
	public IBinder onBind(Intent intent)
	{
		return binder;
	}

	public void onRebind(Intent intent)
	{
		super.onRebind(intent);
	}

	public boolean onUnbind(Intent intent)
	{
		return true;
	}

	public class MyBinder extends Binder
	{
		public UpdateWidgetsService getService()
		{ 
			return UpdateWidgetsService.this; 
		} 
	}
}
