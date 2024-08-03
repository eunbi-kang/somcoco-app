package com.example.somcoco.searchcampus;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.somcoco.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CampusListAdapter extends RecyclerView.Adapter<CampusListAdapter.ViewHolder>  {

    Context context;
    ArrayList<CampusListItem> items = new ArrayList<CampusListItem>();
    OnItemClickListener listener;

    public static interface OnItemClickListener {
        public void onItemClick(ViewHolder holder, View view, int position);
    }

    public CampusListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.campus_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CampusListItem item = items.get(position);

        holder.campus_name.setText(item.cps_name);

        Glide.with(holder.itemView)
                .load(item.getCps_logo())
                .placeholder(R.drawable.noimage)
                .error(R.drawable.noimage)
                .fallback(R.drawable.noimage)
                .into(holder.campus_logo);

        holder.setOnItemClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(CampusListItem item) {
        items.add(item);
    }

    public void addItems(ArrayList<CampusListItem> items) {
        this.items = items;
    }

    public CampusListItem getItem(int position) {
        return items.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView campus_logo;
        TextView campus_name;

        OnItemClickListener listener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            campus_logo = (ImageView) itemView.findViewById(R.id.cps_logo);
            campus_name = (TextView) itemView.findViewById(R.id.cps_name);

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
