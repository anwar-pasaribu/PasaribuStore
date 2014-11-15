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
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.pasaribu.store.control.AppsController;
import com.pasaribu.store.control.CustonJsonObjectRequest;
import com.pasaribu.store.control.GetCloudData;
import com.pasaribu.store.model_data.AppsConstanta;
import com.pasaribu.store.model_data.Barang;
import com.pasaribu.store.model_data.ListRecentlyProduct;
import com.pasaribu.store.view.CustomListHome;
import com.pasaribu.store.view.DisplayGui;

public class Home extends Fragment {
	
	// from androidhive.com -start- //
	
	protected static final String TAG = Home.class.getSimpleName();
	private ProgressDialog pDialog;
	
	//URL_DATA pindah ke 
	//private String URL_DATA 	= "http://192.168.56.5/pasaribu_store/function/getDataBarang.php"; //Hanya utk kebutuhan belajar
	
	//Var ini dipindahkan ke AppsConstanta.java
		//private String JSON_HEADER_BARANG 	= "BARANG"; //jenis kepala json
		//private String JSON_HEADER_DATA_SIZE = "DATA_BARANG_SIZE";
	private String tag_json_obj = "jobj_data_home_req";
	
	private List<Barang> DataBarang_home = new ArrayList<Barang>();
	private List<ListRecentlyProduct> DataBarang_recently = new ArrayList<ListRecentlyProduct>();
	
	private ListView list_home, list_recently;
	
	private GetCloudData getCloudData;
	private CustomListHome customListHome;
	private DisplayGui displayGui;
	
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
		displayGui = new DisplayGui(aController.getMainContext());
		
		getCloudData = new GetCloudData(); //Kelas utk memperoleh data utk ditampilkan di Home
		
		list_home = (ListView) android.findViewById(R.id.list_home);
		list_recently = (ListView) android.findViewById(R.id.list_recently);
		
		requestAllDataBarangJSONObject();
	    
		Log.d(TAG, "Setelah Panggilan populateListDataBarang() ");
		
		return android;
	}
	
	//from androidhive.com -start- //
	public void requestAllDataBarangJSONObject() {	
		
		showProgressDialog();
		
		Map<String, String> data_request = new HashMap<String, String>();
		data_request.put("id_user", "1"); //TODO Guna - Asumsikan user aktif dengan id_user = 1
		
		Log.i(TAG, "Data Request : " + data_request.toString());
		
		CustonJsonObjectRequest jsonObjReq = new CustonJsonObjectRequest(
				Method.POST,
				AppsConstanta.URL_DATA, 
				data_request, 
				new Response.Listener<JSONObject>() {
	
					@Override
					public void onResponse(JSONObject response) {
						
						//Cek Header Data JSON
						if(response.isNull(AppsConstanta.JSON_HEADER_BARANG) && response.isNull(AppsConstanta.JSON_HEADER_DATA_SIZE) ) {
							try {
								
								String msg = response.getString("msg");
								showAlertDialog("Network Database Error ", msg + ", but can't access network database.\nPlease check network connectivity, or please try again");

							} catch (JSONException e) {
								e.printStackTrace();
								Log.e(TAG, e.getMessage());
							}
						} else {						
							parseJSONObject(response);
							populateListDataBarang();
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
	
		// Adding request to request queue
		AppsController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);		
		
		} 	
		//from androidhive.com -end- //
	
	
	public void populateListDataBarang()  {	
		
		//TODO input data ke AppsController utk bisa di akses pada Activity lain
		int data_size = DataBarang_home.size();
		for(int i = 0; i < data_size; i++) {
			aController.setBarang(DataBarang_home.get(i));
		}
		
		if(data_size <= 0) {			
	
			List<String> data_list_recently = new ArrayList<String>();
		    data_list_recently.add("No Data Found!");
		    
		    ArrayAdapter<String> newAdapter = new ArrayAdapter<String> (
		    		getActivity(), 
		    		android.R.layout.simple_list_item_1,
		    		data_list_recently
		    		);
		    list_home.setAdapter(newAdapter);
		    
		} else {
			list_home.setAdapter(null);
			//TODO Set Adapter utk list_home. List barang diperoleh dari aController.
			customListHome = new CustomListHome(getActivity(), aController.getAllBarangList());
			list_home.setAdapter(customListHome);
		}
		
	}

	public void parseJSONObject(JSONObject responseJsonObject) {
		
		//TODO Olah JSONObject data, jika pemanggilan Volley normal
    	try {
    		
    		JSONObject jsonResponse = new JSONObject(responseJsonObject.toString());    		
    		int data_barang_size = jsonResponse.getInt(AppsConstanta.JSON_HEADER_DATA_SIZE);
    		JSONArray jArray_DataBarang = jsonResponse.getJSONArray(AppsConstanta.JSON_HEADER_BARANG);
    		
			//TODO: Untuk mengambil data dari JSONArray
    		int jArrayLength = jArray_DataBarang.length();
			for (int i = 0; i < jArrayLength; i++ ) {
				
				JSONObject jsonObject = jArray_DataBarang.getJSONObject(i);
				
				//Menyimpan data ke Data Structure dataBarang (ke dalam List<Barang>)
				DataBarang_home.add(new Barang(
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

			Log.i(TAG, "JSON Response : " + jsonResponse.toString());
			Log.i(TAG, "Data Barang Size : " + data_barang_size);
			Log.i(TAG, "Data Barang Array : " + jArray_DataBarang.toString());
			
			
		} catch (Exception e) {
			Log.e(TAG, "Gagal : Tidak dapat mengambil data JSON saat parseJSONObject. Message : " + e.getMessage());
		}
	}
	
	
	 public void showAlertDialog(String title, String message) {		
		
		new AlertDialog.Builder(getActivity())
		.setTitle(title)
		.setMessage(message)
		.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//TODO Lakukan pengambilan database offline (SQLite)
			}
		}).show();
		
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
