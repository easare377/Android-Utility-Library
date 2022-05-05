package com.emma.general_backend_library.customViews;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.SpinnerAdapter;

import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import com.emma.general_backend_library.Functions;
import com.emma.general_backend_library.adapters.DropMenuAdapter;
import com.emma.general_backend_library.interfaces.OnItemSelectedListener;
import com.emma.general_backend_library.models.viewModels.inputViewModels.InputStringViewModel;
import com.emma.general_backend_library.models.viewModels.inputViewModels.InputViewModel;
import com.emma.general_backend_library.models.viewModels.inputViewModels.InputViewModelValueChangedListener;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomSpinner extends TextInputEditText {
    //private LayoutInflater mInflater;
    //private TextInputEditText editText;
    private DropMenuAdapter adapter;
    private Object selectedItem;
    private OnItemSelectedListener onItemSelectedListener;
    //private final List<Object> menuItemList = new ArrayList<>();

    public CustomSpinner(Context context) {
        super(context);
        //mInflater = LayoutInflater.from(context);
        init();
    }

    public CustomSpinner(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //mInflater = LayoutInflater.from(context);
        init();
    }

    public CustomSpinner(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //mInflater = LayoutInflater.from(context);
        init();
    }

    private void init() {
        this.setCursorVisible(false);
        this.setFocusable(false);
        this.setLongClickable(false);
        this.setOnClickListener(view -> {
            //menuItemList.clear();
            showPopupWindow();
        });
    }

    private void showPopupWindow() {
        //Creating the instance of PopupMenu
        DropdownMenuPopup menuPopup = new DropdownMenuPopup(getContext(), adapter);
        //CustomSpinner.this.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        int width = CustomSpinner.this.getWidth();
        //int height = CustomSpinner.this.getMeasuredHeight();
        menuPopup.setWidth(width);
        menuPopup.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        ColorDrawable cd = new ColorDrawable(0xFFFFFFFF);
        menuPopup.setBackgroundDrawable(cd);
        menuPopup.setOutsideTouchable(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            menuPopup.setElevation(4.0F);
        }

        menuPopup.setOnItemSelectedListener((position, item) -> {
            //int index = menuItemList.get(position);
            setSelection(position);
            menuPopup.dismiss();
        });
        menuPopup.setFocusable(true);
        menuPopup.showAsDropDown(this);
        int[] location = new int[2];
        this.getLocationOnScreen(location);
    }

    public DropMenuAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(DropMenuAdapter adapter) {
        this.adapter = adapter;

    }

    public Object getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(Object selectedItem) {
        this.selectedItem = selectedItem;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    public void setSelection(int pos) {
        this.setText(adapter.getItem(pos).toString());
        selectedItem = adapter.getItem(pos);
        if (onItemSelectedListener != null) {
            onItemSelectedListener.onItemSelected(pos, selectedItem);
        }
    }

    //adapter to bind array of objects to spinner.
    @BindingAdapter("bind:entries")
    public static void loadEntries(CustomSpinner spinner, Object[] entries) {
        DropMenuAdapter adapter = new DropMenuAdapter(entries);
        spinner.setAdapter(adapter);
    }

    // adapter to 2 way bind spinner selected value to viewModel property
    @BindingAdapter(value = {"selectedValue", "selectedValueAttrChanged"}, requireAll = false)
    public static void bindSpinnerData(CustomSpinner spinner, Object newSelectedValue,
                                       final InverseBindingListener newTextAttrChanged) {
        spinner.setOnItemSelectedListener((position, item) -> newTextAttrChanged.onChange());
        if (newSelectedValue != null) {
            int pos = spinner.getAdapter().getPosition(newSelectedValue);
            spinner.setSelection(pos);
        }
    }

    @InverseBindingAdapter(attribute = "selectedValue", event = "selectedValueAttrChanged")
    public static Object captureSelectedValue(CustomSpinner spinner) {
        return spinner.getSelectedItem();
    }

    // adapter to bind InputViewModel to Custom edittext.
    @BindingAdapter(value = {"selectedInput", "inputAttrChanged"}, requireAll = false)
    public static void bindInputViewModel(CustomSpinner spinner, InputViewModel selectedInput,
                                          final InverseBindingListener newTextAttrChanged) {
        spinner.setOnItemSelectedListener((position, item) -> newTextAttrChanged.onChange());
        if (selectedInput != null) {
            selectedInput.setListener(new InputViewModelValueChangedListener() {
                @Override
                public void onValueChanged(Object value) {
                    int pos = spinner.getAdapter().getPosition(value);
                    spinner.setSelection(pos);
                }

                @Override
                public void onErrorMessageChanged(String errorMessage) {
                    spinner.setError(errorMessage);
                }
            });
            Object selectedItem = spinner.getSelectedItem();
            if(selectedItem != null){
                if(selectedItem.equals(selectedInput.getValue())){
                    return;
                }
            }
            int pos = spinner.getAdapter().getPosition(selectedInput.getValue());
            spinner.setSelection(pos);
        }
    }

    @InverseBindingAdapter(attribute = "app:selectedInput", event = "inputAttrChanged")
    public static InputViewModel bindInputViewModelInverseAdapter(CustomSpinner spinner) {
        return new InputViewModel<>(spinner.getSelectedItem());
    }

}
