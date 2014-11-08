package com.pasaribu.store.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.pasaribu.store.model_data.Barang;

public class GetCloudData {
	
	//Kumpulan tag utk keperluan debugging
	private String TAG_GET = GetCloudData.class.getSimpleName();	
	
	private List<Barang> dataBarang = new ArrayList<Barang>();
	private JSONObject jsonObject;
	
	private String URL_DATA 	= "http://192.168.56.5/pasaribu_store/function/getDataBarang.php"; //Hanya utk kebutuhan belajar
	private String JSON_HEADER 	= "barang"; //jenis kepala json
	private String tag_json_obj = "jobj_req";	
	
	public GetCloudData() {
		Log.i(TAG_GET, "Memulai Constructor GetCloudData ");
		
	}
	
	//from androidhive.com -start- //
	public void requestAllDataBarangJSONObject() {			
			
		JsonObjectRequest jsonObjReq = new JsonObjectRequest(
				Method.GET,
				URL_DATA, 
				null,
				new Response.Listener<JSONObject>() {
	
					@Override
					public void onResponse(JSONObject response) {
						
						manageJsonObject(response);
						
					}
				}, new Response.ErrorListener() {
	
					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.d(TAG_GET, "Error: " + error.getMessage());
					}
				})
		{
			
			//Passing some request headers				 
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("Content-Type", "application/json");
				return headers;
			}
		};
	
		// Adding request to request queue
		AppsController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);		
		
		} //End of requestAllDataBarangJSONObject()		

	public void parseJSONObject(String jsonString) {
		
		//TODO Olah JSONObject data, respon dari pemanggilan
    	try {
    		JSONObject jsonResponse = new JSONObject(jsonString);
			//JSONArray jArray 		= new JSONArray(result);
    		JSONArray jArray 		= jsonResponse.getJSONArray(JSON_HEADER);		
    		
    		
			//TODO: Untuk mengambil data dari JSONArray
    		int jArrayLength = jArray.length();
			for (int i = 0; i < jArrayLength; i++ ) {
				
				JSONObject jsonObject = jArray.getJSONObject(i);
				
				//Menyimpan data ke Data Structure dataBarang (ke dalam List<Barang>)
				dataBarang.add(new Barang(
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

			Log.i(TAG_GET, "DataJSJONArray : " + jArray.toString());
			
			
		} catch (Exception e) {
			Log.e(TAG_GET, "Fail : Tidak dapat mengambil data JSON");
		}
	}

	public List<Barang> getAllDataBarang() {
		return this.dataBarang;
	}

	public int getAllDataBarangArrayListSize() {
		return this.dataBarang.size();
	}
	
	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public void setDataBarang(Barang barang) {
		this.dataBarang.add(barang);
	}

	public void manageJsonObject(JSONObject jsonObject) {
		
		this.jsonObject = jsonObject;
		parseJSONObject(jsonObject.toString());
		
		Log.i(TAG_GET, "Memanggil setJSONObject Methode : " + getJsonObject().toString() );
		Log.i(TAG_GET, "Data Barang Setelah di Parse : " + getAllDataBarang().size());
	
		for(int i = 0; i < getAllDataBarangArrayListSize(); i++) {
			Log.i(TAG_GET, "Nama Barang : " + getAllDataBarang().get(i).getNama_barang());
			Log.i(TAG_GET, "Harga Barang : " + getAllDataBarang().get(i).getHarga_barang());

		}
	}
		
		 //from androidhive.com -end- //
	

}
