package com.emma.general_backend_library.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emma.general_backend_library.R;
import com.emma.general_backend_library.interfaces.OnItemSelectedListener;

import java.util.ArrayList;
import java.util.List;

public class DropMenuAdapter extends RecyclerView.Adapter<DropMenuAdapter.MenuItemViewHolder>{
    private final Object[] itemList;
    private OnItemSelectedListener onItemSelectedListener;

    public DropMenuAdapter(Object[] itemList){
        super();
        this.itemList = itemList;
    }


    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

//    public void addItem(Object item){
//        itemList.add(item);
//    }

    public Object getItem(int position){
      return  itemList[position];
    }

    public int getPosition(Object value){
        for (int i = 0; i < itemList.length; i++) {
            if(value.equals(itemList[i])){
                return i;
            }
        }
        throw new IllegalArgumentException("Object does not exist!");
    }

    @NonNull
    @Override
    public MenuItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MenuItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_menu_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MenuItemViewHolder holder, int position) {
        final Object value = itemList[position];
        holder.titleTextView.setText(value.toString());
        holder.itemView.setOnClickListener(view -> {
            if(onItemSelectedListener != null){
                onItemSelectedListener.onItemSelected(position, value);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.length;
    }

    static class MenuItemViewHolder extends RecyclerView.ViewHolder{
        TextView titleTextView;
        public MenuItemViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title_textView);
        }
    }
}
