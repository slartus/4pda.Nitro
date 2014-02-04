package ru.forpda.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

/**
 * Created by slinkin on 04.02.14.
 */
public class Log {
    public static void d(CharSequence e) {
        android.util.Log.e(TAG, e.toString());
    }

    private static final String TAG = "ru.pda.nitro.Log";

    private static String getClassName(Class<?> clazz) {
        if (clazz != null) {
            if (!TextUtils.isEmpty(clazz.getName())) {
                return clazz.getName();
            }
            if (!TextUtils.isEmpty(clazz.getSimpleName())) {
                return clazz.getSimpleName();
            }

            return getClassName(clazz.getEnclosingClass());
        }

        return "";
    }

    private static String getLocation() {
        final String className = Log.class.getName();
        final StackTraceElement[] traces = Thread.currentThread()
                .getStackTrace();
        boolean found = false;

        for (int i = 0; i < traces.length; i++) {
            StackTraceElement trace = traces[i];

            try {
                if (found) {
                    if (!trace.getClassName().startsWith(className)) {
                        Class<?> clazz = Class.forName(trace.getClassName());
                        return "[" + getClassName(clazz) + ":"
                                + trace.getMethodName() + ":"
                                + trace.getLineNumber() + "]: ";
                    }
                } else if (trace.getClassName().startsWith(className)) {
                    found = true;
                    continue;
                }
            } catch (ClassNotFoundException e) {
            }
        }

        return "[]: ";
    }

    private static CharSequence getMessage(Throwable ex) {
        CharSequence message = ex.getLocalizedMessage();
        if (TextUtils.isEmpty(message))
            message = ex.getMessage();
        if (TextUtils.isEmpty(message))
            message = ex.toString();
        return message;
    }

    public static void e(Context context, Throwable ex) {
        String exLocation = getLocation();
        android.util.Log.e(TAG, exLocation + ex);
        tryShowMessageDialog(context, ex);

    }

    public static void e(CharSequence e) {
        String exLocation = getLocation();
        android.util.Log.e(TAG, exLocation + e);
    }

    public static void e(Context context, CharSequence message, Throwable ex) {
        String exLocation = getLocation();
        android.util.Log.e(TAG, exLocation + ex);
        tryShowMessageDialog(context, ex);
    }

    private static boolean tryShowMessageDialog(Context context, Throwable ex) {
        try {
            new AlertDialog.Builder(context)
                    .setTitle("Ошибка")
                    .setMessage(getMessage(ex))
                    .setCancelable(true)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();
            return true;
        } catch (Throwable ignoredEx) {
            return false;
        }
    }
}
