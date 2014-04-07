package ru.pda.nitro.adapters;
import android.widget.*;
import android.view.*;
import java.util.*;
import ru.forpda.interfaces.forum.*;
import android.content.*;
import ru.pda.nitro.R;
import android.text.*;

import com.nostra13.universalimageloader.core.ImageLoader;
import android.graphics.Typeface;
import com.nostra13.universalimageloader.core.assist.*;
import android.graphics.*;

public class NewsListAdapter extends BaseListAdapter
{
//	private ArrayList<News> newsList;
	private NewsList newsList;
	private Typeface face;
	private Context context;
	final LayoutInflater inflater;
	private ImageLoader imageLoader;
	private boolean list_small = true;
	
	public NewsListAdapter(Context context, NewsList newsList, ImageLoader imageLoader){
		inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		face = Typeface.createFromAsset(context.getAssets(), "4pda/fonts/RobotoSlab-Light.ttf");
		this.imageLoader = imageLoader;
		this.newsList = newsList;
		this.context = context;		
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
			view = inflater.inflate(!list_small ? R.layout.item_news : R.layout.item_news_smal, parent, false);
			holder = new ViewHolder();
			holder.mProgressBar = (LinearLayout)view.findViewById(R.id.mProgressBar);
			holder.linInfo =(LinearLayout)view.findViewById(R.id.linearInfo);
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
		holder.textDescription.setText(data.getDescription());
		holder.textTitle.setText(data.getTitle());
		if(data.getImgUrl() != null)
			imageLoader.displayImage(data.getImgUrl().toString(), holder.imageImage, new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String p1, View p2)
					{
						p2.setVisibility(View.GONE);
						holder.mProgressBar.setVisibility(View.VISIBLE);
					}

					@Override
					public void onLoadingFailed(String p1, View p2, FailReason p3)
					{
						holder.mProgressBar.setVisibility(View.INVISIBLE);
					}

					@Override
					public void onLoadingComplete(String p1, View p2, Bitmap p3)
					{
						p2.setVisibility(View.VISIBLE);
						holder.mProgressBar.setVisibility(View.INVISIBLE);
					}

					@Override
					public void onLoadingCancelled(String p1, View p2)
					{
						// TODO: Implement this method
					}
			});
		if(!list_small){
			holder.linInfo.setVisibility(View.VISIBLE);
			holder.textComments.setText(String.valueOf(data.getCommentsCount()));
			holder.textAutor.setText(data.getAuthor());
			holder.textDate.setText(data.getNewsDate());
	
		if(data.getTagTitle() != null){
			holder.textTag.setVisibility(View.VISIBLE);
			holder.textTag.setText(data.getTagTitle());
			}
		if(data.getSourceTitle() != null){
			holder.textSource.setVisibility(View.VISIBLE);
			holder.textSource.setText("Источник: " + data.getSourceTitle());
		}
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
		public LinearLayout mProgressBar;
		public LinearLayout linInfo;
	}

}
