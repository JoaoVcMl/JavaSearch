package com.fillipe.googlesearch.utils;

import java.util.ArrayList;
import java.util.HashMap;

public interface SearchListener {
    void onSuccess(ArrayList<HashMap<String, String>> responses);
    void onError();
}
