package com.fillipe.googlesearch.utils;

import java.util.HashMap;

public class SearchConfig {
	public String query;
	public int page = 0;
	public HashMap<String, String> params = null;

	public SearchConfig(String query) {
		this.query = query;
	}

	public SearchConfig(String query, int page) {
		this.query = query;
		this.page = page;
	}

	public SearchConfig(String query, int page, HashMap<String, String> params) {
		this.query = query;
		this.page = page;
		this.params = params;
	}
}
