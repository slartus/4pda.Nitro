package ru.pda.nitro.topicsview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import ru.pda.nitro.BaseActivity;
import ru.pda.nitro.R;
import android.view.*;
import android.content.*;
import android.net.*;

/**
 * Created by slinkin on 21.01.14.
 */
public class TopicActivity extends BaseActivity {
    public static final String TOPIC_ID_KEY = "ru.pda.nitro.topicsview.TOPIC_ID_KEY";
    public static final String TOPIC_URL_KEY = "ru.pda.nitro.topicsview.TOPIC_URL_KEY";
    public static final String NAVIGATE_ACTION_KEY = "ru.pda.nitro.topicsview.NAVIGATE_ACTION_KEY";
    public static final String TOPIC_TITLE_KEY = "ru.pda.nitro.topicsview.TOPIC_TITLE_KEY";
	public static final String TOPIC_LIST_TITLE_KEY = "ru.pda.nitro.topicsview.TOPIC_LIST_TITLE_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topic_layout);
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.topic, new TopicView())
			.commit();
			}
        
        Intent intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                if (extras.containsKey(TOPIC_LIST_TITLE_KEY))
                    setTitle(extras.getString(TOPIC_LIST_TITLE_KEY));
            }
        }
		
		
    }
	
	public static void show(Activity activity, Uri uri, CharSequence title, long l){
		Intent intent = new Intent(activity.getApplicationContext(), TopicActivity.class); 
		intent.putExtra("_uri", ContentUris.withAppendedId(uri,l).buildUpon().appendPath("Groop").build());
	//	intent.putExtra("_uri", ContentUris.withAppendedId(uri,l));
		intent.putExtra(TopicActivity.TOPIC_LIST_TITLE_KEY,title);
		
		activity.startActivity(intent);
		}

    public static void show(Activity activity, CharSequence topicId,
                            CharSequence navigateAction) {
        show(activity, topicId, null, null, navigateAction);
    }

    public static void show(Activity activity, CharSequence topicId, CharSequence topicTitle, CharSequence topicListTitle,
                            CharSequence navigateAction) {
        Intent intent = new Intent(activity.getApplicationContext(), TopicActivity.class);
		
	 		intent.putExtra(TOPIC_ID_KEY, topicId);
        if (!TextUtils.isEmpty(navigateAction))
            intent.putExtra(NAVIGATE_ACTION_KEY, navigateAction);
        if (!TextUtils.isEmpty(topicTitle))
            intent.putExtra(TOPIC_TITLE_KEY, topicTitle);
		if (!TextUtils.isEmpty(topicListTitle))
            intent.putExtra(TOPIC_LIST_TITLE_KEY, topicListTitle);
        activity.startActivity(intent);
    }
}
