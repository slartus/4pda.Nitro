package ru.pda.http;

/**
 * Created by slinkin on 01.07.13.
 */
public class AppHttpStatus {
    public static String getReasonPhrase(int code, String defaultPhrase) {
        switch (code) {
            case 500:
                return "Внутренняя ошибка сервера";
            case 501:
                return "Не реализовано";
            case 502:
                return "Плохой шлюз";
            case 503:
                return "Сервис недоступен";
            case 504:
                return "Шлюз не отвечает";
            case 505:
                return "Версия HTTP не поддерживается";
            case 506:
                return "Вариант тоже согласован";
            case 507:
                return "Переполнение хранилища";
            case 509:
                return "Исчерпана пропускная ширина канала";
            case 510:
                return "Не расширено";
        }
        return defaultPhrase;
    }
}
