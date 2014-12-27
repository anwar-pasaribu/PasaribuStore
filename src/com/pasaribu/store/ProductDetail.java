package com.pasaribu.store;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.pasaribu.store.control.AppsController;
import com.pasaribu.store.data_model.AppsConstanta;
import com.pasaribu.store.data_model.Barang;
import com.pasaribu.store.volley.CustonJsonObjectRequest;

/**
 * ProductDetail.java berguna untuk menampilkan data lengkap (semua informasi).
 * Berdasarkan <strong>id_barang</strong> yang di kirim dari Home.java melalui 
 * metode putExtra.
 * @author Anwar Pasaribu
 *
 */
public class ProductDetail extends Activity {

	private final String TAG = ProductDetail.class.getSimpleName();
	
	private AppsController aController;
	private Barang active_productData;
	
	//TextView masing-masing data yang akan ditampilkan
	private TextView 	TextView_product_name, TextView_product_brand, TextView_product_price,
						TextView_product_num_unit, TextView_product_category, TextView_product_supplier,
						TextView_product_description;

	private int id_barang;
	private int list_barang_index;

	private String volley_tag = "get_product_detail";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_detail);
		
		aController = (AppsController) getApplicationContext();		
		
		//Inisialisasi semua TextView
		initilizeTextView();
		
		id_barang 			= getIntent().getExtras().getInt(Barang.ID_BARANG);
		list_barang_index 	= getIntent().getExtras().getInt("list_barang_index");	//List ini berfungsi utk set Data List (UPDATE)	
		
		//Isi data barang ke TextView
		populateTextViewData(id_barang, list_barang_index);
		
	}

	//Berfungsi utk mengisi data dari data barang baik di AppsController atau langsung dari cloud
	private void populateTextViewData(int id_barang, int list_barang_index) {
		
		Log.i(TAG, "id_barang: " + id_barang + ", Indeks Barang:" + list_barang_index);
		
		//Periksa apakah ada koneksi jaringan, jika tidak ambil data di aController
		if(aController.isNetworkAvailable()) {
			//Ambil data dari MySQL Database
			Log.i(TAG, "Ambil data dari MySQL Database");
			
			requestJSONObject();
			
		} else {			
			//Ambil data dari Application Controller
			Log.i(TAG, "Ambil data dari Application Controller");
			setTextViewContents();
			
		}
		
		
	}

	/**
	 * Mengisi TextView di Layout yang dibuat.
	 */
	private void setTextViewContents() {
		//Ambil data barang pada pada index tertentu dari Application Controller
		active_productData = aController.getBarangById(id_barang);
		
		String product_brand = aController.getBrandName(active_productData.getId_merek());
		String store_name = aController.getSupplierNameById(active_productData.getId_penjual());
		
		TextView_product_name.setText("ID: " + active_productData.getId_barang() + " - " + active_productData.getNama_barang());
		TextView_product_brand.setText("ID: " + active_productData.getId_merek() + " - " + product_brand );
		TextView_product_price.setText("Rp " + active_productData.getHarga_barang());
		TextView_product_num_unit.setText(active_productData.getStok_barang() + " " + active_productData.getSatuan_barang());
		TextView_product_category.setText(active_productData.getKategori_barang());
		TextView_product_supplier.setText("ID: " + active_productData.getId_penjual() + " - " +store_name);
		TextView_product_description.setText(active_productData.getDeskripsi_barang());
	}

	// Fungsi utk inisialisasi seluruh textView
	private void initilizeTextView() {
		
		TextView_product_name = (TextView) findViewById(R.id.TextView_product_name);
		TextView_product_brand = (TextView) findViewById(R.id.TextView_product_brand);
		TextView_product_price = (TextView) findViewById(R.id.TextView_product_price);
		TextView_product_num_unit = (TextView) findViewById(R.id.TextView_product_num_unit);
		TextView_product_category = (TextView) findViewById(R.id.TextView_product_category);
		TextView_product_supplier = (TextView) findViewById(R.id.TextView_product_supplier);
		TextView_product_description = (TextView) findViewById(R.id.TextView_product_description);
		
	}
	
	private void requestJSONObject() {
		
		//Data yang akan di kirim ke server
		Map<String, String> dataToSend = new HashMap<String,String>();
		dataToSend.put(Barang.ID_BARANG, String.valueOf(id_barang));
		dataToSend.put(Barang.ID_USER, "1"); //TODO Asumsi user aktif adalah 1 = Anwar Pasaribu
		
		Log.i(TAG, "Request JSON Object, dataToSend: " + dataToSend.toString());
		
		CustonJsonObjectRequest jsonObjReq_singleData = new CustonJsonObjectRequest(
				Method.POST,
				AppsConstanta.URL_DATA, 
				dataToSend,
				new Response.Listener<JSONObject>() {
	
					@Override
					public void onResponse(JSONObject response) {
						
						//Set JSONObject to return
						parseJSONObject(response);
						
					}
				}, new Response.ErrorListener() {
	
					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.d(TAG, "Error: " + error.getMessage());
						
					}
				});
		
		//Untuk memastikan cache yang dibuat pada Main.java tidak di-overwrite (27 Des)
		jsonObjReq_singleData.setShouldCache(false);
		
		// Adding request to request queue
		AppsController.getInstance().addToRequestQueue(jsonObjReq_singleData, volley_tag );		
	
		Log.i(TAG, "Done!! Request JSON Object");
	}

	//Parser utk JSONOBject dari MySQL Database
	private void parseJSONObject(JSONObject responseJsonObject) {
		
		if(responseJsonObject != null) { 
		
	    	try {
	    		
	    		JSONObject jsonResponse 		= new JSONObject(responseJsonObject.toString());    		
	    		int data_barang_size_new 		= jsonResponse.getInt(AppsConstanta.JSON_HEADER_DATA_SIZE);
	    		JSONArray jArray_dataBarang 	= jsonResponse.getJSONArray(AppsConstanta.JSON_HEADER_BARANG);    		
	    		int jArray_dataBarang_length 	= jArray_dataBarang.length();
	    		
	    		if(data_barang_size_new != 0 ) {
	    			
					for (int i = 0; i < jArray_dataBarang_length; i++ ) {
						
						JSONObject jsonObject = jArray_dataBarang.getJSONObject(i);
						
						//Mengubah data Barang di aController
						aController.setBarangAtPosition(list_barang_index, 
														aController.createBarangFromJSONObject(jsonObject));					
					}
	    				
				
	    		} else {
	    			
	    			Toast.makeText(getApplicationContext(), "Barang dengan ID : " + id_barang + " tidak tersedia.", Toast.LENGTH_LONG).show();
	    			
	    		}
				
				
			} catch (Exception e) {
				Log.e(TAG, "Tidak dapat mengambil data JSON saat parseJSONObject. Message : " + e.getMessage());
				e.printStackTrace();
			} finally {
				
				setTextViewContents();
				
			}
    	
		} else {
			Toast.makeText(getApplicationContext(), "Tidak ada data dalam JSON Object", Toast.LENGTH_SHORT).show();;
			
		}
    	
    	
	}	
	
	
	
	/////////////////////////////////////////////////////////////////////////
	////////////////////////ACTION BAR - START///////////////////////////////
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.product_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		
		switch (id) {
		case R.id.action_settings:
			
			break;

		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	/////////////////////////////////////////////////////////////////////////
	////////////////////////ACTION BAR - END/////////////////////////////////
}
