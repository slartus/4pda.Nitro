package ru.pda.nitro.database;
import java.util.ArrayList;
import ru.forpda.interfaces.forum.News;
import ru.forpda.interfaces.forum.Topic;
import android.database.Cursor;

public class LocalDataHelper
{
	public static ArrayList<News> news;
	private static ArrayList<Topic> topics;
	
	public static ArrayList<News> getLocalNews(Cursor cursor){
		news = new ArrayList<News>();
		if(cursor.moveToFirst()){
			do{
				news.add(getNewsItem(cursor));
			}while(cursor.moveToNext());
		}
		return news;
	}
	
	
	private static News getNewsItem(Cursor cursor){
		News item = new News(null, null);
		item.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(Contract.News.description)));
		item.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(Contract.News.title)));
		item.setId(cursor.getString(cursor.getColumnIndexOrThrow(Contract.News.id)));
		item.setAuthor(cursor.getString(cursor.getColumnIndexOrThrow(Contract.News.author)));
		item.setNewsDate(cursor.getString(cursor.getColumnIndexOrThrow(Contract.News.newsDate)));
		item.setImgUrl(cursor.getString(cursor.getColumnIndexOrThrow(Contract.News.imgUrl)));
		
		item.setTagLink(cursor.getString(cursor.getColumnIndexOrThrow(Contract.News.tagLink)));
		item.setTagName(cursor.getString(cursor.getColumnIndexOrThrow(Contract.News.tagName)));
		item.setTagTitle(cursor.getString(cursor.getColumnIndexOrThrow(Contract.News.tagTitle)));
		
		item.setCommentsCount(cursor.getInt(cursor.getColumnIndexOrThrow(Contract.News.commentsCount)));
		item.setSourceTitle(cursor.getString(cursor.getColumnIndexOrThrow(Contract.News.sourceTitle)));
		item.setSourceUrl(cursor.getString(cursor.getColumnIndexOrThrow(Contract.News.sourseUrl)));
		
		return item;
	}
	
	
}
