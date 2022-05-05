package com.emma.general_backend_library.models.viewModels.inputViewModels;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

public class InputViewModel<T> extends BaseObservable {
    private T value;
    private String errorMessage;
    private InputViewModelValueChangedListener<T> listener;

    public InputViewModel() {
    }

    public InputViewModel(T value) {
        this.value = value;
    }

    @Bindable
    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
        notifyPropertyChanged(BR.value);
        if(listener != null){
            listener.onValueChanged(value);
        }
    }

    @Bindable
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        notifyPropertyChanged(BR.errorMessage);
        if(listener != null){
            listener.onErrorMessageChanged(errorMessage);
        }
    }

    public void setListener(InputViewModelValueChangedListener<T> listener) {
        this.listener = listener;
    }
}
