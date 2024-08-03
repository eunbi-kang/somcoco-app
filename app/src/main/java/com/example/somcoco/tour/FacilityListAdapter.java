package com.example.somcoco.tour;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.somcoco.R;

import java.util.ArrayList;

public class FacilityListAdapter extends RecyclerView.Adapter<FacilityListAdapter.ViewHolder> {

    Context context;
    ArrayList<FacilityListItem> items = new ArrayList<FacilityListItem>();
    OnItemClickListener listener;

    public static interface OnItemClickListener {
        public void onItemClick(ViewHolder holder, View view, int position);
    }

    public FacilityListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.facility_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FacilityListItem item = items.get(position);

        holder.facilityName.setText(item.getFacilityName());

        holder.setOnItemClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(FacilityListItem item) {
        items.add(item);
    }

    public void addItems(ArrayList<FacilityListItem> items) {
        this.items = items;
    }

    public FacilityListItem getItem(int position) {
        return items.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView facilityName;

        OnItemClickListener listener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            facilityName = (TextView) itemView.findViewById(R.id.facil_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if (listener != null){
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
