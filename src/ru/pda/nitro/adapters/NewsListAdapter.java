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
import android.graphics.*;

public class NewsListAdapter extends BaseAdapter
{
	private NewsList newsList;
	private Typeface face;
	private Context context;
	final LayoutInflater inflater;
	private DisplayImageOptions options;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	
	public NewsListAdapter(Context context, NewsList newsList){
		inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		face = Typeface.createFromAsset(context.getAssets(), "4pda/fonts/RobotoSlab-Light.ttf");
		
		this.newsList = newsList;
		this.context = context;
		
		options = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.no_image)
			.showImageForEmptyUri(R.drawable.no_image)
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
	
	public void setData(NewsList data){
		this.newsList = data;
	}
	
	@Override
	public int getCount()
	{
		return newsList.size();
	}

	@Override
	public Object getItem(int p1)
	{
		return newsList.get(p1);
	}

	@Override
	public long getItemId(int p1)
	{
		return p1;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent)
	{
		final ViewHolder holder;
		if(view == null){
			view = inflater.inflate(R.layout.item_news, parent, false);
			holder = new ViewHolder();
			holder.textSource = (TextView)view.findViewById(R.id.textSource);
			holder.textComments = (TextView)view.findViewById(R.id.textComments);
			holder.textTag =(TextView)view.findViewById(R.id.textTag);
			holder.imageImage = (ImageView)view.findViewById(R.id.imageImage);
			holder.textAutor = (TextView)view.findViewById(R.id.textAvtor);
			holder.textDate = (TextView)view.findViewById(R.id.textDate);
			holder.textDescription = (TextView)view.findViewById(R.id.textDescription);
			holder.textTitle = (TextView)view.findViewById(R.id.textTitle);
			holder.textTitle.setTypeface(face);
			view.setTag(holder);

		}else{
			holder = (ViewHolder) view.getTag();
		}
		News data = newsList.get(position);
		holder.textComments.setText(String.valueOf(data.getCommentsCount()));
		holder.textAutor.setText(data.getAuthor());
		holder.textDate.setText(data.getNewsDate());
		holder.textDescription.setText(data.getDescription());
		holder.textTitle.setText(data.getTitle());
		if(data.getImgUrl() != null)
		imageLoader.displayImage(data.getImgUrl().toString(), holder.imageImage, options);
		if(data.getTagTitle() != null){
			holder.textTag.setVisibility(View.VISIBLE);
			holder.textTag.setText(data.getTagTitle());
			}
		if(data.getSourceTitle() != null){
			holder.textSource.setVisibility(View.VISIBLE);
			holder.textSource.setText("Источник: " + data.getSourceTitle());
		}
	
		return view;
	}
	
	public class ViewHolder{
		public ImageView imageImage;
		public TextView textTitle;
		public TextView textDate;
		public TextView textDescription;
		public TextView textAutor;
		public TextView textTag;
		public TextView textComments;
		public TextView textSource;
	}

}
