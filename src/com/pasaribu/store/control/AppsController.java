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
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.pasaribu.store.data_model.Barang;
import com.pasaribu.store.data_model.Brand;
import com.pasaribu.store.data_model.Supplier;
import com.pasaribu.store.volley.LruBitmapCache;

/**
 * @author Anwar Pasaribu
 *
 */
public class AppsController extends Application {
	
	public static final String TAG = AppsController.class.getSimpleName();
	private RequestQueue queue;
	private ImageLoader mImageLoader;	
	private static AppsController mInstance;	
	
	//Me create this, List yang akan menampung seluruh data
	private Context mainContext;	
	private List<Barang> barang_data_full = new ArrayList<Barang>();
	private ArrayList<Supplier> list_supplier = new ArrayList<Supplier>();
	private List<Brand> list_brand = new ArrayList<Brand>();
	private List<String> list_product_category = new ArrayList<String>();
	private List<Abjad> listHuruf = new ArrayList<Abjad>();
	
	//Var ini berfungsi untuk mendeteksi apakah parsing sudah pernah dilakukan
	//Asumsi pertama boleh dilakukan, utk Home.java
	public boolean isExecuted = true;
	
	//Mengidentifikasi perubahan data barang, utk notifyDataChanged pada Home.java
	public boolean isDataChanged = false;
	
	//Mengetahui apakah aplikasi pertama kali di buka, berguna utk menentukan pengambilan 50 data pertama
	public boolean isFirstLoad = true;
	
	//Lokasi Scroll ListView pada Home (25 Des / Christmas!)
	public int listHome_lastViewedPosition = 0;
	public int listHome_topOffset = 0;
	

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		
		Log.v(TAG, "Apps Conroller onCreate");
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
	
	/**
	 * @return Seluruh List Barang Lengkap
	 */
	public List<Barang> getAllBarangList() {
		return barang_data_full;
	}	
	
	public Barang getBarangAtPosition(int position) {
		return barang_data_full.get(position);
	}
	
	public Barang getBarangById(int id_barang) {
		
		Barang barang = null;
		
		for(int i = 0; i< getBarangArrayListSize(); i++) {
			if(getAllBarangList().get(i).getId_barang() == id_barang)
				barang = getAllBarangList().get(i);
		}
		return barang;
	}
	
	/**
	 * Mendapatkan posisi barang/ index barang dalam List &lt;Barang&gt;
	 * @param nama_barang : nama yang akan di cari posisi indexnya.
	 * @return posisi barang dalam list (index).
	 */
	public int getBarangListIndexByName(String nama_barang) {
		
		int index_category_list = 0;
		
		for(int i = 0; i < getBarangArrayListSize(); i++) {
			if(getAllBarangList().get(i).getNama_barang().equals(nama_barang) || getAllBarangList().get(i).getNama_barang().matches(nama_barang) ) {
				index_category_list = i;
				break;
				
			}
		}
		
		return index_category_list;
		
	}
	
	/**
	 * Berfungsi utk mengolah <b>JSONObject</b> Barang menjadi var Barang.
	 * @param jsonObject - String dengan format JSON dari Server
	 * @return Barang - Bentuk barang full version (table barang) 
	 * @author Anwar Pasaribu
	 * 
	 */
	public Barang createBarangFromJSONObject(JSONObject jsonObject) {		
//		String huruf_pertama = "";		
		Barang data_barang_temp = null;
		
		try {
//			
//			huruf_pertama = jsonObject.getString(Barang.NAMA_BARANG).substring(0, 1);
//			
//			if(listHuruf.isEmpty()) {				
//				listHuruf.add(new Abjad(huruf_pertama, 1));				
//				addToListBarang(new Barang(huruf_pertama, 1));
//				
//			} else {
//				
//				for(int i = 0; i < listHuruf.size();) {
//					
//					if(!isCharPresentOnListHuruf(huruf_pertama) ) {
//						
//						listHuruf.add(new Abjad(huruf_pertama, 1));
//						addToListBarang(new Barang(huruf_pertama, 1));
//						break;
//						
//					} else {
//						
//						int index_hurufDalamList = getListHurufIndexByCharacter(huruf_pertama);
//						
//						int jml_huruf = listHuruf.get(index_hurufDalamList).getJumlah();
//						listHuruf.set(index_hurufDalamList, new Abjad(huruf_pertama, jml_huruf+1));
//						
//						String huruf  = listHuruf.get(index_hurufDalamList).getHuruf();
//						int char_num = listHuruf.get(index_hurufDalamList).getJumlah();
//						
//						int indexHurufDalamListBarang = getBarangListIndexByName(huruf_pertama);
//						
//						getAllBarangList().set( indexHurufDalamListBarang, new Barang(huruf, char_num) );
//						
//						break;
//					}
//					
//					
//				}
//				
//			}
			
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
	

	/**
	 * Memasukkan satu object Barang
	 * @param barang
	 */
	public void addToListBarang(Barang barang) {
		barang_data_full.add(barang);
	}
	
	/**
	 * Memasukkan list data Barang. <b> Bukan Object Barang </b>
	 * @param barang
	 */
	public void addToListBarangFull(List<Barang> listBarang) {
		barang_data_full = listBarang;
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
	/////////////////////////BARANG-END//////////////////////////////
	
	////////////////////////////////////////////////////////////////
	///////////////////////HURUF-HEADER/////////////////////////////	
	
	
	/**
	 * Kelas utk menampung data huruf abjad dan berapa banyak data per huruf.
	 * @author Anwar Pasaribu
	 * @version 1.0
	 */
	class Abjad{
		private String huruf;
		private int jumlah;
		
		public Abjad(String huruf, int jumlah) {
			super();
			this.setHuruf(huruf);
			this.setJumlah(jumlah);
		}
		
		public String getHuruf() {
			return huruf;
		}
		
		public void setHuruf(String huruf) {
			this.huruf = huruf;
		}
		
		public int getJumlah() {
			return jumlah;
		}
		
		public void setJumlah(int jumlah) {
			this.jumlah = jumlah;
		}
		
		
	}
	
	public int getSize_listHuruf() {
		return listHuruf.size();
	}
	
	/**
	 * Menemukan indeks huruf dalam list
	 * @param character
	 * @return int (index)
	 */
	public int getListHurufIndexByCharacter(String character) {
		
		for(int i = 0; i < listHuruf.size(); i++) {
			if(listHuruf.get(i).getHuruf().equals(character)) {
				return i;
			}
		}
		
		return 0;
		
	}
	
	public boolean isCharPresentOnListHuruf(String character) {
		
		for(int i = 0; i < listHuruf.size(); i++) {
			if(listHuruf.get(i).getHuruf().equals(character)) {
				return true;
			}
		}
		
		return false;
	}
	///////////////////////HURUF-HEADER-END////////////////////////
	
	////////////////////////////////////////////////////////////////
	///////////////////////SUPPLIER/////////////////////////////////
	
	/**
	 * Mengambil seluruh data supplier.
	 * @return List &lt;Supplier&gt;
	 */
	public ArrayList<Supplier> getList_supplier() {
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
