package ru.pda.nitro.widgets;
import android.app.*;
import android.content.*;
import android.os.*;
import android.util.*;

public class App extends Application
{
	private ServiceConnection connect;
	private UpdateWidgetsService service;
	private boolean bind = false;
	private int widgetsCount = 0;
	Intent intent;
	
	private static App INSTANCE = null;

    public App() {
        INSTANCE = this;
    }
	
    public static App getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new App();
        }

        return INSTANCE;
    }

	@Override
	public void onCreate()
	{
		super.onCreate();
		connect = new ServiceConnection(){

			@Override
			public void onServiceConnected(ComponentName p1, IBinder binder)
			{
				service = ((UpdateWidgetsService.MyBinder) binder).getService(); 
			}

			@Override
			public void onServiceDisconnected(ComponentName p1)
			{
				setBind(false);
			}
		};
		
		intent = new Intent(this, UpdateWidgetsService.class);
		startMyService();
		}
	
	public void startMyService(){
		startService(intent);
		bindMyService();
	}
	
	public void stopMyServise(){
		if(getWidgetsCount() == 0){
		unbindMyServise();
		stopService(intent);
		}
	}
		
	private void bindMyService(){
		setBind(bindService(intent, connect, BIND_AUTO_CREATE));
	}
	
	private void unbindMyServise(){
		if(isBind()){
		 unbindService(connect);
		 setBind(false);
		 }
	}
	
	
	
	public UpdateWidgetsService getMyServise(){
			
		return service;
	}
	
	public void setWidgetsCount(int widgetsCount)
	{
		this.widgetsCount = widgetsCount;
	}

	public int getWidgetsCount()
	{
		return widgetsCount;
	}

	public void setBind(boolean bind)
	{
		this.bind = bind;
	}

	public boolean isBind()
	{
		return bind;
	}
}
