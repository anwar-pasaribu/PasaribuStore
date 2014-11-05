package com.pasaribu.store;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Favorite extends Fragment {
	@Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	 
	        View android = inflater.inflate(R.layout.activity_favorite, container, false);
	        ((TextView)android.findViewById(R.id.title_left_pane)).setText("Favorite");
	        return android;
}}
