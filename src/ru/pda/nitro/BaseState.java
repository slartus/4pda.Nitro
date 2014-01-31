package ru.pda.nitro;

public class BaseState
{
	private static boolean groop_menu = false;
	private static  boolean refresh_favorite = true;
	private static boolean refresh_news = true;

	public static void setRefresh_news(boolean refresh_news)
	{
		BaseState.refresh_news = refresh_news;
	}

	public static boolean isRefresh_news()
	{
		return refresh_news;
	}

	public static void setRefresh_favorite(boolean refresh_favorite)
	{
		BaseState.refresh_favorite = refresh_favorite;
	}

	public static boolean isRefresh_favorite()
	{
		return refresh_favorite;
	}


	public static void setGroop_menu(boolean groop_menu)
	{
		BaseState.groop_menu = groop_menu;
	}

	public static boolean isGroop_menu()
	{
		return groop_menu;
	}}
