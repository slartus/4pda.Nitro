package ru.pda.nitro.widgets;
import android.app.Service;
import android.content.*;
import android.os.*;
import java.io.*;
import android.widget.*;
import java.util.*;
import ru.forpda.interfaces.*;
import ru.pda.nitro.*;

public class UpdateWidgetsService extends Service
{
	@Override
	public void onCreate()
	{
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		
		UpdateData(this);
		return Service.START_STICKY;
	}
	
	
	
	public static void UpdateData(Context context){
		Update update = new Update(context);
		update.execute();
	}


	public static class Update extends AsyncTask<Void, Void, ArrayList<? extends ListInfo>>
	{
		private Context context;

		public Update(Context context){
			this.context = context;
		}

		@Override
		protected ArrayList<ListInfo> doInBackground(Void[] p1)
		{

			return TopicsWidget.inBackground();
		}

		@Override
		protected void onPostExecute(ArrayList<ListInfo> result)
		{
			super.onPostExecute(result);
			if(result != null){
				TopicsWidget.inPostExecute(context, result);
			}
			context.stopService(new Intent(context, UpdateWidgetsService.class));
			WidgetsHelper.updateAllWidgets(context);
		}


	}
	
	@Override
	public IBinder onBind(Intent p1)
	{
		return null;
	}

}
