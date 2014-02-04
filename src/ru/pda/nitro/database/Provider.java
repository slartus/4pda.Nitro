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
	
	private static final int GROOP = 9;
	private static final int GROOP_ID = 10;
	
	private static final int GROOPS = 5;
	private static final int GROOPS_ID = 6;
	private static final int GROOPS_ID_GROOP = 7;
	private static final int GROOPS_ID_GROOP_ID = 8;
	
	

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
                qb.setTables(Contract.News.TABLE_NAME);
                defaultSortOrder = Contract.News.DEFAULT_SORT_ORDER;
				break;
			
			case FAVORITE:
                qb.setTables(Contract.Favorite.TABLE_NAME);
                defaultSortOrder = Contract.Favorite.DEFAULT_SORT_ORDER;
				break;
	
			case GROOP:
                qb.setTables(Contract.Groop.TABLE_NAME);
                defaultSortOrder = Contract.Groop.DEFAULT_SORT_ORDER;
				break;
			case GROOPS:
                qb.setTables(Contract.Groops.TABLE_NAME);
                defaultSortOrder = Contract.Groops.DEFAULT_SORT_ORDER;
				break;
			case GROOPS_ID:
                qb.setTables(Contract.Groops.TABLE_NAME);
                defaultSortOrder = Contract.Groops.DEFAULT_SORT_ORDER;
				qb.appendWhere("_id=" + uri.getLastPathSegment());
				break;
			case GROOPS_ID_GROOP:
                qb.setTables(Contract.Groop.TABLE_NAME);
                defaultSortOrder = Contract.Groop.DEFAULT_SORT_ORDER;
				qb.appendWhere(Contract.Groop.groop + "=" + uri.getPathSegments().get(1));
				break;
			case GROOPS_ID_GROOP_ID:
                qb.setTables(Contract.Groops.TABLE_NAME);
                defaultSortOrder = Contract.Groops.DEFAULT_SORT_ORDER;
				qb.appendWhere(Contract.Groop.groop + "=" + uri.getPathSegments().get(1));
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
			case GROOPS:
				return Contract.Groop.CONTENT_TYPE;
			case GROOPS_ID:
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
                tableName = Contract.News.TABLE_NAME;
                break;
			case FAVORITE:
                tableName = Contract.Favorite.TABLE_NAME;
                break;
			case GROOPS:
                tableName = Contract.Groops.TABLE_NAME;
                break;
			case GROOPS_ID_GROOP:
				values.put(Contract.Groop.groop, uri.getPathSegments().get(1));
                tableName = Contract.Groop.TABLE_NAME;

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
			case NEWS:
                tableName = Contract.News.TABLE_NAME;
                break;
			case FAVORITE:
                tableName = Contract.Favorite.TABLE_NAME;
                break;
			case GROOPS_ID:
                tableName = Contract.Groops.TABLE_NAME;
                selection = "_id=" + uri.getLastPathSegment() + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
				break;
			case GROOP:
                tableName = Contract.Groop.TABLE_NAME;
                break;
			case GROOP_ID:
                tableName = Contract.Groop.TABLE_NAME;
                selection = "_id=" + uri.getLastPathSegment() + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
				break;
			case GROOPS_ID_GROOP_ID:
                tableName = Contract.Groop.TABLE_NAME;
                selection = Contract.Groop.groop + "=" + uri.getPathSegments().get(1) + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
				selection = "_id=" + uri.getLastPathSegment() + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
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
                tableName = Contract.News.TABLE_NAME;
                selection = "_id=" + uri.getLastPathSegment() + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
				break;
			case FAVORITE_ID:
                tableName = Contract.Favorite.TABLE_NAME;
                selection = "_id=" + uri.getLastPathSegment() + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
				break;
			case GROOPS_ID:
                tableName = Contract.Groops.TABLE_NAME;
                selection = "_id=" + uri.getLastPathSegment() + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
				break;
			case GROOPS_ID_GROOP_ID:
                tableName = Contract.Groops.TABLE_NAME;
                selection = Contract.Groop.groop + "=" + uri.getPathSegments().get(1) + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
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
		
		sUriMatcher.addURI(Contract.AUTHORITY, "Groop", GROOP);
		sUriMatcher.addURI(Contract.AUTHORITY, "Groop/#", GROOP_ID);
		
		
		sUriMatcher.addURI(Contract.AUTHORITY, "Группы", GROOPS);
		sUriMatcher.addURI(Contract.AUTHORITY, "Группы/#", GROOPS_ID);
		sUriMatcher.addURI(Contract.AUTHORITY, "Группы/#/Groop", GROOPS_ID_GROOP);
		sUriMatcher.addURI(Contract.AUTHORITY, "Группы/#/Groop/#", GROOPS_ID_GROOP_ID);
		
        sUriMatcher.addURI(Contract.AUTHORITY, "Favorite", FAVORITE);
		sUriMatcher.addURI(Contract.AUTHORITY, "Favorite/#", FAVORITE_ID);
		sUriMatcher.addURI(Contract.AUTHORITY, "News", NEWS);
		sUriMatcher.addURI(Contract.AUTHORITY, "News/#", NEWS_ID);

    }

}
