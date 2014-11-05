package com.pasaribu.store.control;

import java.util.ArrayList;
import java.util.List;

import com.pasaribu.store.model_data.Barang;

import android.app.Application;
import android.content.Context;

public class AppsController extends Application {
	
	private Context mainContext;
	
	private List<Barang> barang_data_full = new ArrayList<Barang>();
	
	public Barang getBarangAtPosition(int position) {
		return barang_data_full.get(position);
	}
	
	public List<Barang> getAllBarangList() {
		return barang_data_full;
	}
	
	public void setBarang(Barang barang) {
		barang_data_full.add(barang);
	}
	
	public int getBarangArrayListSize() {
		return barang_data_full.size();
	}
	
	public Context getMainContext() {
		return this.mainContext;
	}

}
