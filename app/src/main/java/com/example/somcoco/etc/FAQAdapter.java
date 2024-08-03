package com.example.somcoco.etc;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.somcoco.R;

import java.util.ArrayList;

public class FAQAdapter extends RecyclerView.Adapter<FAQAdapter.ItemViewHolder> {

    private ArrayList<NoticeItem> listData = new ArrayList<NoticeItem>();
    private Context context;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    private int prePosition = -1;

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {



        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        holder.onBind(listData.get(position), position);
    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return listData.size();
    }

    public void addItem(NoticeItem noticeItem) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(noticeItem);
    }

    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title;
        private TextView date;
        private ImageView imageView1;
        private TextView content;
        private LinearLayout linearItem;
        private NoticeItem noticeItem;
        private int position;

        ItemViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
            imageView1 = itemView.findViewById(R.id.imageView1);
            content = itemView.findViewById(R.id.content);
            linearItem = itemView.findViewById(R.id.linearItem);
        }

        void onBind(NoticeItem noticeItem, int position) {
            this.noticeItem = noticeItem;
            this.position = position;

            title.setText(noticeItem.getTitle());
            date.setText(noticeItem.getDate());
            content.setText(noticeItem.getContent());

            changeVisibility(selectedItems.get(position));

            linearItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.linearItem:
                    if (selectedItems.get(position)) {
                        // 펼쳐진 Item을 클릭 시
                        selectedItems.delete(position);
                    } else {
                        // 직전의 클릭됐던 Item의 클릭상태를 지움
                        selectedItems.delete(prePosition);
                        // 클릭한 Item의 position을 저장
                        selectedItems.put(position, true);
                    }
                    // 해당 포지션의 변화를 알림
                    if (prePosition != -1) notifyItemChanged(prePosition);
                    notifyItemChanged(position);
                    // 클릭된 position 저장
                    prePosition = position;
                    break;

            }
        }

        private void changeVisibility(final boolean isExpanded) {
            // height 값을 dp로 지정해서 넣고싶으면 아래 소스를 이용
            int dpValue = 150;
            float d = context.getResources().getDisplayMetrics().density;
            int height = (int) (dpValue * d);

            // ValueAnimator.ofInt(int... values)는 View가 변할 값을 지정, 인자는 int 배열
            ValueAnimator va = isExpanded ? ValueAnimator.ofInt(0, height) : ValueAnimator.ofInt(height, 0);
            // Animation이 실행되는 시간, n/1000초
            va.setDuration(600);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    // value는 height 값
                    int value = (int) animation.getAnimatedValue();
                    // imageView의 높이 변경
                    content.getLayoutParams().height = value;
                    content.requestLayout();
                    // imageView가 실제로 사라지게하는 부분
                    content.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
                    imageView1.setImageResource(isExpanded ? R.drawable.ic_up : R.drawable.ic_down);
                }
            });
            // Animation start
            va.start();
        }
    }

}
