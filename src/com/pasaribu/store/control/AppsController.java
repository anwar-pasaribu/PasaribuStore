package com.pasaribu.store.control;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.pasaribu.store.model_data.Barang;
import com.pasaribu.store.model_data.Brand;
import com.pasaribu.store.model_data.Supplier;

/**
 * @author Anwar Pasaribu
 *
 */
public class AppsController extends Application {
	
	//From androidhive.com
	public static final String TAG = AppsController.class.getSimpleName();
	private RequestQueue queue;
	private ImageLoader mImageLoader;	
	private static AppsController mInstance;	
	
	//Me create this, List yang akan menampung seluruh data
	private Context mainContext;	
	private List<Barang> barang_data_full = new ArrayList<Barang>();
	private List<Supplier> list_supplier = new ArrayList<Supplier>();
	private List<Brand> list_brand = new ArrayList<Brand>();
	private List<String> list_product_category = new ArrayList<String>();
	
	
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
		
		if (queue == null) {
			queue = Volley.newRequestQueue(getApplicationContext());
		}

		return queue;
	}

	public ImageLoader getImageLoader() {
		getRequestQueue();
		if (mImageLoader == null) {
			mImageLoader = new ImageLoader(this.queue,
					new LruBitmapCache());
		}
		return this.mImageLoader;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {		
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	
	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (queue != null) {
			queue.cancelAll(tag);
		}
	}	
	/* from androidhive.com -end- */
	
	
	
	//Cek koneksi internet / jaringan
	//Source : http://stackoverflow.com/questions/4238921/detect-whether-there-is-an-internet-connection-available-on-android
	public boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	// me -start- //
	
	/////////////////////////////////////////////////////////////////
	/////////////////////////BARANG-START////////////////////////////
	
	/**
	 * Mengubah data Barang pada lokasi/index yang kirim.
	 * @param location : Lokasi data yang akan di ubah
	 * @param new_data_barang : Data Barang baru utk mengganti data lama.
	 */
	public void setBarangAtPosition(int location, Barang new_data_barang) {
		barang_data_full.set(location, new_data_barang);
	}
	
	public List<Barang> getAllBarangList() {
		return barang_data_full;
	}	
	
	public Barang getBarangAtPosition(int position) {
		return barang_data_full.get(position);
	}
	
	public Barang getBrangById(int id_barang) {
		
		Barang barang = null;
		
		for(int i = 0; i< getBarangArrayListSize(); i++) {
			if(getAllBarangList().get(i).getId_barang() == id_barang)
				barang = getAllBarangList().get(i);
		}
		return barang;
	}
	
	/**
	 * Berfungsi utk mengolah JSONObject Barang menjadi var Barang.
	 * @param jsonObject - String dengan format JSON dari Server
	 * @return Barang - Bentuk barang full version (table barang)
	 */
	public Barang createBarangFromJSONObject(JSONObject jsonObject) {
		
		Barang data_barang_temp = null;
		try {
			data_barang_temp = new Barang(
					jsonObject.getInt(Barang.ID_BARANG), 
					jsonObject.getInt(Barang.ID_MEREK), 
					jsonObject.getInt(Barang.ID_PENJUAL), 
					jsonObject.getInt(Barang.ID_GAMBAR), 
					jsonObject.getString(Barang.NAMA_BARANG), 
					jsonObject.getInt(Barang.STOK_BARANG), 
					jsonObject.getString(Barang.SATUAN_BARANG), 
					jsonObject.getInt(Barang.HARGA_BARANG), 
					jsonObject.getString(Barang.TGL_HARGA_STOK_BARANG), 
					jsonObject.getString(Barang.KODE_BARANG), 
					jsonObject.getString(Barang.LOKASI_BARANG), 
					jsonObject.getString(Barang.KATEGORI_BARANG), 
					jsonObject.getString(Barang.DESKRIPSI_BARANG), 
					jsonObject.getInt(Barang.FAVORITE) 
					);
		} catch (JSONException e) {
			e.printStackTrace();
		}	
		
		return data_barang_temp;
		
	}

	public void addToListBarangFull(Barang barang) {
		barang_data_full.add(barang);
	}
	
	public int getBarangArrayListSize() {
		return barang_data_full.size();
	}
	
	/**
	 * <strong>!!!</strong> Menghapus seluruh isi data List Barang.
	 */
	public void clearAllBarangList() {
		barang_data_full.clear();
	}
	
	/////////////////////////////////////////////////////////////////
	/////////////////////////BARANG-END//////////////////////////////
	
	
	////////////////////////////////////////////////////////////////
	///////////////////////SUPPLIER/////////////////////////////////
	
	public List<Supplier> getList_supplier() {
		return list_supplier;
	}

	public void setList_supplier(Supplier list_supplier_full) {
		this.list_supplier.add(list_supplier_full);
	}	
	
	/**
	 * Memperoleh Nama Toko dari id yang diberikan.
	 * @param id_supplier : ID Toko yang dimaksud.
	 * @return supplier_name : Nama Toko
	 */
	public String getSupplierNameById(int id_supplier) {
		
		String supplier_name = "";
		
		for(int i = 0; i < getList_supplier().size(); i++) {
			if(getList_supplier().get(i).getId_penjual() == id_supplier) {
				supplier_name = getList_supplier().get(i).getNama_toko();
			}
		}
		
		return !TextUtils.isEmpty(supplier_name) ? supplier_name : "Toko Tidak Dketahui";
		
	}
	
	public int getSupplierListIndexByName(String supplier_name) {
		
		int index_supplier_list = 0;
		
		for(int i = 0; i < getList_supplier().size(); i++) {
			if(getList_supplier().get(i).getNama_toko().equals(supplier_name)) {
				index_supplier_list = i;
			}
		}
		
		return index_supplier_list;
	}
	
	/**
	 * !!! Clear All Supplier List
	 */
	public void clearAllSupplierList() {
		list_supplier.clear();
	}
	///////////////////////SUPPLIER - END///////////////////////////
	

	
	////////////////////////////////////////////////////////////////
	////////////////////CATEGORY////////////////////////////////////
	public List<String> getList_product_category() {
		return list_product_category;
	}

	public void setList_product_category(String product_category) {
		this.list_product_category.add(product_category);
	}
	
	public int getProductCategoryListIndexByName(String category_name) {
		
		int index_category_list = 0;
		
		for(int i = 0; i < getList_product_category().size(); i++) {
			if(getList_product_category().get(i).equals(category_name)) {
				index_category_list = i;
			}
		}
		
		return index_category_list;
		
	}	
	////////////////////CATEGORY - END//////////////////////////////
	
	

	////////////////////////////////////////////////////////////////
	///////////////////////BRAND////////////////////////////////////
	public List<Brand> getList_brand() {
		return list_brand;
	}

	public void setList_brand(Brand list_brand_full) {
		this.list_brand.add(list_brand_full);
	}
	
	public Context getMainContext() {
		return this.mainContext;
	}
	
	/**
	 * Memperoleh Nama Merek dari id yang diberikan.
	 * @param id_brand : ID Brand yang dimaksud.
	 * @return brand_name : Nama Brand
	 */
	public String getBrandName(int id_brand) {
		
		String brand_name = "";
		
		for(int i = 0; i < getList_brand().size(); i++) {
			if( getList_brand().get(i).getId_merek() == id_brand ) {
				brand_name = getList_brand().get(i).getNama_merek();
			}
		}
		
		return !TextUtils.isEmpty(brand_name) ? brand_name : "No Brand";
		
	}	
	///////////////////////BRAND - END//////////////////////////////
}
