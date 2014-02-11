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

public class NewsListAdapter extends BaseListAdapter
{
	private ArrayList<News> newsList;
	private Typeface face;
	private Context context;
	final LayoutInflater inflater;
	private DisplayImageOptions options;
	private ImageLoader imageLoader;
	
	public NewsListAdapter(Context context, ArrayList<News> newsList, ImageLoader imageLoader){
		inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		face = Typeface.createFromAsset(context.getAssets(), "4pda/fonts/RobotoSlab-Light.ttf");
		this.imageLoader = imageLoader;
		this.newsList = newsList;
		this.context = context;
		
		options = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.no_image)
			.showImageForEmptyUri(R.drawable.no_image)
			.cacheInMemory()
			.cacheOnDisc()
			.bitmapConfig(Bitmap.Config.RGB_565)
			.build();
		
	}

	@Override
	public void setData(ArrayList<? extends IListItem> data)
	{
		this.newsList = (NewsList) data;
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
