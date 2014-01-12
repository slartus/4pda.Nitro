package ru.pda.application.listfragments;

import android.app.Fragment;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import ru.pda.interfaces.forum.IListItem;

/**
 * Created by slartus on 12.01.14.
 * Базовый класс для списков
 * Здесь общие свойства и методы для фрагментов списков
 */
public abstract class BaseListFragment extends Fragment {
    public abstract ArrayList<? extends IListItem> getList() throws ParseException, IOException;

    public abstract String getName();

    public abstract String getTitle();


}
