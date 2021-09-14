package com.shubhamgupta16.recyclersuggestion;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class _SuggestionAdapter extends RecyclerView.Adapter<_SuggestionAdapter.ViewHolder> {

    private final Context activity;
    private final List<RecyclerSuggestionView.SuggestionModel> list;
    private RecyclerSuggestionView.OnItemClickListener onItemClickListener;
    private RecyclerSuggestionView.OnCopyClickListener onCopyClickListener;
    private RecyclerSuggestionView.OnItemLongPressListener onItemLongPressListener;
    private Typeface typeface;
    private int historyIcon = R.drawable.ic_kit_history_24, suggestionIcon = R.drawable.ic_kit_search_24,
            copyIcon = R.drawable.ic_kit_copy_24, background = 9999, iconTint = 9999;

    public _SuggestionAdapter(Context context, List<RecyclerSuggestionView.SuggestionModel> list) {
        this.activity = context;
        this.list = list;
    }

    public void setOnItemClickListener(RecyclerSuggestionView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongPressListener(RecyclerSuggestionView.OnItemLongPressListener onItemLongPressListener) {
        this.onItemLongPressListener = onItemLongPressListener;
    }

    public void setOnCopyClickListener(RecyclerSuggestionView.OnCopyClickListener onCopyClickListener) {
        this.onCopyClickListener = onCopyClickListener;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
        notifyDataSetChanged();
    }

    public void setBackground(int background) {
        this.background = background;
        notifyDataSetChanged();
    }

    public void setCopyIcon(int copyIcon) {
        this.copyIcon = copyIcon;
        notifyDataSetChanged();
    }

    public void setHistoryIcon(int historyIcon) {
        this.historyIcon = historyIcon;
        notifyDataSetChanged();
    }

    public void setSuggestionIcon(int suggestionIcon) {
        this.suggestionIcon = suggestionIcon;
        notifyDataSetChanged();
    }

    public void setIconTint(int colorRes) {
        iconTint = colorRes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.kit_suggestion_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
//    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        RecyclerSuggestionView.SuggestionModel model = list.get(position);
        if (model.isHistory())
            holder.icon1.setImageResource(historyIcon);
        else
            holder.icon1.setImageResource(suggestionIcon);
        holder.button1.setImageResource(copyIcon);
        if (background != 9999)
            holder.itemView.setBackgroundResource(background);

        if (iconTint != 9999) {
            holder.icon1.setColorFilter(iconTint);
            holder.button1.setColorFilter(iconTint);
        }
        holder.text1.setText(model.getText());
        if (typeface != null)
            holder.text1.setTypeface(typeface);
        Log.d("tagtag", model.getText() + " = " + historyIcon);

//        views toggle
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(model.getText(), model.getListPosition(), position, model.isHistory());

                }
            }
        });
        holder.button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCopyClickListener != null)
                    onCopyClickListener.onCopyClick(model.getText(), model.getListPosition(), position, model.isHistory());
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemLongPressListener != null)
                    return onItemLongPressListener.onLongPress(model.getText(), model.getListPosition(), position, model.isHistory());
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView icon1;
        private final ImageButton button1;
        private final TextView text1;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon1 = itemView.findViewById(R.id.image1);
            button1 = itemView.findViewById(R.id.button1);
            text1 = itemView.findViewById(R.id.text1);
        }
    }
}
