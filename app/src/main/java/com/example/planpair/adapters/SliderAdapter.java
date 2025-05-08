package com.example.planpair.adapters;

import android.app.Notification;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planpair.R;
import com.example.planpair.SliderItem;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {

    private List<SliderItem> sliderItems;

    public SliderAdapter(List<SliderItem> sliderItems) {
        this.sliderItems = sliderItems;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slide_item, parent, false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        SliderItem item = sliderItems.get(position);
        holder.imageView.setImageResource(item.getImageResId());

        String info = item.getInfo();
        SpannableString spannableString = new SpannableString(info);
        int lineBreakIndex = info.indexOf("\n");
        if (lineBreakIndex > 0) {
            spannableString.setSpan(
                    new StyleSpan(Typeface.BOLD),
                    0,
                    lineBreakIndex,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }
        holder.textView.setText(spannableString);

    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }

    static class SliderViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;


        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.slideImage);
            textView = itemView.findViewById(R.id.slideText);

        }
    }
}