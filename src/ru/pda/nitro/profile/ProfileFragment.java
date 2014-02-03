package ru.pda.nitro.profile;
import android.support.v4.app.*;
import android.os.*;
import ru.pda.nitro.bricks.BricksProfile.*;
import ru.pda.nitro.*;
import android.app.*;

public class ProfileFragment extends BaseFragment
{

	@Override
	public void getData()
	{
		// TODO: Implement this method
	}


	private String getTitle(){
		return ProfileBrick.TITLE;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onActivityCreated(savedInstanceState);
		getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		
		BaseState.setMTitle(getTitle());
		getActivity().getActionBar().setTitle(getTitle());
		
	}
	
	
}
