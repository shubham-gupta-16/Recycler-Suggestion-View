package com.shubhamgupta16.recyclersuggestion_sample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.os.Bundle;
import android.widget.Toast;

import com.shubhamgupta16.recyclersuggestion.RecyclerSuggestionView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SearchView searchView = findViewById(R.id.search_view);
        RecyclerSuggestionView suggestionView = findViewById(R.id.suggestion_view);
        suggestionView.initialize("unique-name");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                suggestionView.addHistory(query);
                suggestionView.refresh();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                suggestionView.filterSuggestion(newText);
                return false;
            }
        });

        suggestionView.setOnSuggestionClickListener((suggestion, listPosition, position, isHistory) -> {
            Toast.makeText(MainActivity.this, listPosition + " -> " + suggestion, Toast.LENGTH_SHORT).show();
            suggestionView.refresh();
        });
    }
}