package com.emma.general_backend_library.models.viewModels.inputViewModels;

import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

public class InputStringViewModel extends InputViewModel<String> {
    public InputStringViewModel() {
    }

    public InputStringViewModel(String value) {
        super(value);
    }
}
