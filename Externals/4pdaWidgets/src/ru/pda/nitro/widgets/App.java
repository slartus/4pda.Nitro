package ru.pda.nitro.widgets;
import android.app.*;
import android.content.*;
import android.os.*;
import com.nostra13.universalimageloader.core.*;
import com.nostra13.universalimageloader.cache.disc.naming.*;
import com.nostra13.universalimageloader.core.assist.*;
import ru.forpda.common.*;

public class App extends Application
{
	private ServiceConnection connect;
	private UpdateWidgetsService service;
	//есть ли связь с сервисом
	private boolean bind = false;
	//количество виджетов на экране
	private int widgetsCount = 0;
	private boolean log = false;
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
		
		setLog(false);
		
		connect = new ServiceConnection(){

			@Override
			public void onServiceConnected(ComponentName p1, IBinder binder)
			{
				service = ((UpdateWidgetsService.MyBinder) binder).getService(); 
				setBind(true);
				setLog("connect");
			}

			@Override
			public void onServiceDisconnected(ComponentName p1)
			{
				setBind(false);
				setLog("disconnect");
			}
		};
		
		intent = new Intent(this, UpdateWidgetsService.class);

	}
	public void setLog(String text){
		if(isLog())
			Log.d(text);
	}
	
	public static void initImageLoader(Context context) {
	
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
			.threadPriority(Thread.NORM_PRIORITY - 2)
			.denyCacheImageMultipleSizesInMemory()
			.discCacheFileNameGenerator(new Md5FileNameGenerator())
			.tasksProcessingOrder(QueueProcessingType.LIFO)
			//	.writeDebugLogs() // Remove for release app
			.build();
		ImageLoader.getInstance().init(config);
	}
	
	public void startMyService(){
		startService(intent);
		bindMyService();
		setLog("startservise");
	}
	
	public void stopMyServise(){
		//Убиваем сервис при удалении последнего виджета
		if(getWidgetsCount() == 0){
		unbindMyServise();
		stopService(intent);
		}
	}
		
	private void bindMyService(){
		bindService(intent, connect, BIND_AUTO_CREATE);
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
	
	public void setLog(boolean log)
	{
		this.log = log;
	}

	public boolean isLog()
	{
		return log;
	}
}
