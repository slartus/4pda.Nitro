package ru.pda.interfaces;

/**
 * Created by slartus on 12.01.14.
 * Служебный класс
 */
public class ListInfo {
    private int from;// начиная с какой страницы или порядкового номера темы

    private int outCount;// тут возвращается сколько всего в листе элементов (сколько всего тем)

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getOutCount() {
        return outCount;
    }

    public void setOutCount(int outCount) {
        this.outCount = outCount;
    }
}
