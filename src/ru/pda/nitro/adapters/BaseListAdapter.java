package ru.pda.nitro.adapters;
import android.widget.*;
import java.util.*;
import ru.forpda.interfaces.forum.*;

public abstract class BaseListAdapter extends BaseAdapter
{
	public abstract void setData(ArrayList<? extends IListItem> data);
}
