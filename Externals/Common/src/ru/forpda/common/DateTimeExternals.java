package ru.forpda.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by slartus on 12.01.14.
 */
public class DateTimeExternals {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    private static SimpleDateFormat parseDateTimeFormat = new SimpleDateFormat("dd.MM.yyyy, HH:mm");

    /**
     * Сегодня в формате dd.MM.yyyy
     * @return
     */
    public static String getTodayString() {
        GregorianCalendar nowCalendar = new GregorianCalendar();
        return dateFormat.format(nowCalendar.getTime());
    }

    public static String getDateString(Date date) {
        return dateFormat.format(date);
    }

    public static String getDateTimeString(Date date) {
        return dateTimeFormat.format(date);
    }

    /**
     * Вчера в формате dd.MM.yyyy
     * @return
     */
    public static String getYesterdayString() {
        GregorianCalendar nowCalendar = new GregorianCalendar();
        nowCalendar.add(Calendar.DAY_OF_MONTH, -1);
        return dateFormat.format(nowCalendar.getTime());
    }

    /**
     * Парсит дату, которую отдаёт форум
     * @param dateTime строка с датой, в том числе "вчера" и "сегодня"
     * @param today Дата "вчера", чтобы не приходилось её в методе каждый раз в цикле брать
     * @param yesterday Дата "сегодня", чтобы не приходилось её в методе каждый раз в цикле брать
     * @return
     * @throws ParseException
     */
    public static Date parseForumDateTime(String dateTime, String today, String yesterday) throws ParseException {
        try {
            Date res = parseDateTimeFormat.parse(dateTime.toString().replace("Сегодня", today).replace("Вчера", yesterday));
            if (res.getYear() < 100)
                res.setYear(2000 + res.getYear());
            return res;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
