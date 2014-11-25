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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Request.Priority;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.pasaribu.store.control.AppsController;
import com.pasaribu.store.control.CustonJsonObjectRequest;
import com.pasaribu.store.control.GetCloudData;
import com.pasaribu.store.model_data.AppsConstanta;
import com.pasaribu.store.model_data.Barang;
import com.pasaribu.store.model_data.Brand;
import com.pasaribu.store.model_data.Supplier;
import com.pasaribu.store.view.CustomListHome;
import com.pasaribu.store.view.CustomListHome.CustomListHomeListener;

public class Home extends Fragment implements CustomListHomeListener{
	
	protected static final String TAG = Home.class.getSimpleName();
	private ProgressDialog pDialog;
	
	private String tag_json_obj = "jobj_data_home_req";		
	private ListView list_home, list_recently;	
	private CustomListHome customListHome;	
	private AppsController aController;
	private GetCloudData getCloudData;
	private CustonJsonObjectRequest dataBarang_jsonObjReq;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View android = inflater.inflate(R.layout.activity_home, container, false);
		
		//Inisialisasi Progress Dialog Box
		pDialog = new ProgressDialog(getActivity());
		pDialog.setMessage("Loading...");
		pDialog.setCancelable(false);
		
		//Application Controller, berisi data ter-share utk semua activity.
		aController = (AppsController) getActivity().getApplicationContext();
		
		getCloudData = new GetCloudData();
		
		list_home = (ListView) android.findViewById(R.id.list_home);	
		list_recently = (ListView) android.findViewById(R.id.list_recently);
		
		list_recently.setItemsCanFocus(false);
		list_recently.setFocusable(false);
		list_recently.setFocusableInTouchMode(false);
		list_recently.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String list_content = parent.getAdapter().getItem(position).toString().trim();
				
				if(list_content.equals("Daftar IP"))
					getBrandSupplierData();
				
				Toast.makeText(getActivity(), "List Recently : " + list_content, Toast.LENGTH_SHORT).show();
				
			}
		});
		
		//TODO : Sementara Utk list recently
		populateRecentlyList();
		
		//Mengambil data list_home dari MySQL Database (Online)
		requestAllDataBarangJSONObject();
		
		

		Log.d(TAG, "Setelah Panggilan populateListDataBarang() ");
		return android;
	}


	private void getBrandSupplierData() {
		
		Map<String, String> data_temp = new HashMap<String, String>();
		data_temp.put("id_user", "1");
		
		//Mengambil data Brand dan Supplier dari MySQL kemudia mem-parse ke Apps Controller
		getCloudData.requestJSONObject(AppsConstanta.URL_SUPPLIER_AND_BRAND, data_temp, "tag_brand_and_supplier");
		
		if(!(getCloudData.getJsonObject() == null)) {
			parseBrandSupplierJSONObject( getCloudData.getJsonObject() );
			Toast.makeText(getActivity(), "Data Sudah Masuk", Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(getActivity(), "Data Kosong", Toast.LENGTH_LONG).show();
		}
		
	}
	
	
	public void requestAllDataBarangJSONObject() {	
		
		showProgressDialog();
		
		Map<String, String> data_request = new HashMap<String, String>();
		data_request.put("id_user", "1"); //TODO Guna - Asumsikan user aktif dengan id_user = 1
		
		Log.i(TAG, "Data Request : " + data_request.toString());
		
		dataBarang_jsonObjReq = new CustonJsonObjectRequest(
				Method.POST,
				AppsConstanta.URL_DATA, 
				data_request, 
				new Response.Listener<JSONObject>() {
	
					@Override
					public void onResponse(JSONObject response) {
						
						//Cek Header Data JSON
						if(response.isNull(AppsConstanta.JSON_HEADER_BARANG) 
								&& response.isNull(AppsConstanta.JSON_HEADER_DATA_SIZE) ) {
							try {
								
								String msg = response.getString(AppsConstanta.JSON_HEADER_MESSAGE);
								showAlertDialog("Network Database Error ", 
										msg + ", but can't access network database.\nPlease check network connectivity, or please try again");

							} catch (JSONException e) {
								e.printStackTrace();
								Log.e(TAG, e.getMessage());
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
				
		
		dataBarang_jsonObjReq.setPriority(Priority.IMMEDIATE);
		
	
		// Adding request to request queue (Menjalankan Request)
		// TODO Lakukan pemeriksaan cache sehingga tidak dilakukan request ke server lagi.
		AppsController.getInstance().addToRequestQueue(dataBarang_jsonObjReq, tag_json_obj);		
		
	} 	
	
	
	private void parseReceivedJSONObject(JSONObject responseJsonObject) {
		
		//TODO Olah JSONObject data, jika pemanggilan Volley normal
    	try {
    		
    		if(!responseJsonObject.isNull(AppsConstanta.JSON_HEADER_BARANG) ) {
    			
	    		JSONObject jsonResponse 		= new JSONObject(responseJsonObject.toString());    		
	    		int data_barang_size_new 		= jsonResponse.getInt(AppsConstanta.JSON_HEADER_DATA_SIZE);
	    		JSONArray jArray_dataBarang 	= jsonResponse.getJSONArray(AppsConstanta.JSON_HEADER_BARANG);    		
	    		JSONArray jArray_SupplierData 	= jsonResponse.getJSONArray(AppsConstanta.JSON_HEADER_SUPPLIER);
	    		JSONArray jArray_BrandData 		= jsonResponse.getJSONArray(AppsConstanta.JSON_HEADER_BRAND);

	    		int jArray_dataBarang_length 	= jArray_dataBarang.length();
	    		int data_barang_size_local 		= aController.getBarangArrayListSize();
	    		
	    		// Memeriksa apakah ukuran data barang di aController sama dengan data yang baru masuk
	    		// Jika sama, data pada list di home akan tetap, jika tidak
	    		// Akan di olah lebih lanjut     		
	    		if(data_barang_size_new != data_barang_size_local ) {
	    			
	    			if(data_barang_size_new > data_barang_size_local) {				
	    						
							for (int i = data_barang_size_local; i < jArray_dataBarang_length; i++ ) {							
								JSONObject jsonObject = jArray_dataBarang.getJSONObject(i);
								aController.addToListBarangFull(aController.createBarangFromJSONObject(jsonObject));					
							}
	    				
	    			} else if( data_barang_size_new < data_barang_size_local ) {
	    				
	    				aController.clearAllBarangList();
	    				
	    				for (int i = 0; i < jArray_dataBarang_length; i++ ) {							
							JSONObject jsonObject = jArray_dataBarang.getJSONObject(i);
							aController.addToListBarangFull(aController.createBarangFromJSONObject(jsonObject));					
						}
	    				
	    			}  			    			
				
	    		} else {
	    			populateListDataBarang();
	    		}
	    		
	    		//TODO: Untuk mengambil data dari JSONArray Supplier
	    		int jArray_SupplierDataLength = jArray_SupplierData.length();
				for (int i = 0; i < jArray_SupplierDataLength; i++ ) {
					
					JSONObject jsonObject = jArray_SupplierData.getJSONObject(i);
					
					aController.setList_supplier(new Supplier(
							jsonObject.getInt(Supplier.ID_PENJUAL), 
							jsonObject.getString(Supplier.NAMA_PENJUAL), 
							jsonObject.getString(Supplier.NAMA_TOKO), 
							jsonObject.getString(Supplier.ALAMAT_TOKO), 
							jsonObject.getString(Supplier.GEOLOCATION), 
							jsonObject.getString(Supplier.KONTAK_TOKO), 
							jsonObject.getString(Supplier.EMAIL_TOKO) 
							));	
					
									
				}
				
				//TODO: Untuk mengambil data dari JSONArray Brand
	    		int jArray_BrandDataLength = jArray_BrandData.length();
				for (int i = 0; i < jArray_BrandDataLength; i++ ) {
					
					JSONObject jsonObject = jArray_BrandData.getJSONObject(i);
					
					aController.setList_brand(new Brand(
							jsonObject.getInt(Brand.ID_MEREK), 
							jsonObject.getString(Brand.NAMA_MEREK), 
							jsonObject.getString(Brand.LOGO_MEREK), 
							jsonObject.getString(Brand.DESKRIPSI_MEREK) 
							));	
					
									
				}
	
				
				Log.i(TAG, "Ukuran Data : " + data_barang_size_new);
			
				
				
    		} 			
			
		} catch (Exception e) {
			Log.e(TAG, "Gagal : Tidak dapat mengambil data JSON saat parseJSONObject. Message : " + e.getMessage());
		} finally {
			
			populateListDataBarang();
			
		}  	
    	
    	
	} //END - Parse JSONObject Data Barang 	
	
	
	private void parseBrandSupplierJSONObject(JSONObject responseJsonObject) {
		Log.i(TAG, "( Parse data Brand dan Supplier ) Data : " + responseJsonObject.toString());
		
		try {
    		
    		JSONObject jsonResponse = new JSONObject(responseJsonObject.toString());    		
    		JSONArray jArray_SupplierData = jsonResponse.getJSONArray(AppsConstanta.JSON_HEADER_SUPPLIER);
    		JSONArray jArray_BrandData = jsonResponse.getJSONArray(AppsConstanta.JSON_HEADER_BRAND);
    		
			//TODO: Untuk mengambil data dari JSONArray Supplier
    		int jArray_SupplierDataLength = jArray_SupplierData.length();
			for (int i = 0; i < jArray_SupplierDataLength; i++ ) {
				
				JSONObject jsonObject = jArray_SupplierData.getJSONObject(i);
				
				aController.setList_supplier(new Supplier(
						jsonObject.getInt(Supplier.ID_PENJUAL), 
						jsonObject.getString(Supplier.NAMA_PENJUAL), 
						jsonObject.getString(Supplier.NAMA_TOKO), 
						jsonObject.getString(Supplier.ALAMAT_TOKO), 
						jsonObject.getString(Supplier.GEOLOCATION), 
						jsonObject.getString(Supplier.KONTAK_TOKO), 
						jsonObject.getString(Supplier.EMAIL_TOKO) 
						));	
				
								
			}
			
			//TODO: Untuk mengambil data dari JSONArray Brand
    		int jArray_BrandDataLength = jArray_BrandData.length();
			for (int i = 0; i < jArray_BrandDataLength; i++ ) {
				
				JSONObject jsonObject = jArray_BrandData.getJSONObject(i);
				
				aController.setList_brand(new Brand(
						jsonObject.getInt(Brand.ID_MEREK), 
						jsonObject.getString(Brand.NAMA_MEREK), 
						jsonObject.getString(Brand.LOGO_MEREK), 
						jsonObject.getString(Brand.DESKRIPSI_MEREK) 
						));	
				
								
			}
			
		} catch (Exception e) {
			Log.e(TAG, "Tidak dapat mengambil data JSONObject Brand dan Supplier. Message : " + e.getMessage());
		}
		
	} 
	
	
	
	/**
	 * <strong>Mengisi List Home / List Utama</strong>
	 * <p>Menangani List Utama pada halaman Home. Dalam method sudah terdapat akses data
	 * ke MySQL database melalui GetCloudData.java sesuai URL dan data yang akan di request.</p>
	 */
	private void populateListDataBarang()  {	
		
//		Map<String, String> data_request = new HashMap<String, String>();
//		data_request.put("id_user", "1"); //TODO Guna - Asumsikan user aktif dengan id_user = 1
//		
//		getCloudData.requestJSONObject(AppsConstanta.URL_DATA, data_request);
//		JSONObject received_JSONObject = getCloudData.getJsonObject();
//		
//		//Periksa JSONObject, Apakah Ada?
//		if( !isNullJSONObject(received_JSONObject) ) {
//			parseJSONObject(received_JSONObject);
//		} else {
//			
//		}
//		
		
		
		
		int data_size = aController.getBarangArrayListSize();		
		
		if (data_size > 0) {
			
			list_home.setAdapter(null);
			
			//Set Adapter utk list_home. List barang diperoleh dari aController.
			customListHome = new CustomListHome(getActivity(), aController.getAllBarangList());
			customListHome.setCallBack(this);
			list_home.setItemsCanFocus(false);
			list_home.setFocusable(false);
			list_home.setFocusableInTouchMode(false);
			list_home.setAdapter(customListHome);
			
			list_home.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					
					Barang selected_data_barang = (Barang) aController.getBarangAtPosition(position);
					int id_barang = selected_data_barang.getId_barang();
					
					//TODO Membuka activity baru (ProductDetails) dengan mengirim 
					//variable id_barang aktif dan index data barang di list melalui metode putExtra().
					Intent intent_product_detail = new Intent(getActivity(), ProductDetail.class);
					intent_product_detail.putExtra(Barang.ID_BARANG, id_barang);
					intent_product_detail.putExtra("list_barang_index", position);
					startActivity(intent_product_detail);
					
				}
			});
			
		} else  {		
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
