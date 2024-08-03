package com.example.somcoco.etc;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.somcoco.R;

import java.util.ArrayList;

public class CheckListAdapter extends RecyclerView.Adapter<CheckListAdapter.ViewHolder> {

    Context context;
    ArrayList<CheckListItem> items = new ArrayList<CheckListItem>();
    OnItemClickListener listener;

    public static interface OnItemClickListener {
        public void onItemClick(ViewHolder holder, View view, int position);
    }

    public CheckListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.checklist_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CheckListItem item = items.get(position);

        holder.id = item.getId();
        holder.checkItem.setText(item.getContent());
        holder.checkItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    buttonView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    buttonView.setPaintFlags(0);
                }
            }
        });

        holder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.date = item.getDate();

        holder.setOnItemClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(CheckListItem item) {
        items.add(item);
    }

    public CheckListItem getItem(int position) {
        return items.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        int id;
        String date;
        CheckBox checkItem;
        ImageView editItem;
        ImageView deleteItem;

        OnItemClickListener listener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            checkItem = (CheckBox) itemView.findViewById(R.id.check_item);
            editItem = (ImageView) itemView.findViewById(R.id.check_edit);
            deleteItem = (ImageView) itemView.findViewById(R.id.check_delete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if (listener != null) {
                        listener.onItemClick(ViewHolder.this, view, position);
                    }
                }
            });
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }
    }
}
