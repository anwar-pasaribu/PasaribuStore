package com.pasaribu.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.gson.Gson;
import com.pasaribu.store.control.AppsController;
import com.pasaribu.store.control.CustonJsonObjectRequest;
import com.pasaribu.store.model_data.AppsConstanta;
import com.pasaribu.store.model_data.Barang;
import com.pasaribu.store.model_data.Brand;
import com.pasaribu.store.model_data.Supplier;
import com.pasaribu.store.view.CustomListHome;
import com.pasaribu.store.view.CustomListHome.CustomListHomeListener;

public class Home extends Fragment implements CustomListHomeListener{
	
	private ArrayList<Barang> barang_data_full = new ArrayList<Barang>();;
	
	protected static final String TAG = Home.class.getSimpleName();
	private ProgressDialog pDialog;
	
	private String tag_json_obj = "jobj_data_home_req";		
	private ListView list_home, list_recently;	
	private CustomListHome adapterListHome;	
	private AppsController aController;
	private CustonJsonObjectRequest dataBarang_jsonObjReq;
	
	int start_from = 0;
	int limit = 10;
	boolean loadingMore = false ;
	
	private View viewHome = null;	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i(TAG, "onCreateView");
		
		viewHome = inflater.inflate(R.layout.activity_home, container, false);
		
		//Inisialisasi Progress Dialog Box
		pDialog = new ProgressDialog(getActivity());
		pDialog.setMessage("Loading...");
		pDialog.setCancelable(false);
		
		//Application Controller, berisi data ter-share utk semua activity.
		aController = (AppsController) getActivity().getApplicationContext();		
		
		//List Home inisialization
		list_home = (ListView) viewHome.findViewById(R.id.list_home);
		//barang_data_full.add(new Barang("Data Pertama", 1));
		adapterListHome = new CustomListHome(getActivity(), barang_data_full);
		
			
		
		//Inisialisasi List Recently
		list_recently = (ListView) viewHome.findViewById(R.id.list_recently);		
		list_recently.setItemsCanFocus(false);
		list_recently.setFocusable(false);
		list_recently.setFocusableInTouchMode(false);
		list_recently.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String list_content = parent.getAdapter().getItem(position).toString().trim();
				
				Toast.makeText(getActivity(), "List Recently : " + list_content, Toast.LENGTH_SHORT).show();
				if(position == 0 || list_content.matches("Daftar IP")) {
					//Test tambah data list Home
					
					int rand_num = 0 + (int) (Math.random() * ( (100 - 0) + 1) );
					adapterListHome.add(new Barang("Data Added", rand_num));
					adapterListHome.notifyDataSetChanged();
				}
			}
		});
		
		//Sementara Utk list recently
		populateRecentlyList();
		
		//Mengambil data list_home dari MySQL Database (Online)	
		if(aController.isNetworkAvailable()) {
			requestAllDataBarangJSONObject();
			Log.d(TAG, "requestAllDataBarangJSONObject called");
		} else {
			
			getCacheData();
			Log.d(TAG, "getCacheData called");
			
		}
		
		//Untuk mengisi list_home dengan data
		
		populateListDataBarang();
		Log.d(TAG, "populateListDataBarang called");
		
		return viewHome;
	}
	


	/**
	 * Method hanya dilakukan jika ada jaringan internet
	 */
	public void requestAllDataBarangJSONObject() {	
		
		showProgressDialog();
		loadingMore = true; //Menyamai tutorial
		aController.isExecuted = true; //Karena di asumsikan setelah melalui akses jaringan data barang sudah ada pembaruan.
		
		Map<String, String> data_request = new HashMap<String, String>();
		data_request.put("id_user", "1"); //TODO Guna - Asumsikan user aktif dengan id_user = 1
		data_request.put("start_from", String.valueOf(start_from));
		data_request.put("limit", String.valueOf(limit));
		
		Log.i(TAG, "Data Request : " + data_request.toString());
		
		dataBarang_jsonObjReq = new CustonJsonObjectRequest(
				Method.POST,
				AppsConstanta.URL_DATA, 
				data_request, 
				new Response.Listener<JSONObject>() {
	
					@Override
					public void onResponse(JSONObject response) {
//						Gson gson = new GsonBuilder().setPrettyPrinting().create();						
//						Log.i(TAG, "onResponse Listener, Data : " + gson.toJson(response));
						
						//Cek Header Data JSON, jika data barang tidak ada
						if(response.isNull(AppsConstanta.JSON_HEADER_BARANG) 
								&& response.isNull(AppsConstanta.JSON_HEADER_DATA_SIZE) ) {
							try {
								
								String msg = response.getString(AppsConstanta.JSON_HEADER_MESSAGE);
								
								if(msg.equals(AppsConstanta.MESSAGE_SUCCESS)) {
									msg = "Berhasil, Akses data jaringan berhasil dilakukan.";
								} else {
									msg = "Gagal, Akses data jaringan tidak bisa dilakukan.";
								}
								
								Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();

							} catch (JSONException e) {
								e.printStackTrace();
							}
							
						} else {
																					
							parseReceivedJSONObject(response);
						}
						
						hideProgressDialog();
						
					}
				}, new Response.ErrorListener() {	
					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.d(TAG, "Error: " + error.toString());
						Log.e(TAG, "ErrorResponse : " + error.toString());
						hideProgressDialog();
						
					}
				}){};
				
		
		dataBarang_jsonObjReq.setShouldCache(true);
		
		// Lakukan pemeriksaan cache sehingga tidak dilakukan request ke server lagi.		
		if(AppsController.getInstance().getRequestQueue().getCache().get(AppsConstanta.URL_DATA) != null 
				&& !aController.isNetworkAvailable()) {
			
			getCacheData();
			
		} else {
			Log.i(TAG, "Mengambil data dari jaringan.");
			AppsController.getInstance().addToRequestQueue(dataBarang_jsonObjReq, tag_json_obj);
		}
		
	}




	/**
	 * Mengambil data dari cache dan mem-parseny, setelah eksekusi method ini panggil <b>populateListDataBarang()</b>.
	 * @throws JSONException Pada eksekusi
	 * 
	 */
	private void getCacheData() {
		
		if(AppsController.getInstance().getRequestQueue().getCache().get(AppsConstanta.URL_DATA) != null) {
			
			Log.i(TAG, "Mengambil data dari cache.");
			
			String cachedResponse = new String( AppsController.getInstance().getRequestQueue().getCache().get(AppsConstanta.URL_DATA).data ); 
			JSONObject cachedJsonObject = null;
			
			try {
				cachedJsonObject = new JSONObject(cachedResponse);
			} catch (JSONException e) {
				e.printStackTrace();
			} finally {
				cetakSimpleDataBarang("Semua data di cache : ", cachedResponse);
				aController.isExecuted = true; //utk memastikan bisa ekesekusi jika offline
				parseReceivedJSONObject(cachedJsonObject);
			}
			
		} else {
			Log.e(TAG, "Tidak ada data di cache!");
		}
		
		
		
	} 	
	
	
	private void cetakSimpleDataBarang(String title, String stringResponse) {
		JSONObject jsonResponse = null;
		Log.v(TAG, title);
		try {
			jsonResponse = new JSONObject(stringResponse.toString());		
			JSONArray jArray_dataBarang = null;	
			jArray_dataBarang = jsonResponse.getJSONArray(AppsConstanta.JSON_HEADER_BARANG);		
			
			for(int i  = 0; i < aController.getAllBarangList().size(); i++) {
				
					JSONObject jsonObject = jArray_dataBarang.getJSONObject(i);
					
					Log.d(TAG, i + " - Nama : " + jsonObject.getString(Barang.NAMA_BARANG) );
			}
		
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}




	private void parseReceivedJSONObject(JSONObject responseJsonObject) {
		
		JSONObject responseObject = null;
		Log.i(TAG,"Parsing data dilakukan, isExecuted : " + String.valueOf(aController.isExecuted));
		
    	if (aController.isExecuted) {	
    		
			try {

				Gson gson = new Gson();
				int i = 0; // Mengurangi pengulangan inisalisasi var

				responseObject = new JSONObject(responseJsonObject.toString());
				JSONArray jArray_dataBarang = responseObject.getJSONArray(AppsConstanta.JSON_HEADER_BARANG);
				JSONArray jArray_SupplierData = responseObject.getJSONArray(AppsConstanta.JSON_HEADER_SUPPLIER);
				JSONArray jArray_BrandData = responseObject.getJSONArray(AppsConstanta.JSON_HEADER_BRAND);
				JSONArray jArray_CategoryData = responseObject.getJSONArray(AppsConstanta.JSON_HEADER_CATEGORY);
				String query = responseObject.getString("QUERY");
				
				int isEndOfData = responseObject.getInt(AppsConstanta.JSON_HEADER_END_OF_DATA);

				int data_barang_size_new = responseObject.getInt(AppsConstanta.JSON_HEADER_DATA_SIZE);
				
				Log.v(TAG, "Query, " + query);
				if(isEndOfData == 1) {
					Log.w(TAG, "Akhir seluruh data, " + isEndOfData);
				} else {
					Log.v(TAG, "Tidak Akhir seluruh data, " + isEndOfData);
				}
				
				//Ukuran barang lokal di kurang dengan banyak header
				//int data_barang_size_local = aController.getBarangArrayListSize()- aController.getSize_listHuruf(); 

				//TODO: Untuk mengambil data dari JSONArray Barang
				//data_barang_size_new > 0 && data_barang_size_local != data_barang_size_new
				if (jArray_dataBarang.length() == 0) {

					showAlertDialog("Data Barang Kosong", "Data barang tidak ada, silahkan restart aplikasi");

				} else {
					Log.d(TAG, "Jumlah data on parse: " + data_barang_size_new);

					//JSONObject jsonObject = null;	  	

					for (i = 0; i < jArray_dataBarang.length(); i++) {

						start_from++; //untuk memperbarui index data dimulai dari mana

						//jsonObject = jArray_dataBarang.getJSONObject(i);
						//aController.addToListBarangFull(aController.createBarangFromJSONObject(jsonObject));

						String barangInfo = jArray_dataBarang.getJSONObject(i).toString();
						Barang barang = gson.fromJson(barangInfo, Barang.class); //Dengan gson, mengubah json menjadi data Barang
						
						aController.addToListBarang(barang);
						barang_data_full.add(barang);
						adapterListHome.add(barang);
						
					}
					
					aController.addToListBarangFull(barang_data_full); //Add list data Barang (bukan object Barang)	
					adapterListHome.notifyDataSetChanged();
					loadingMore = false;
					

				}

				//TODO: Untuk mengambil data dari JSONArray Supplier
				int jArray_SupplierDataLength = jArray_SupplierData.length();
				if (aController.getList_supplier().size() != jArray_SupplierDataLength) {

					aController.clearAllSupplierList();

					for (i = 0; i < jArray_SupplierDataLength; i++) {
						JSONObject jsonObject = jArray_SupplierData
								.getJSONObject(i);
						aController.setList_supplier(new Supplier(jsonObject
								.getInt(Supplier.ID_PENJUAL), jsonObject
								.getString(Supplier.NAMA_PENJUAL), jsonObject
								.getString(Supplier.NAMA_TOKO), jsonObject
								.getString(Supplier.ALAMAT_TOKO), jsonObject
								.getString(Supplier.GEOLOCATION), jsonObject
								.getString(Supplier.KONTAK_TOKO), jsonObject
								.getString(Supplier.EMAIL_TOKO)));

					}
				}

				//TODO: Untuk mengambil data dari JSONArray Brand
				int jArray_BrandDataLength = jArray_BrandData.length();
				if (aController.getList_brand().size() != jArray_BrandDataLength) {

					aController.getList_brand().clear();

					for (i = 0; i < jArray_BrandDataLength; i++) {
						JSONObject jsonObject = jArray_BrandData
								.getJSONObject(i);
						aController.setList_brand(new Brand(jsonObject
								.getInt(Brand.ID_MEREK), jsonObject
								.getString(Brand.NAMA_MEREK), jsonObject
								.getString(Brand.LOGO_MEREK), jsonObject
								.getString(Brand.DESKRIPSI_MEREK)));

					}
				}

				//TODO: Untuk mengambil data dari JSONArray Category
				int jArray_CategoryDataLength = jArray_CategoryData.length();
				if (aController.getList_product_category().size() != jArray_CategoryDataLength) {

					aController.getList_product_category().clear();

					for (i = 0; i < jArray_CategoryDataLength; i++) {
						JSONObject jsonObject = jArray_CategoryData
								.getJSONObject(i);
						aController.setList_product_category(jsonObject
								.getString("kategori_barang"));
					}
				}

				Log.i(TAG, "Ukuran Data Barang : " + data_barang_size_new);
				Log.i(TAG, "Ukuran Data Supplier jArray : "
						+ jArray_SupplierDataLength + ", aController : "
						+ aController.getList_supplier().size());
				Log.i(TAG, "Ukuran Data Brand jArray : "
						+ jArray_BrandDataLength + ", aController : "
						+ aController.getList_brand().size());
				
				
				//Setiap pemanggilan keseluruhan, isExecuted akan di ubah menjadi false jika tidak ada jaringan net.
				//Untuk membuat parsing hanya dilakukan sekali.
				aController.isExecuted = false;
				Log.i(TAG,"Parsing data selesai dilakukan, isExecuted : " + String.valueOf(aController.isExecuted));

			} catch (JSONException e) {

				Log.e(TAG,"Gagal : Tidak dapat mengambil data JSON saat parseJSONObject. Message : " + e.getMessage());
				e.printStackTrace();

			}
			
		} else {
			Log.w(TAG, "Semua data cache sudah di olah");
		}
    	 //End isExecuted check    	
    	
    	
	} 
	/////END - Parse JSONObject Data Barang///////////////////////////////////////////// 	
	
	
	/**
	 * <strong>Mengisi List Home / List Utama</strong>
	 * <p>Menangani List Utama pada halaman Home. Dalam method sudah terdapat akses data
	 * ke MySQL database melalui GetCloudData.java sesuai URL dan data yang akan di request.</p>
	 */
	private void populateListDataBarang()  {	
		
//		int data_size = aController.getBarangArrayListSize();
		int data_size = barang_data_full.size() == 0 ? 1 : aController.getBarangArrayListSize();
		
		Log.i(TAG, "Data barang size on populate: " + data_size);		
		
		if (data_size > 0) {			
				
			
			adapterListHome.setCallBack(this);
			
			list_home.setAdapter(adapterListHome);
			list_home.setTextFilterEnabled(true); //new entry 
			list_home.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					
					Barang selected_data_barang = (Barang) aController.getBarangAtPosition(position);
					int id_barang = selected_data_barang.getId_barang();
					
					if(id_barang != 0) {
						//TODO Membuka (ProductDetails) dengan mengirim 
						//variable id_barang aktif dan index data barang di list melalui metode putExtra().
						Intent intent_product_detail = new Intent(getActivity(), ProductDetail.class);
						intent_product_detail.putExtra(Barang.ID_BARANG, id_barang);
						intent_product_detail.putExtra("list_barang_index", position);
						startActivity(intent_product_detail);
					} else {
						Log.w(TAG, "ID : " + id_barang + " merupakan header list");
					}
					
				}
			});
			
			//Aksi pada setiap scrolling pada list_home / List Utama Home (12/12/14)
			list_home.setOnScrollListener(new OnScrollListener() {
				
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {}
				
				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					
					int lastInScreen = firstVisibleItem + visibleItemCount;					
					
					if(lastInScreen == totalItemCount && !(loadingMore) ) {
						Log.v(TAG, "Start Data : " + start_from + ", Limit : " + limit);
						
						//Aksi ambil database jika ada internet
						if(aController.isNetworkAvailable())
							requestAllDataBarangJSONObject();							
							
					} 
					
				}
			});
			
			Log.v(TAG, "Data barang di Application Controller size : " + aController.getBarangArrayListSize());
			
			
		} else if(data_size == 0)  {	
			//Jika tidak ada data hasil parsing JSONObject
			List<String> data_list_recently = new ArrayList<String>();
		    data_list_recently.add("No Data Found!");
		    
		    ArrayAdapter<String> newAdapter = new ArrayAdapter<String> (
		    		getActivity(), 
		    		android.R.layout.simple_list_item_1,
		    		data_list_recently
		    		);
		    
		    list_home.setAdapter(newAdapter);
		    
		} 
		
	}

//	private boolean isNullJSONObject(JSONObject response) {
//		
//		if(response.isNull(AppsConstanta.JSON_HEADER_BARANG) 
//				&& response.isNull(AppsConstanta.JSON_HEADER_DATA_SIZE) ) {
//			try {
//				
//				String msg = response.getString(AppsConstanta.JSON_HEADER_MESSAGE);
//				showAlertDialog("Network Database Error ", 
//						msg + ", but can't access network database.\nPlease check network connectivity, or please try again");
//
//				return false;
//				
//			} catch (JSONException e) {
//				e.printStackTrace();
//				Log.e(TAG, e.getMessage());
//			}
//		} 
//		
//		return true;
//		
//	}
	

	private void populateRecentlyList() {		
		
		List<String> data_list_recently = new ArrayList<String>();
	    data_list_recently.add("Daftar IP");
	    data_list_recently.add(getHostName(null));
	    
	    ArrayAdapter<String> newAdapter = new ArrayAdapter<String> (
	    		getActivity(), 
	    		android.R.layout.simple_list_item_1,
	    		data_list_recently
	    		);
	    
	    list_recently.setAdapter(newAdapter);
	    
	}




	public void showAlertDialog(String title, String message) {		
		
		new AlertDialog.Builder(getActivity())
		.setTitle(title)
		.setMessage(message)
		.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		}).show();
		
	}

	

	@Override
	public void deleteDataBarangSuccess(boolean status) {
		// TODO Lakukan jika proses hapus data berhasil
		if(status) {
			requestAllDataBarangJSONObject();
			showAlertDialog("Data Berhasil Dihapus", "Data sudah dihapus dari database MySQL.");
		}
		
	}
	
	public static String getHostName(String defValue) {
		try {
			java.lang.reflect.Method getString = Build.class.getDeclaredMethod("getString", String.class);
			getString.setAccessible(true);
			return getString.invoke(null, "net.hostname").toString();
		} catch (Exception e) {
			return defValue;
		}
	}

	private void showProgressDialog() {
		if (!pDialog.isShowing())
			pDialog.show();
	}

	private void hideProgressDialog() {
		if (pDialog.isShowing())
			pDialog.hide();
	}
	
}
