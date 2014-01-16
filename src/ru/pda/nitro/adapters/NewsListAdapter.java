package ru.pda.nitro.adapters;
import android.widget.*;
import android.view.*;
import java.util.*;
import ru.forpda.interfaces.forum.*;
import android.content.*;
import ru.pda.nitro.R;
import android.text.*;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import android.graphics.Bitmap;

public class NewsListAdapter extends BaseAdapter
{

	private ArrayList<News> news;
	private Context context;
	final LayoutInflater inflater;
	DisplayImageOptions options;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	
	public NewsListAdapter(Context context, ArrayList<News> data){
		inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		
		this.news = data;
		this.context = context;
		
		options = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.empty_state_land)
			.showImageForEmptyUri(R.drawable.empty_state_land)
			.cacheInMemory()
			.cacheOnDisc()
			.bitmapConfig(Bitmap.Config.RGB_565)
			//.decodingType(ImageScaleType.EXACT)
			.build();
			//	File cacheDir = StorageUtils.getCacheDirectory(context, "UniversalImageLoader/Cache");

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
			//.memoryCacheExtraOptions(480, 800) // width, height
			//.discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75) // width, height, compress format, quality
			.threadPoolSize(5)
			.threadPriority(Thread.MIN_PRIORITY + 2)
			.denyCacheImageMultipleSizesInMemory()

			.memoryCache(new UsingFreqLimitedMemoryCache(5 * 1024 * 1024)) // 2 Mb
			//.discCache(new UnlimitedDiscCache(cacheDir))
			.discCacheFileNameGenerator(new HashCodeFileNameGenerator())
			//.imageDownloader(new DefaultImageDownloader(5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)
			.defaultDisplayImageOptions(options)

			.build();
		this.imageLoader.init(config);
	}
	
	public void setData(ArrayList<News> data){
		this.news = data;
	}
	
	@Override
	public int getCount()
	{
		// TODO: Implement this method
		return news.size();
	}

	@Override
	public Object getItem(int p1)
	{
		// TODO: Implement this method
		return news.get(p1);
	}

	@Override
	public long getItemId(int p1)
	{
		// TODO: Implement this method
		return p1;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent)
	{
		final ViewHolder holder;
		if(view == null){
			view = inflater.inflate(R.layout.item_news, parent, false);
			holder = new ViewHolder();
			holder.imageImage = (ImageView)view.findViewById(R.id.imageImage);
			holder.textAutor = (TextView)view.findViewById(R.id.textAvtor);
			holder.textDate = (TextView)view.findViewById(R.id.textDate);
			holder.textDescription = (TextView)view.findViewById(R.id.textDescription);
			holder.textTitle = (TextView)view.findViewById(R.id.textTitle);
			view.setTag(holder);

		}else{
			holder = (ViewHolder) view.getTag();
		}
		News data = news.get(position);
		holder.textAutor.setText(data.getAuthor());
		holder.textDate.setText(data.getNewsDate());
		holder.textDescription.setText(Html.fromHtml(data.getDescription().toString()));
		holder.textTitle.setText(data.getTitle());
		if(data.getImgUrl() != null)
		imageLoader.displayImage(data.getImgUrl().toString(), holder.imageImage, options);
	
		return view;
	}
	
	public class ViewHolder{
		public ImageView imageImage;
		public TextView textTitle;
		public TextView textDate;
		public TextView textDescription;
		public TextView textAutor;
	}

}
