package com.example.somcoco.tour;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.somcoco.R;

import java.util.ArrayList;

public class FacilityCarouselAdapter extends RecyclerView.Adapter<FacilityCarouselAdapter.ViewHolder> {
    Context context;
    ArrayList<FacilityCarouselItem> items = new ArrayList<FacilityCarouselItem>();
    OnItemClickListener listener;

    public static interface OnItemClickListener {
        public void onItemClick(FacilityCarouselAdapter.ViewHolder holder, View view, int position);
    }

    public FacilityCarouselAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.facility_carousel_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FacilityCarouselAdapter.ViewHolder holder, int position) {
        FacilityCarouselItem item = items.get(position);

        Glide.with(holder.itemView)
                .load(item.getNoticeImage())
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.noimage)
                .error(R.drawable.noimage)
                .fallback(R.drawable.noimage)
                //.centerCrop()
                .into(holder.facilityCarouselImage);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(FacilityCarouselItem item) {
        items.add(item);
    }

    public FacilityCarouselItem getItem(int position) {
        return items.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView facilityCarouselImage;

        String facilityCarouselUrl;

        OnItemClickListener listener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            facilityCarouselImage = (ImageView) itemView.findViewById(R.id.facil_carousel_item);

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
