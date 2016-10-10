package com.zhonghong.xqshijie.util;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.ReplacementTransformationMethod;

/**
 * Created by xiezl on 16/6/24.
 */
public class HidePasswordUtil extends ReplacementTransformationMethod {
    private static char[] ORIGINAL = new char[] { '\r' };
    private static char[] REPLACEMENT = new char[] { '\uFEFF' };

    /**
     * The character to be replaced is \r.
     */
    protected char[] getOriginal() {
        return ORIGINAL;
    }

    /**
     * The character that \r is replaced with is \uFEFF.
     */
    protected char[] getReplacement() {
        return REPLACEMENT;
    }

    public static HideReturnsTransformationMethod getInstance() {
        if (sInstance != null)
            return sInstance;

        sInstance = new HideReturnsTransformationMethod();
        return sInstance;
    }

    private static HideReturnsTransformationMethod sInstance;
}
