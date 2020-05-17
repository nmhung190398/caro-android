package com.nmhung.caro.utils;

import android.content.Context;
import android.view.View;

import androidx.core.content.ContextCompat;

public class ViewUtils {
    Context context;

    private static ViewUtils ins;

    public static ViewUtils of(Context context) {
        if (ins == null) {
            ins = new ViewUtils();
        }
        ins.context = context;
        return ins;
    }

    private ViewUtils() {
    }

    public void setBackground(View view, int id) {
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(ContextCompat.getDrawable(context, id));
        } else {
            view.setBackground(ContextCompat.getDrawable(context, id));
        }

    }
}
