package com.pasaribu.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.pasaribu.store.control.AppsController;
import com.pasaribu.store.control.GetCloudData;
import com.pasaribu.store.model_data.Barang;
import com.pasaribu.store.model_data.ListRecentlyProduct;
import com.pasaribu.store.view.CustomListHome;

public class Home extends Fragment {
	
	// from androidhive.com -start- //
	
	protected static final String TAG = Home.class.getSimpleName();
	protected static final String JSON_HEADER = "barang";
	// These tags will be used to cancel the requests
	private String tag_json_obj = "jobj_req";
	private ProgressDialog pDialog;
	
	// from androidhive.com -end- //
	
	
	private List<Barang> DataBarang_home = new ArrayList<Barang>();
	private List<ListRecentlyProduct> DataBarang_recently = new ArrayList<ListRecentlyProduct>();
	
	private ListView list_home, list_recently;
	
	private GetCloudData getCloudData;
	private CustomListHome customListHome;
	
	private AppsController aController;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View android = inflater.inflate(R.layout.activity_home, container, false);
		
		//Inisialisasi Progress Dialog Box
		pDialog = new ProgressDialog(getActivity());
		pDialog.setMessage("Loading...");
		pDialog.setCancelable(false);
		
		aController = (AppsController) getActivity().getApplicationContext();
		
		getCloudData = new GetCloudData(); //Kelas utk memperoleh data utk ditampilkan di Home
		
		list_home = (ListView) android.findViewById(R.id.list_home);
		list_recently = (ListView) android.findViewById(R.id.list_recently);
		
		populateListDataBarang();
		makeJsonObjReq();
	    
	    Log.i(TAG, "Setelah memanggil makeJsonObjectRequest :)");
		
		return android;
	}
	
	//Methode Kosong
	public void populateListDataBarang() {
//		
//		//Menjalankan proses pengambilan data
//		//Hanya bisa dipanggil sekali (ERROR jika tidak)
////        getCloudData.execute(); 
////        
////        //Memperoleh data dari proses asynctask
////	    try {
////	    	//TODO Menyalin data "barang" full ke variable aController agar bisa di akses pada activity yang lain
////			this.DataBarang_home = getCloudData.get();			
////			int data_size = DataBarang_home.size();
////			for(int i = 0; i < data_size; i++) {
////				aController.setBarang(DataBarang_home.get(i));
////			}
////			
////		} catch (InterruptedException e) {
////			e.printStackTrace();
////		} catch (ExecutionException e) {
////			e.printStackTrace();
////		} finally {
////			Toast.makeText(
////					getActivity(), 
////					"Ukuran Data Barang : " + aController.getBarangArrayListSize(), 
////					Toast.LENGTH_LONG
////					).show();
////		}	    
////	    
////	    customListHome = new CustomListHome(getActivity(), aController.getAllBarangList());
////	    list_home.setAdapter(customListHome);
//	    
//	    
//	    makeJsonObjReq();
//	    
//	    Log.i(TAG, "Setelah memanggil makeJsonObjectRequest :)");
//	    
	}
	
	 //from androidhive.com -start- //
	private void makeJsonObjReq() {
		
		Log.i(TAG, "Memanggil makeJsonObjectRequest :)");
		
		showProgressDialog();
		JsonObjectRequest jsonObjReq = new JsonObjectRequest(
				Method.GET,
				"http://192.168.56.5/pasaribu_store/function/getDataBarang.php", 
				null,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						Log.d(TAG, response.toString());
						
						//TODO Olah JSONObject data, respon dari pemanggilan
				    	try {
				    		JSONObject jsonResponse = new JSONObject(response.toString());
							//JSONArray jArray 		= new JSONArray(result);
				    		JSONArray jArray 		= jsonResponse.getJSONArray(JSON_HEADER);
							
				    		Log.i("DataJSJONResponse", "DataJSJONResponse : " + jsonResponse.toString());
				    		Log.i("DataJSJONArray", "DataJSJONArray : " + jArray.toString());
				    		
							//TODO: Untuk mengambil data dari JSONArray
							for (int i = 0; i < jArray.length(); i++ ) {
								
								JSONObject jsonObject = jArray.getJSONObject(i);
								
								//Menyimpan data ke Data Structure dataBarang (ke dalam List<Barang>)
								aController.setBarang(new Barang(
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
										));								
												
							}

							
						} catch (Exception e) {
							Log.e(TAG, "Fail : Tidak dapat mengambil data JSON");
						}
						
						
						//Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
						
						hideProgressDialog();
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.d(TAG, "Error: " + error.getMessage());
						hideProgressDialog();
					}
				}) {

			/**
			 * Passing some request headers
			 * */
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("Content-Type", "application/json");
				return headers;
			}

//			@Override
//			protected Map<String, String> getParams() {
//				Map<String, String> params = new HashMap<String, String>();
//				params.put("name", "Androidhive");
//				params.put("email", "abc@androidhive.info");
//				params.put("pass", "password123");
//
//				return params;
//			}

		};

		// Adding request to request queue
		AppsController.getInstance().addToRequestQueue(jsonObjReq,
				tag_json_obj);

		// Cancelling request
		// ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);		
	
		//TODO Set Adapter utk list_home
		customListHome = new CustomListHome(getActivity(), aController.getAllBarangList());
	    list_home.setAdapter(customListHome);
	
	}
	
	private void showProgressDialog() {
		if (!pDialog.isShowing())
			pDialog.show();
	}

	private void hideProgressDialog() {
		if (pDialog.isShowing())
			pDialog.hide();
	}
	
	 //from androidhive.com -end- //

	
}
