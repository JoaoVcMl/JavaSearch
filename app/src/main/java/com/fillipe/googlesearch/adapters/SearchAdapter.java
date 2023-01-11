package com.fillipe.googlesearch.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fillipe.googlesearch.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchHolder> {
	private ArrayList<HashMap<String, String>> resultsList;

	public SearchAdapter(ArrayList<HashMap<String, String>> resultsList) {
		this.resultsList = resultsList;
	}

	@NonNull
	@Override
	public SearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		return new SearchHolder(LayoutInflater.from(parent.getContext())
				.inflate(R.layout.search_item, parent, false));
	}

	@Override
	public void onBindViewHolder(@NonNull SearchHolder holder, int position) {
		HashMap<String, String> result = resultsList.get(position);
		holder.url.setText(result.get("url"));
		holder.title.setText(result.get("title"));
		holder.content.setText(result.get("content"));
	}

	@Override
	public int getItemCount() {
		return resultsList.size();
	}

	public static class SearchHolder extends RecyclerView.ViewHolder {
		public final TextView url, title, content;

		public SearchHolder(@NonNull View itemView) {
			super(itemView);
			url = itemView.findViewById(R.id.textUrl);
			title = itemView.findViewById(R.id.textTitle);
			content = itemView.findViewById(R.id.textContent);
		}
	}
}
