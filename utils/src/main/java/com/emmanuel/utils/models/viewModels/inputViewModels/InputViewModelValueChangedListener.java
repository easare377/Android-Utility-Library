package com.emma.general_backend_library.models.viewModels.inputViewModels;

public interface InputViewModelValueChangedListener<T> {
    void onValueChanged(T value);
    void onErrorMessageChanged(String errorMessage);
}
