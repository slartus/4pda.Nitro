package ru.pda.nitro.adapters;
import android.widget.*;
import android.view.*;
import java.util.*;
import ru.forpda.interfaces.forum.*;
import android.content.*;
import ru.pda.nitro.R;

public class NewsListAdapter extends BaseAdapter
{

	private ArrayList<News> news;
	private Context context;
	final LayoutInflater inflater;
	
	public NewsListAdapter(Context context, ArrayList<News> data){
		inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		
		
		this.news = data;
		this.context = context;
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
		holder.textDescription.setText(data.getDescription());
		holder.textTitle.setText(data.getTitle());
	//	holder.imageFlag.setImageDrawable(null);
	
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
