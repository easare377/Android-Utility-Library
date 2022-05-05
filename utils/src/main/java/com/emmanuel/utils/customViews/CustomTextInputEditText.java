package com.emma.general_backend_library.customViews;

import android.content.Context;
import android.text.TextWatcher;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class CustomTextInputEditText extends TextInputEditText {
    private final List<TextWatcher> textWatcherList = new ArrayList<>();
    public CustomTextInputEditText(@NonNull Context context) {
        super(context);
    }

    public CustomTextInputEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomTextInputEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void addTextChangedListener(TextWatcher watcher) {
        super.addTextChangedListener(watcher);
        textWatcherList.add(watcher);
    }

    public void removeAllTextChangedListeners() {
        textWatcherList.clear();
    }
}
