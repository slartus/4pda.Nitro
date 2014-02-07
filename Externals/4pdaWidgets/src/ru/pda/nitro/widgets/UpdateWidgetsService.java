package ru.pda.nitro.widgets;
import android.app.*;
import android.content.*;
import android.os.*;

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
		UpdateTopicsTask.UpdateData(this);
		return Service.START_STICKY;
	}

	
	@Override
	public IBinder onBind(Intent p1)
	{
		return null;
	}

}
