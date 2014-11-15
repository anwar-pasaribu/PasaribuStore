package com.pasaribu.store.control;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.pasaribu.store.model_data.Barang;
import com.pasaribu.store.model_data.Brand;
import com.pasaribu.store.model_data.Supplier;

public class AppsController extends Application {
	
	//From androidhive.com
	public static final String TAG = AppsController.class.getSimpleName();
	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;	
	private static AppsController mInstance;	
	
	//Me create this, List yang akan menampung seluruh data
	private Context mainContext;	
	private List<Barang> barang_data_full = new ArrayList<Barang>();
	private List<Supplier> list_supplier = new ArrayList<Supplier>();
	private List<Brand> list_brand = new ArrayList<Brand>();
	
	
	/* From androidhive.com -start- */
	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
	}
	
	public static synchronized AppsController getInstance() {
		return mInstance;
	}
	

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}

	public ImageLoader getImageLoader() {
		getRequestQueue();
		if (mImageLoader == null) {
			mImageLoader = new ImageLoader(this.mRequestQueue,
					new LruBitmapCache());
		}
		return this.mImageLoader;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}	
	/* from androidhive.com -end- */
	
	
	// me -start- //
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

	public List<Supplier> getList_supplier() {
		return list_supplier;
	}

	public void setList_supplier(Supplier list_supplier_full) {
		this.list_supplier.add(list_supplier_full);
	}

	public List<Brand> getList_brand() {
		return list_brand;
	}

	public void setList_brand(Brand list_brand_full) {
		this.list_brand.add(list_brand_full);
	}
	
	
	// me -end- //

}
