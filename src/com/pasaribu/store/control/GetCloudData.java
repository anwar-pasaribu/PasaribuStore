package com.pasaribu.store.control;

import java.util.Map;

import org.json.JSONObject;

import android.util.Log;

import com.android.volley.Request.Method;
import com.android.volley.Request.Priority;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.pasaribu.store.volley.CustonJsonObjectRequest;

/**
 * Kelas ini berfungsi untuk mengambil JSONObject dari database MySQL.
 * @author Anwar Pasaribu
 * 
 */
public class GetCloudData {
	
	//Kumpulan tag utk keperluan debugging
	private String TAG = GetCloudData.class.getSimpleName();	
	
	//JSONObject yg akan dikembalikan kepada pemintanya
	private JSONObject jsonObject;
	
	public GetCloudData() {	
		Log.i(TAG, "Get Cloud Data Called");
	}
	
	
	
	/**
	 * Method ini berfungsi utk mengakses server di url yang didapatkannya. Dan membuat
	 * JSONObject tersedia di kelas ini.
	 * @param url : Alamat URL PHP File
	 * @param dataToSend : Data yang akan di kirim ke Server Map<String, String>
	 * @param volley_tag : Tag yg berguna agar Volley bisa membuat queue proses.
	 */
	public void requestJSONObject(String url, Map<String, String> dataToSend, String volley_tag) {		
		Log.i(TAG, "Request JSON Object");
		
		CustonJsonObjectRequest generalJsonObjReq = new CustonJsonObjectRequest(
				Method.POST,
				url, 
				dataToSend,
				new Response.Listener<JSONObject>() {
	
					@Override
					public void onResponse(JSONObject response) {
						
						//Set JSONObject to return
						setJsonObject(response);
						
					}
				}, new Response.ErrorListener() {
	
					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.d(TAG, "Error: " + error.getMessage());
						
					}
				}){};
				
		generalJsonObjReq.setPriority(Priority.HIGH);
				
		// Adding request to request queue
		AppsController.getInstance().addToRequestQueue(generalJsonObjReq, volley_tag);		
		
		} 			
		//getJSONObject -end- //
	
	
	
	//////////////////////////////////////////////////////////////////
	////////////////////Getter and Setter/////////////////////////////
	
	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JSONObject jsonObject) {
		
		this.jsonObject = null; //Utk memastikan data yang dikembalikan adalah data terbaru.
		this.jsonObject = jsonObject;
	}
	

}
