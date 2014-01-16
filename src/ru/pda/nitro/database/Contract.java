package ru.pda.nitro.database;

import android.net.Uri;
import android.provider.*;

public final class Contract
{
    private Contract()
    {}

    public static final String AUTHORITY = "ru.pda.nitro.database";
    
    public static final class Favorite
    {
		private Favorite()
		{}

		public static final String id = "id";
		public static final String title = "title";
		public static final String lastAvtor = "lastAvtor";
		public static final String lastDate = "lastDate";
		public static final String forumTitle = "forumTitle";
		public static final String description = "description";
		public static final String hasUnreadPosts = "hasUnreadPosts";
		
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/Favorite");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + ".Favorite";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + ".Favorite";

		public static final String DEFAULT_SORT_ORDER = BaseColumns._ID + " DESC";

    }
	
	public static final class News
    {
		private News()
		{}

		public static final String id = "id";
		public static final String title = "title";
		public static final String author = "author";
		public static final String newsDate = "newsDate";
		public static final String page = "page";
		public static final String description = "description";
		public static final String imgUrl = "imgUrl";

		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/News");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + ".News";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + ".News";

		public static final String DEFAULT_SORT_ORDER = BaseColumns._ID + " DESC";

    }
    


}
