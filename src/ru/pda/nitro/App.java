package ru.pda.nitro;

import android.app.Application;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import android.content.Context;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.*;
import android.graphics.*;
import com.nostra13.universalimageloader.cache.memory.impl.*;
import com.nostra13.universalimageloader.cache.disc.naming.*;
import com.nostra13.universalimageloader.core.assist.*;
import android.os.*;

/**
 * Created by slartus on 12.01.14.
 */
public class App extends Application {
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
		initImageLoader(getApplicationContext());
	}
	public static void initImageLoader(Context context) {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.no_image)
			.showImageForEmptyUri(R.drawable.no_image)
			.cacheInMemory(true)
			.cacheOnDisc(true)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.handler(new Handler())
			.build();
		//	File cacheDir = StorageUtils.getCacheDirectory(context, "UniversalImageLoader/Cache");

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
			.threadPoolSize(5)
			.threadPriority(Thread.MIN_PRIORITY + 2)
			.denyCacheImageMultipleSizesInMemory()

			.memoryCache(new UsingFreqLimitedMemoryCache(5 * 1024 * 1024)) // 2 Mb
			.discCacheFileNameGenerator(new HashCodeFileNameGenerator())
			//  .imageDownloader(new DefaultImageDownloader(5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)
			.defaultDisplayImageOptions(options)

			//	.memoryCacheSize(3000000)
			//	.discCacheSize(50000000)
			
			.build();
		
		ImageLoader.getInstance().init(config);
	}
}
