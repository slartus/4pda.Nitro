package ru.pda.nitro.bricks;

import android.support.v4.app.Fragment;

/**
 * Created by slartus on 12.01.14.
 */
public abstract class BrickInfo {
    private String name;
    private String title;
	private boolean selected;
	private boolean favorite;
	

    public BrickInfo(String name,String title, boolean selected, boolean favorite){
        this.name=name;
        this.title=title;
		this.selected = selected;
		this.favorite = favorite;
    }

	public void setFavorite(boolean favorite)
	{
		this.favorite = favorite;
	}

	public boolean isFavorite()
	{
		return favorite;
	}

	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}

	public boolean isSelected()
	{
		return selected;
	}


    public String getName() {
        return name;
    }


    public String getTitle() {
        return title;
    }
	
    public abstract Fragment createFragment();
	public abstract Class<? extends Fragment> getClassList();
}
