package ru.forpda.api.infos;

/**
 * Created by slinkin on 21.01.14.
 */
public class ResultInfo {
    private boolean success;
    private String message;

    public ResultInfo(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    /**
     * Результат запроса
     *
     * @return true-успех, false-провал
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Сообщение результата
     * Это может быть как текст ошибки, так и сообщение
     *
     * @return
     */
    public String getMessage() {
        return message;
    }
}
