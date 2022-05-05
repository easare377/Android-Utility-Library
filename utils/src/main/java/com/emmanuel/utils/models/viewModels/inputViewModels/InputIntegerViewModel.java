package com.emma.general_backend_library.models.viewModels.inputViewModels;

import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

public class InputIntegerViewModel extends InputViewModel<Integer>{
    public InputIntegerViewModel() {
    }

    public InputIntegerViewModel(Integer value) {
        super(value);
    }
}
