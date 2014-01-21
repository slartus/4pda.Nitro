package ru.pda.nitro.topicsview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import ru.pda.nitro.BaseActivity;
import ru.pda.nitro.R;

/**
 * Created by slinkin on 21.01.14.
 */
public class TopicActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topic_layout);
    }


    public static void show(Activity activity, CharSequence topicId) {
        Intent intent = new Intent(activity.getApplicationContext(), TopicActivity.class);

        intent.putExtra(TopicView.TOPIC_ID_KEY, topicId);
        activity.startActivity(intent);
    }
}
