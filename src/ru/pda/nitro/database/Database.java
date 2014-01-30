package ru.pda.nitro.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.database.*;
import java.util.*;
import android.content.*;

public class Database extends SQLiteOpenHelper
{
	private static final String DATABASE_NAME = "nitro.db";
    private static final int DATABASE_VERSION = 6;
	
	static final String NEWS_TABLE = "News";
    static final String FAVORITE_TABLE = "Favorite";
	static final String GROUP_TABLE = "Group";
	

    public Database(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
		db.execSQL("CREATE TABLE " + GROUP_TABLE + " ("
				   + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				   + Contract.Group.id + " TEXT,"
				   + Contract.Group.title + " TEXT"
				   + ")");

		db.execSQL("CREATE TABLE " + FAVORITE_TABLE + " ("
				   + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				   + Contract.Favorite.id + " TEXT,"
				   + Contract.Favorite.title + " TEXT,"
				   + Contract.Favorite.lastAvtor + " TEXT,"
				   + Contract.Favorite.lastDate + " TEXT,"
				   + Contract.Favorite.description + " TEXT,"
				   + Contract.Favorite.hasUnreadPosts + " INTEGER,"
				   + Contract.Favorite.forumTitle + " TEXT"
				   
				   + ")");

		db.execSQL("CREATE TABLE " + NEWS_TABLE + " ("
				   + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				   + Contract.News.category + " TEXT,"
				   + Contract.News.author + " TEXT,"
				   + Contract.News.description + " TEXT,"
				   + Contract.News.id + " TEXT,"
				   + Contract.News.imgUrl + " TEXT,"
				   + Contract.News.newsDate + " TEXT,"
				   + Contract.News.page + " INTEGER,"
				   + Contract.News.title + " TEXT,"
				   + Contract.News.commentsCount + " INTEGER,"
				   + Contract.News.sourceTitle + " TEXT,"
				   + Contract.News.sourseUrl + " TEXT,"
				   + Contract.News.tagLink + " TEXT,"
				   + Contract.News.tagName + " TEXT,"
				   + Contract.News.tagTitle + " TEXT"
				   + ")");
				   
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
	/*	Cursor cursor = db.query(NEWS_TABLE, null, null, null, null, null, Contract.News.DEFAULT_SORT_ORDER);
		LocalDataHelper.getLocalNews(cursor);
		cursor.close();
		*/
		db.execSQL("DROP TABLE IF EXISTS " + GROUP_TABLE);
		
		db.execSQL("DROP TABLE IF EXISTS " + FAVORITE_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + NEWS_TABLE);
		onCreate(db);

    }
	
	
	
}
