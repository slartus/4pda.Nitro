package ru.pda.nitro.profile;
import android.support.v4.app.*;
import android.os.*;
import ru.pda.nitro.bricks.BricksProfile.*;
import ru.pda.nitro.*;

public class ProfileFragment extends Fragment
{

	private String getTitle(){
		return ProfileBrick.TITLE;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onActivityCreated(savedInstanceState);
		BaseState.setMTitle(getTitle());
		getActivity().getActionBar().setTitle(getTitle());
		
	}
	
	
}
