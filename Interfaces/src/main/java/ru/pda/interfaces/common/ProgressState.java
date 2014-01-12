package ru.pda.interfaces.common;

/**
 * Created by slartus on 12.01.14.
 */
public abstract class ProgressState {
    public abstract void update(String message, int percents);

    public boolean isCanceled() {
        return m_Canceled;
    }

    private Boolean m_Canceled = false;

    public void cancel() {
        m_Canceled = true;
    }
}
