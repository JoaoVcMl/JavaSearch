package com.fillipe.googlesearch;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fillipe.googlesearch.adapters.SearchAdapter;
import com.fillipe.googlesearch.utils.SearchConfig;
import com.fillipe.googlesearch.utils.SearchListener;
import com.fillipe.googlesearch.utils.SearchProvider;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

	private SearchView searchView;
	private RecyclerView recyclerView;
	private SearchAdapter searchAdapter;
	private LinearLayout errorView;

	private void statements() {
		searchView = findViewById(R.id.searchView);
		recyclerView = findViewById(R.id.recyclerView);
		errorView = findViewById(R.id.errorView);
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		statements();

		searchView.setIconified(false);
		searchView.setOnQueryTextListener(this);

		errorView.setVisibility(View.GONE);
    }

	@Override
	public boolean onQueryTextSubmit(String s) {
		searchView.setQuery("", false);

		SearchConfig config = new SearchConfig(s);
		SearchProvider.search(config, new SearchListener() {
			@Override
			public void onSuccess(ArrayList<HashMap<String, String>> responses) {
				searchAdapter = new SearchAdapter(responses);
				LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
				recyclerView.setAdapter(searchAdapter);
				recyclerView.setLayoutManager(layoutManager);

				errorView.setVisibility(View.GONE);
			}

			@Override
			public void onError() {
				errorView.setVisibility(View.VISIBLE);
			}
		});
		return false;
	}

	@Override
	public boolean onQueryTextChange(String s) {
		return false;
	}
}