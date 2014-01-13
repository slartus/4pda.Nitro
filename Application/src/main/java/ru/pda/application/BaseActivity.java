package ru.pda.application;
import android.app.*;
import android.os.*;

public class BaseActivity extends Activity
{
	public ActionBar ab;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		ab = getActionBar();
	}
	
}
