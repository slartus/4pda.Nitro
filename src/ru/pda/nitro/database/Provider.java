package ru.pda.nitro.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class Provider extends ContentProvider
{

   	private static final int NEWS = 1;
	private static final int NEWS_ID = 2;
	private static final int FAVORITE = 3;
	private static final int FAVORITE_ID = 4;
	private static final int GROUP = 5;
	private static final int GROUP_ID = 6;
	

    private static final UriMatcher sUriMatcher;
    private Database mOpenHelper;

    @Override
    public boolean onCreate()
	{
        mOpenHelper = new Database(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
	{
        String groupBy = null;
        String defaultSortOrder;
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (sUriMatcher.match(uri))
		{
			case NEWS:
				qb.setTables(Database.NEWS_TABLE);
				defaultSortOrder = Contract.News.DEFAULT_SORT_ORDER;
				break;
			case NEWS_ID:
				qb.setTables(Database.NEWS_TABLE);
				defaultSortOrder = Contract.News.DEFAULT_SORT_ORDER;
				qb.appendWhere("_id=" + uri.getLastPathSegment());
				break;
			case FAVORITE:
				qb.setTables(Database.FAVORITE_TABLE);
				defaultSortOrder = Contract.Favorite.DEFAULT_SORT_ORDER;
				break;
			case FAVORITE_ID:
				qb.setTables(Database.FAVORITE_TABLE);
				defaultSortOrder = Contract.Favorite.DEFAULT_SORT_ORDER;
				qb.appendWhere("_id=" + uri.getLastPathSegment());
				break;
			case GROUP:
				qb.setTables(Database.GROUP_TABLE);
				defaultSortOrder = Contract.Groop.DEFAULT_SORT_ORDER;
				break;
			case GROUP_ID:
				qb.setTables(Database.GROUP_TABLE);
				defaultSortOrder = Contract.Groop.DEFAULT_SORT_ORDER;
				qb.appendWhere("_id=" + uri.getLastPathSegment());
				break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        String orderBy;
        if (TextUtils.isEmpty(sortOrder))
		{
            orderBy = defaultSortOrder;
        }
		else
		{
            orderBy = sortOrder;
        }
        Cursor c = qb.query(mOpenHelper.getReadableDatabase(), projection, selection, selectionArgs, groupBy, null, orderBy);
        c.setNotificationUri(getContext().getContentResolver(), uri);

        return c;
    }

    @Override
    public String getType(Uri uri)
	{
        switch (sUriMatcher.match(uri))
		{
           	case NEWS:
				return Contract.News.CONTENT_TYPE;
			case NEWS_ID:
				return Contract.News.CONTENT_ITEM_TYPE;
			case FAVORITE:
				return Contract.Favorite.CONTENT_TYPE;
			case FAVORITE_ID:
				return Contract.Favorite.CONTENT_ITEM_TYPE;
			case GROUP:
				return Contract.Groop.CONTENT_TYPE;
			case GROUP_ID:
				return Contract.Groop.CONTENT_ITEM_TYPE;
				
				default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values)
	{
        String tableName;
        switch (sUriMatcher.match(uri))
		{
            
			case NEWS:
				tableName = Database.NEWS_TABLE;
				break;
			case FAVORITE:
				tableName = Database.FAVORITE_TABLE;
				break;
			case GROUP:
				tableName = Database.GROUP_TABLE;
				break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        long rowId = mOpenHelper.getWritableDatabase().insert(tableName, null, values);
        if (rowId > 0)
		{
            Uri itemUri = ContentUris.withAppendedId(uri, rowId);
            getContext().getContentResolver().notifyChange(itemUri, null);
            return itemUri;
        }
        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
	{
        String tableName;
        switch (sUriMatcher.match(uri))
		{
			
			case NEWS_ID:
				tableName = Database.NEWS_TABLE;
				selection = "_id=" + uri.getLastPathSegment() + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
				break;
			case FAVORITE_ID:
				tableName = Database.FAVORITE_TABLE;
				selection = "_id=" + uri.getLastPathSegment() + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
				break;
	
			case NEWS:
				tableName = Database.NEWS_TABLE;
				break;

			case FAVORITE:
				tableName = Database.FAVORITE_TABLE;
				break;
				
			case GROUP_ID:
				tableName = Database.GROUP_TABLE;
				selection = "_id=" + uri.getLastPathSegment() + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
				break;

			case GROUP:
				tableName = Database.GROUP_TABLE;
				break;
				
			
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        int count =  mOpenHelper.getWritableDatabase().delete(tableName, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
	{
        String tableName;
        switch (sUriMatcher.match(uri))
		{
            
			case NEWS_ID:
				tableName = Database.NEWS_TABLE;
				selection = "_id=" + uri.getLastPathSegment() + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
				break;
			case FAVORITE_ID:
				tableName = Database.FAVORITE_TABLE;
				selection = "_id=" + uri.getLastPathSegment() + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
				break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        int count = mOpenHelper.getWritableDatabase().update(tableName, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    static {

        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		
		sUriMatcher.addURI(Contract.AUTHORITY, "Groop", GROUP);
		sUriMatcher.addURI(Contract.AUTHORITY, "Groop/#", GROUP_ID);
		
        sUriMatcher.addURI(Contract.AUTHORITY, "Favorite", FAVORITE);
		sUriMatcher.addURI(Contract.AUTHORITY, "Favorite/#", FAVORITE_ID);
		sUriMatcher.addURI(Contract.AUTHORITY, "News", NEWS);
		sUriMatcher.addURI(Contract.AUTHORITY, "News/#", NEWS_ID);

    }

}
