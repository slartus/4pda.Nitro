package ru.pda.nitro.classes;
import android.text.style.*;
import android.util.*;
import android.graphics.*;
import android.content.*;
import android.text.*;

/**
 * Style a {@link Spannable}
with a custom {@link
Typeface}.
 *
 * @author Tristan Waddington
 */
public class TypefacesSpan
extends MetricAffectingSpan {
      /** An <code>LruCache</
code> for previously loaded
typefaces. */
    private static LruCache<String, Typeface>sTypefaceCache = new LruCache<String, Typeface>(12);
    private Typeface mTypeface;
    /**
     * Load the {@link
Typeface} and apply to a
{@link Spannable}.
     */
    public TypefacesSpan(Context context, String typefaceName) {
        mTypeface = sTypefaceCache.get(typefaceName);
        if (mTypeface == null) {
            mTypeface = Typeface.createFromAsset(context.getApplicationContext().getAssets(),/* String.format("4pda/fonts/", */"4pda/fonts/" +typefaceName);
            sTypefaceCache.put(typefaceName, mTypeface);
        }
    }
    @Override
    public void updateMeasureState(TextPaint p) {
        p.setTypeface(mTypeface);
        // Note: This flag isrequired for proper typefacerendering
        p.setFlags(p.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
    }
    @Override
    public void updateDrawState(TextPaint tp)
{
        tp.setTypeface(mTypeface);
        // Note: This flag isrequired for proper typefacerendering
        tp.setFlags(tp.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
    }
}
