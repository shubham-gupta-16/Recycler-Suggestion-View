package com.shubhamgupta16.recyclersuggestion;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerSuggestionView extends RecyclerView {
    public RecyclerSuggestionView(@NonNull Context context) {
        super(context);
    }

    public RecyclerSuggestionView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private TypedArray a;
    public _SuggestionAdapter adapter;
    public List<SuggestionModel> models;
    private SQLiteDatabase database;
    private int _limit;
    private String newText;
    public List<String> strings;

    public RecyclerSuggestionView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        a = context.obtainStyledAttributes(attrs, R.styleable.RecyclerSuggestionView, defStyleAttr, 0);


        if (a.hasValue(R.styleable.RecyclerSuggestionView_android_fontFamily))
            setItemFontFamily(a.getResourceId(R.styleable.RecyclerSuggestionView_android_fontFamily, -1));
        if (a.hasValue(R.styleable.RecyclerSuggestionView_historyIcon))
            setHistoryIcon(a.getResourceId(R.styleable.RecyclerSuggestionView_historyIcon, -1));
        if (a.hasValue(R.styleable.RecyclerSuggestionView_suggestionIcon))
            setSuggestionIcon(a.getResourceId(R.styleable.RecyclerSuggestionView_suggestionIcon, -1));
        if (a.hasValue(R.styleable.RecyclerSuggestionView_copyIcon))
            setCopyIcon(a.getResourceId(R.styleable.RecyclerSuggestionView_copyIcon, -1));
        if (a.hasValue(R.styleable.RecyclerSuggestionView_itemBackground))
            setItemBackground(a.getResourceId(R.styleable.RecyclerSuggestionView_itemBackground, -1));
        if (a.hasValue(R.styleable.RecyclerSuggestionView_iconTint))
            setIconTint(a.getColor(R.styleable.RecyclerSuggestionView_iconTint, Color.BLACK));
        setDivider(a.getBoolean(R.styleable.RecyclerSuggestionView_itemDivider, false));
        a.recycle();
    }

    public void setItemFontFamily(int fontRes) {
        adapter.setTypeface(ResourcesCompat.getFont(getContext(), fontRes));
    }

    public void setItemBackground(@DrawableRes int res) {
        adapter.setBackground(res);
    }

    public void setHistoryIcon(@DrawableRes int res) {
        Log.d("tagtag", "inhere");
        adapter.setHistoryIcon(res);
    }

    public void setIconTint(@ColorInt int colorRes) {
        adapter.setIconTint(colorRes);
    }

    public void setSuggestionIcon(@DrawableRes int res) {
        adapter.setSuggestionIcon(res);
    }

    public void setCopyIcon(@DrawableRes int res) {
        adapter.setCopyIcon(res);
    }

    public void setDivider(boolean divider) {
        if (divider)
            addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        else if (getItemDecorationCount() > 0)
            removeItemDecorationAt(0);

    }

    public void setLimit(int limit) {
        if (models == null) return;
        this._limit = limit;
        ArrayList<SuggestionModel> suggestionModels = new ArrayList<>(models);
        _filter(suggestionModels);
    }

    public void setOnSuggestionClickListener(OnItemClickListener onItemClickListener) {
        adapter.setOnItemClickListener(onItemClickListener);
    }

    public void refresh(){
        if (newText!= null)
        filterSuggestion(newText, strings);
    }

    public void setOnSuggestionLongPressListener(OnItemLongPressListener onItemLongPressListener) {
        adapter.setOnItemLongPressListener(onItemLongPressListener);
    }

    public void filterSuggestion(@NonNull String newText) {
        filterSuggestion(newText, null);
    }

    public void filterSuggestion(@NonNull String newText, @Nullable List<String> strings) {
        this.newText = newText;
        this.strings = strings;
        ArrayList<SuggestionModel> suggestionModels = new ArrayList<>();
        int localLimit = _limit;
        Cursor c;
        if (newText.isEmpty())
            c = database.rawQuery("SELECT text FROM history ORDER BY id DESC", null);
        else
            c = database.rawQuery("SELECT text FROM history WHERE text LIKE '%" + newText + "%' ORDER BY id DESC", null);
        while (c.moveToNext()) {
            suggestionModels.add(new SuggestionModel(c.getString(0), -1, true));
            localLimit--;
        }
        c.close();
        if (strings != null)
            for (int i = 0; i < Math.min(localLimit, strings.size()); i++) {
                int index = suggestionModels.indexOf(new SuggestionModel(strings.get(i), -1, true));
                if (index < 0)
                    suggestionModels.add(new SuggestionModel(strings.get(i), i, false));
                else
                    suggestionModels.set(index, new SuggestionModel(strings.get(i), i, true));
            }
        _filter(suggestionModels);
    }

    public void removeHistory(int position) {
        if (models.size() <= position) return;
        String text = models.get(position).getText();
        removeHistory(text);
        models.remove(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, models.size());
    }

    public void removeHistory(String text) {
        database.execSQL("DELETE FROM history WHERE text = '" + text + "';");
    }

    public void addHistory(String text) {
        try {
            database.execSQL("INSERT INTO history (text) VALUES ('" + text + "');");
        } catch (Exception ignored) {
        }
    }

    private void initSqlDB(String key) {
        database = getContext().openOrCreateDatabase(key, Context.MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS history (id INTEGER PRIMARY KEY AUTOINCREMENT, text VARCHAR UNIQUE);");
    }

    private void _filter(ArrayList<SuggestionModel> suggestionModels) {
        models.clear();
        for (int i = 0; i < Math.min(_limit, suggestionModels.size()); i++) {
            models.add(suggestionModels.get(i));
        }
        adapter.notifyDataSetChanged();
    }

    public void initialize(String name) {
        initSqlDB(name);
        _limit = 20;
        models = new ArrayList<>();
        adapter = new _SuggestionAdapter(getContext(), models);
        setLayoutManager(new LinearLayoutManager(getContext()));
        setAdapter(adapter);
        filterSuggestion("", null);
    }

    public interface OnItemClickListener {
        void onClick(String suggestion, int listPosition, int position, boolean isHistory);
    }

    public interface OnCopyClickListener {
        void onCopyClick(String suggestion, int listPosition, int position, boolean isHistory);
    }

    public interface OnItemLongPressListener {
        boolean onLongPress(String suggestion, int listPosition, int position, boolean isHistory);
    }

    static class SuggestionModel {
        private final int listPosition;
        private final String text;
        private final boolean isHistory;

        public SuggestionModel(String text, int listPosition, boolean isHistory) {
            this.text = text;
            this.listPosition = listPosition;
            this.isHistory = isHistory;
        }

        public int getListPosition() {
            return listPosition;
        }

        public String getText() {
            return text;
        }

        public boolean isHistory() {
            return isHistory;
        }
    }
}
