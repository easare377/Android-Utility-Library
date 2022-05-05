package com.emma.general_backend_library;

import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.databinding.ViewDataBinding;

import com.emma.general_backend_library.customViews.CustomTextInputEditText;
import com.emma.general_backend_library.models.viewModels.inputViewModels.InputDateViewModel;
import com.emma.general_backend_library.models.viewModels.inputViewModels.InputIntegerViewModel;
import com.emma.general_backend_library.models.viewModels.inputViewModels.InputStringViewModel;
import com.emma.general_backend_library.models.viewModels.inputViewModels.InputViewModel;
import com.emma.general_backend_library.models.viewModels.inputViewModels.InputViewModelValueChangedListener;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class BindingAdapterUtils {
    // adapter to bind bitmap to imageView.
    @BindingAdapter("bind:imageBitmap")
    public static void loadImage(ImageView iv, Bitmap bitmap) {
        if (bitmap != null) {
            iv.setImageBitmap(bitmap);
        }
    }

    //adapter to bind array of objects to spinner.
    @BindingAdapter("bind:entries")
    public static void loadEntries(Spinner spinner, Object[] entries) {
        //spinner.
        List<Object> spinnerList = new ArrayList<>(Arrays.asList(entries));
        ArrayAdapter<Object> adapter = new ArrayAdapter<Object>(
                spinner.getContext(), android.R.layout.simple_spinner_item, spinnerList);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    // adapter to 2 way bind spinner selected value to viewModel property
    @BindingAdapter(value = {"selectedValue", "selectedValueAttrChanged"}, requireAll = false)
    public static void bindSpinnerData(Spinner spinner, Object newSelectedValue, final InverseBindingListener newTextAttrChanged) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                newTextAttrChanged.onChange();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        if (newSelectedValue != null) {
            int pos = ((ArrayAdapter<Object>) spinner.getAdapter()).getPosition(newSelectedValue);
            spinner.setSelection(pos, true);
        }
    }

    @InverseBindingAdapter(attribute = "selectedValue", event = "selectedValueAttrChanged")
    public static Object captureSelectedValue(Spinner spinner) {
        return spinner.getSelectedItem();
    }

    @BindingAdapter({"bind:items", "bind:item_layout", "bind:layout_variable_name"})
    public static void addViewsItems(final ViewGroup viewGroup, Object[] items, int layout, String variableName) {
        //View view = inflater.inflate(layout, null);
        addViewsItems(viewGroup, items, layout, variableName, 0);
    }

    //Binding adapter for adding medications to LinearLayout.
    @BindingAdapter({"bind:items", "bind:item_layout", "bind:layout_variable_name", "bind:dividerHeight"})
    public static void addViewsItems(final ViewGroup viewGroup, Object[] items, int layout, String variableName, int dividerHeight) {
        //View view = inflater.inflate(layout, null);
        int variableId = -1;
        ClassLoader classLoader = BindingAdapterUtils.class.getClassLoader();
        try {
            Class aClass = classLoader.loadClass("androidx.databinding.library.baseAdapters.BR");
            variableId = (int) aClass.getField(variableName).get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        viewGroup.removeAllViews();
        for (int i = 0; i < items.length; i++) {
            View itemView = getItemView(viewGroup, items[i], i, layout, variableId, dividerHeight);
            viewGroup.addView(itemView);
        }
    }

    //Binding adapter for adding medications to LinearLayout.
    @BindingAdapter({"bind:items", "bind:item_layout", "bind:layout_variable_name", "bind:dividerHeight"})
    public static void addViewsItems(final ViewGroup viewGroup, ObservableList itemsList, int layout, String variableName, int dividerHeight) {
        Object[] items = new Object[itemsList.size()];
        items = itemsList.toArray(items);
        addViewsItems(viewGroup, items, layout, variableName, dividerHeight);
    }

    //Binding adapter for adding medications to LinearLayout.
    @BindingAdapter({"bind:items", "bind:item_layout", "bind:layout_variable_name"})
    public static void addViewsItems(final ViewGroup viewGroup, ObservableList items, int layout, String variableName) {
        //View view = inflater.inflate(layout, null);
        addViewsItems(viewGroup, items, layout, variableName, 0);
    }

    private static View getItemView(ViewGroup viewGroup, Object item, int position, int layout, int variableId, int dividerHeight) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        ViewDataBinding binding = DataBindingUtil
                .inflate(inflater, layout, viewGroup, false);
        binding.setVariable(variableId, item);
        View view = binding.getRoot();
        //Set layout properties of this view
        LinearLayout.LayoutParams sv_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        int rl_margin = 0; //getResources().getDimensionPixelSize(0);
        if (position == 0) {
            sv_params.setMargins(rl_margin, Functions.dpTopx(0, view), rl_margin, Functions.dpTopx(0, view));
        } else {
            sv_params.setMargins(rl_margin, Functions.dpTopx((int) dividerHeight, view), rl_margin, Functions.dpTopx(0, view));
        }
        view.setLayoutParams(sv_params);

        return view;
    }

    //Binding adapter for adding click listener.
    @BindingAdapter({"app:onclick"})
    public static void onClickView(View v, View.OnClickListener listener) {
        v.setOnClickListener(listener);
    }

//    @BindingAdapter({"bind:items", "app:layout", "app:dividerHeight"})
//    public static void addViews(ViewGroup viewGroup, ObservableArrayList<Object> items,
//                                int layout, double dividerHeight) {
//        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
//        //View view = inflater.inflate(layout, null);
//        for (Object item : items) {
//            ViewDataBinding binding = DataBindingUtil
//                    .inflate(inflater, layout, viewGroup, false);
//            binding.setVariable(1, item);
//            View view = binding.getRoot();
//            //Set layout properties of this view
//            LinearLayout.LayoutParams sv_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT);
//            int rl_margin = 0; //getResources().getDimensionPixelSize(0);
//            if (item.equals(items.get(0))) {
//                sv_params.setMargins(rl_margin, Functions.dpTopx(0, view), rl_margin, Functions.dpTopx(0, view));
//            } else {
//                sv_params.setMargins(rl_margin, Functions.dpTopx((int) dividerHeight, view), rl_margin, Functions.dpTopx(0, view));
//            }
//            view.setLayoutParams(sv_params);
//            viewGroup.addView(view);
//        }
//
//    }

    // adapter to make edittext not editable.
    @BindingAdapter("bind:editable")
    public static void editable(EditText editText, Boolean editable) {
        editText.setFocusable(editable);
        editText.setLongClickable(editable);
        editText.setCursorVisible(false);
    }

    // adapter to bind InputStringViewModel to Custom edittext.
    @BindingAdapter(value = {"inputText", "inputAttrChanged"}, requireAll = false)
    public static void bindInputTextViewModel(CustomTextInputEditText tv, InputStringViewModel value, final InverseBindingListener inverseBindingListener) {
        if (value != null) {
            Editable oldValue = tv.getText();
            String oldText = "";
            if (oldValue != null) {
                oldText = oldValue.toString();
            }
            InputViewModelValueChangedListener<String> listener = new InputViewModelValueChangedListener<String>() {
                @Override
                public void onValueChanged(String value) {
                    tv.setText(value);
                }

                @Override
                public void onErrorMessageChanged(String errorMessage) {
                    tv.setError(errorMessage);
                }
            };
            value.setListener(listener);
            if (oldText.equals(value.getValue())) {
                return;
            }
            tv.setText(value.getValue());
            tv.setError(value.getErrorMessage());
            // Set the cursor to the end of the text.
            if (tv.getText() != null) {
                tv.setSelection(tv.getText().length());
            }
        }
        tv.removeAllTextChangedListeners();
        tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //inverseBindingListener.onChange();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inverseBindingListener.onChange();
            }

            @Override
            public void afterTextChanged(Editable s) {
                //inverseBindingListener.onChange();
            }
        });
    }

    @InverseBindingAdapter(attribute = "app:inputText", event = "app:inputAttrChanged")
    public static InputStringViewModel bindInputViewModelInverseAdapter(CustomTextInputEditText view) {
        Editable editable = view.getText();
        //String string = view.getText().toString();
        if (editable == null) {
            return null;
        } else {
            return new InputStringViewModel(editable.toString());
        }
    }

    // adapter to bind InputDateViewModel to Custom edittext.
    @BindingAdapter(value = {"app:inputDate", "app:format"}, requireAll = false)
    public static void bindInputDateViewModel(CustomTextInputEditText tv, InputDateViewModel inputDate, String format) {
        if (inputDate != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            Editable oldValue = tv.getText();
            String oldText = "";
            if (oldValue != null) {
                oldText = oldValue.toString();
            }
            InputViewModelValueChangedListener<Date> listener = new InputViewModelValueChangedListener<Date>() {
                @Override
                public void onValueChanged(Date value) {

                    tv.setText(dateFormat.format(value));
                }

                @Override
                public void onErrorMessageChanged(String errorMessage) {
                    tv.setError(errorMessage);
                }
            };
            inputDate.setListener(listener);
            if (oldText.equals(dateFormat.format(inputDate.getValue()))) {
                return;
            }
            tv.setText(dateFormat.format(inputDate.getValue()));
            tv.setError(inputDate.getErrorMessage());
            // Set the cursor to the end of the text.
            if (tv.getText() != null) {
                tv.setSelection(tv.getText().length());
            }
        }
    }

//    @InverseBindingAdapter(attribute = "app:inputText", event = "app:inputAttrChanged")
//    public static InputDateViewModel bindInputDateViewModelInverseAdapter(CustomTextInputEditText view) {
//        Editable editable = view.getText();
//        //String string = view.getText().toString();
//        if (editable == null) {
//            return null;
//        } else {
//            SimpleDateFormat format = new SimpleDateFormat("")
//            return new InputDateViewModel(editable.toString());
//        }
//    }

    // adapter to bind InputStringViewModel to Custom edittext.
    @BindingAdapter(value = {"inputInt", "inputAttrChanged"}, requireAll = false)
    public static void bindInputIntViewModel(CustomTextInputEditText tv, InputIntegerViewModel value, final InverseBindingListener inverseBindingListener) {
        if (value != null) {
            Editable oldValue = tv.getText();
            String oldText = "";
            if (oldValue != null) {
                oldText = oldValue.toString();
            }
            InputViewModelValueChangedListener<Integer> listener = new InputViewModelValueChangedListener<Integer>() {
                @Override
                public void onValueChanged(Integer value) {
                    if (value != null) {
                        tv.setText(value.toString());
                    }
                }

                @Override
                public void onErrorMessageChanged(String errorMessage) {
                    tv.setError(errorMessage);
                }
            };
            value.setListener(listener);
            if (value.getValue() != null && oldText.equals(value.getValue().toString())) {
                return;
            }
            if (value.getValue() != null) {
                tv.setText(value.getValue().toString());
            }
            tv.setError(value.getErrorMessage());
            // Set the cursor to the end of the text.
            if (tv.getText() != null) {
                tv.setSelection(tv.getText().length());
            }
        }
        tv.removeAllTextChangedListeners();
        tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //inverseBindingListener.onChange();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inverseBindingListener.onChange();
            }

            @Override
            public void afterTextChanged(Editable s) {
                //inverseBindingListener.onChange();
            }
        });
    }

    @InverseBindingAdapter(attribute = "app:inputInt", event = "app:inputAttrChanged")
    public static InputIntegerViewModel bindInputIntViewModelInverseAdapter(CustomTextInputEditText view) {
        Editable editable = view.getText();
        //String string = view.getText().toString();
        if (editable == null) {
            return null;
        } else {
            return new InputIntegerViewModel(Integer.parseInt(editable.toString()));
        }
    }

    @BindingAdapter("android:layout_marginTop")
    public static void setLayoutMarginTop(View view, Float dimen) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.topMargin = dimen.intValue();
    }

    @BindingAdapter("android:layout_marginLeft")
    public static void setLayoutMarginLeft(View view, Float dimen) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.leftMargin = dimen.intValue();
    }

    @BindingAdapter("android:layout_marginRight")
    public static void setLayoutMarginRight(View view, Float dimen) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.rightMargin = dimen.intValue();
    }

    @BindingAdapter("android:layout_marginBottom")
    public static void setLayoutMarginBottom(View view, Float dimen) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.bottomMargin = dimen.intValue();
    }

//    @BindingAdapter("bind:text")
//    public static void setText(TextInputEditText editText, InputViewModel<String> text) {
//        editText.setText(text.getValue());
//    }
//
//    @InverseBindingAdapter(attribute = "bind:text")
//    public static InputViewModel<String> getText(TextInputEditText editText) {
//        Editable text = editText.getText();
//        if(text != null){
//            InputViewModel<String> input = new InputViewModel<>();
//            input.setValue(text.toString());
//            return input;
//        }
//        return null;
//    }

    //adapter to bind array of objects to spinner.
//    @BindingAdapter("app:entries")
//    public static void loadMaterialSpinnerEntries(MaterialSpinner spinner, Object[] entries) {
//        List<Object> spinnerList = new ArrayList<>(Arrays.asList(entries));
//        ArrayAdapter<Object> adapter = new ArrayAdapter<Object>(
//                spinner.getContext(), android.R.layout.simple_spinner_item, spinnerList);
//
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
//    }
//
//    // adapter to 2 way bind spinner selected value to viewModel property
//    @BindingAdapter(value = {"app:selectedValue", "selectedValueAttrChanged"}, requireAll = false)
//    public static void bindMaterialSpinnerData(MaterialSpinner spinner, Object newSelectedValue, final InverseBindingListener newTextAttrChanged) {
//        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
//            @Override
//            public void onNothingSelected(MaterialSpinner materialSpinner) {
//
//            }
//
//            @Override
//            public void onItemSelected(MaterialSpinner materialSpinner, View view, int i, long l) {
//                newTextAttrChanged.onChange();
//            }
//        });
//        if (newSelectedValue != null) {
//            int pos = ((ArrayAdapter<Object>) spinner.getAdapter()).getPosition(newSelectedValue);
//            spinner.setSelection(pos);
//        }
//    }
}
