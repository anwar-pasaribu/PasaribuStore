package com.pasaribu.store;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.pasaribu.store.control.AppsController;
import com.pasaribu.store.control.CustonJsonObjectRequest;
import com.pasaribu.store.model_data.AppsConstanta;
import com.pasaribu.store.model_data.Barang;
import com.pasaribu.store.model_data.Brand;
import com.pasaribu.store.model_data.Supplier;

public class AddDataBarang extends Activity 
	implements 	OnItemSelectedListener, 
				OnClickListener,
				DialogAddSupplier.DialogAddSupplierListener{
	
	final String TAG = AddDataBarang.class.getSimpleName();
	private String tag_add_data_barang = "proses_data_barang"; //Tag proses Volley, 
	
	private ProgressDialog pDialog;
	private AppsController aController;
	
	private EditText 	editText_product_name,
						editText_product_price,
						editText_product_stock,
						editText_product_description;
	
	private AutoCompleteTextView 	autoComp_product_unit,
									autoComp_product_brand;
	
	private Spinner		spinner_product_category,
						spinner_supplier;
	
	private Button btn_add_supplier;
	
	private List<String> list_data_category = new ArrayList<String>();
	private List<String> list_data_supplier = new ArrayList<String>();
	private List<String> list_data_product_unit = new ArrayList<String>();
	private List<String> list_data_brand = new ArrayList<String>();

	private String supplier;

	private String kategori;
	
	
	//private DialogAddSupplier dialodAddSupplier;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_data_barang);
		
		aController = (AppsController) getApplicationContext();
		
		intializeWidget();
		
		//Progress Dialog Initialization
		//Inisialisasi Progress Dialog Box
		pDialog = new ProgressDialog(AddDataBarang.this);
		pDialog.setMessage("Loading...");
		pDialog.setCancelable(false);
		
		autoComp_product_brand.setThreshold(2);
		autoComp_product_unit.setThreshold(2);
		
		//Memberikan Click Listener pada view
		spinner_product_category.setOnItemSelectedListener(this);
		spinner_supplier.setOnItemSelectedListener(this);
		btn_add_supplier.setOnClickListener(this);
		
		//TODO Membuat list kategori. Berikutnya data diperoleh dari database
		list_data_category.add("Elektronik");
		list_data_category.add("Automotif");
		list_data_category.add("Chainsaw");
		list_data_category.add("Lainnya");
		
		//Mengisi data autoComp dan Spinner
		populateFormData();
		
		list_data_product_unit.add("Kotak");
		list_data_product_unit.add("Lusin");
		list_data_product_unit.add("Unit");
		list_data_product_unit.add("Kaleng");
		list_data_product_unit.add("Botol");
		list_data_product_unit.add("Buah");
		list_data_product_unit.add("Plastik");		
		
		autoComp_product_unit.setAdapter(generateAdapter(list_data_product_unit));
		
		
	}

	/**
	 * Inisialisasi widget.
	 */
	private void intializeWidget() {
		
		editText_product_name = (EditText) findViewById(R.id.editText_product_name);
		autoComp_product_brand = (AutoCompleteTextView) findViewById(R.id.TextView_product_brand);
		editText_product_price = (EditText) findViewById(R.id.editText_product_price);
		editText_product_stock = (EditText) findViewById(R.id.editText_product_stock);
		autoComp_product_unit = (AutoCompleteTextView) findViewById(R.id.autoComp_product_unit);
		editText_product_description = (EditText) findViewById(R.id.editText_product_description);
		
		spinner_product_category = (Spinner) findViewById(R.id.spinner_product_category);
		spinner_supplier = (Spinner) findViewById(R.id.spinner_supplier);
		
		btn_add_supplier = (Button) findViewById(R.id.btn_add_supplier);
	}
	
	private void populateFormData() {
		// Mengambil data Supplier dan data Brand utk dimasukkan ke Spinner dan AutoCompleteTextView		
		
		int[] list_size = {aController.getList_brand().size(), aController.getList_product_category().size(), aController.getList_supplier().size()};
		int i = 0;
		
		// 0. Add data Brand
		for(i = 0; i < list_size[0]; i++) {
			list_data_brand.add(aController.getList_brand().get(i).getNama_merek());
		}
		
		// 1. Add data category
		for(i = 0 ; i < list_size[1]; i++) {
			list_data_category.add(aController.getList_product_category().get(i) );
		}
		
		// 2. Add data supplier
		for(i = 0; i < list_size[2]; i++) {
			list_data_supplier.add(aController.getList_supplier().get(i).getNama_toko());
		}
		
		//Mengisi autoComp_product_brand
		autoComp_product_brand.setAdapter(generateAdapter(list_data_brand));
		
		//Mengisi data spinner
		spinner_product_category.setAdapter(generateAdapter(list_data_category));
		spinner_supplier.setAdapter(generateAdapter(list_data_supplier));
		
		Log.i(TAG, "Memperoleh Spinner data done. Ukuran Data Brand : " + list_size[0] + " dan Supplier : " + list_size[1]);
	}

	private ArrayAdapter<String> generateAdapter (List<String> list_data) {		
		ArrayAdapter<String> adapter = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item, list_data);
		adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);		
		return adapter;		
	}

	private void getFormDataAndSend() {
		// TODO Memperoleh seluruh data dari form		
		
		String product_name 	= editText_product_name.getText().toString();		
		String product_brand 	= autoComp_product_brand.getText().toString(); 
		String product_price 	= editText_product_price.getText().toString();
		String product_stock 	= editText_product_stock.getText().toString();
		String product_unit 	= autoComp_product_unit.getText().toString();
		String product_category = spinner_product_category.getSelectedItem().toString();
		String product_supplier = spinner_supplier.getSelectedItem().toString();
		String product_description = editText_product_description.getText().toString();
		
		//get product_brand id from nama_merek(product_brand), to send to mysql database
		//Jika merek tidak didapatkan, maka String (teks) merek yang akan di kirim
		//Untuk selanjutnya di proses oleh server
		int brand_list_size = aController.getList_brand().size();
		for(int i = 0; i < brand_list_size; i++) {
			if(aController.getList_brand().get(i).getNama_merek().equals(product_brand)) {
				product_brand = String.valueOf(aController.getList_brand().get(i).getId_merek());
			}
		}
		
		//get supplier_id from product_supplier text
		//Jika tidak di dapat maka teks akan di kirim ke server utk di proses
		int supplier_list_size = aController.getList_supplier().size();
		for(int i = 0; i < supplier_list_size; i++) {
			if(aController.getList_supplier().get(i).getNama_toko().equals(product_supplier)) {
				product_supplier = String.valueOf( aController.getList_supplier().get(i).getId_penjual() );
			}
		}
		
		//Membuat string tanggal sekarang
		Calendar now = Calendar.getInstance();
		int month = now.get(Calendar.MONTH) + 1;
		int day = now.get(Calendar.DAY_OF_MONTH);
		int year = now.get(Calendar.YEAR);
		
		String tanggal_mysql = year+"-"+month+"-"+day;
		
		
		Map<String, String> data_barang_to_send = new HashMap<String, String>();
		//Data utk id_barang AUTOINCREMENT di server
		data_barang_to_send.put(Barang.ID_USER, "1"); //TODO Guna - User Aktif diasumsikan!
		data_barang_to_send.put(Barang.ID_MEREK, product_brand);
		data_barang_to_send.put(Barang.ID_PENJUAL, product_supplier);
		data_barang_to_send.put(Barang.ID_GAMBAR, "0"); //TODO Ingat, utk sekarang gambar masih kosong
		data_barang_to_send.put(Barang.NAMA_BARANG, product_name);
		data_barang_to_send.put(Barang.STOK_BARANG, product_stock + "");
		data_barang_to_send.put(Barang.SATUAN_BARANG, product_unit);
		data_barang_to_send.put(Barang.HARGA_BARANG, product_price);
		data_barang_to_send.put(Barang.TGL_HARGA_STOK_BARANG, tanggal_mysql);
		data_barang_to_send.put(Barang.KODE_BARANG, "KODE");
		data_barang_to_send.put(Barang.LOKASI_BARANG, "LOKASI");
		data_barang_to_send.put(Barang.KATEGORI_BARANG, product_category);
		data_barang_to_send.put(Barang.DESKRIPSI_BARANG, product_description);
		data_barang_to_send.put(Barang.FAVORITE, "0"); //Default status favorite utk Barang Adalah 0
		
		Log.i(TAG, "Data yang akan di kirim : " + data_barang_to_send.toString());
		
		//Mengirim data ke server utk di olah
		jsonObjectAccess(AppsConstanta.URL_INSERT_PRODUCT, data_barang_to_send);		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.add_data_barang, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		int id = item.getItemId();
		
		switch (id) {
		case R.id.option_done_adding:
			
			// Aksi menyimpan data ke SQLite, ke MySQL jika online.
			// Pertama periksa dulu formulir
			if( validateAddProductForm() ) {
				
				if(aController.isNetworkAvailable()) {
					
					getFormDataAndSend(); 
					
				} else {
					//TODO Remind - Lakukan penyimpanan ke SQLite. Utk sekarang masih fokus MySQL
					showAlertDialog("Tidak Ada Jaringan", "\nData akan di simpan ke penyimpanan lokal");
				}
				

			} else {
				Toast.makeText(getApplicationContext(), 
						"Form Masih Kosong, Periksa kembali formulir, pastikan sudah di isi.", 
						Toast.LENGTH_SHORT).show();
			}
			
			break;
			
		case R.id.action_settings :
			
			Toast.makeText(getApplicationContext(), 
					" Settings " + AddDataBarang.class.getName(), 
					Toast.LENGTH_SHORT).show();

			break;

		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}

	//Menangani Seleksi pada spinner
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		
		int id_spinner = parent.getId();
	
		switch (id_spinner) {
		case R.id.spinner_product_category: //Menerima aksi dari spinner/dropdown kategori produk
			supplier = parent.getItemAtPosition(position).toString();
			break;
		case R.id.spinner_supplier: //Menerima kasi dari spinner Toko penyedia barang
			kategori = parent.getItemAtPosition(position).toString();
			break;
		default:
			break;
		}
		
		Log.d(TAG, "Spinner Aktif : " + supplier + kategori);
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		
		Log.i(TAG, " Parent : " + parent.toString());		
		
		Toast.makeText(getApplicationContext(), "Nothing Selected", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onClick(View v) {

		int id = v.getId();
		
		switch (id) {
		case R.id.btn_add_supplier:
			
			//TODO Jika mengklik tombol Tambah Penjual, dialog akan dibuka
			final DialogFragment dialogAddSupplier = new DialogAddSupplier();
			dialogAddSupplier.show(getFragmentManager(), "add_supplier");
			
			break;

		default:
			break;
		}
		
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog, Map<String, String> new_supplier_data) {
		// TODO Menangani positive action saat membuka dialog add supplier.
		// Data yg diperoleh adalah 
		
		String str_nama_toko = new_supplier_data.get(AppsConstanta.KEY_NAMA_TOKO).toString();
		
		if(!str_nama_toko.equals("") && !list_data_supplier.contains(str_nama_toko)) {
			
			//TODO Tambah data ke database MySQL
			jsonObjectAccess(AppsConstanta.URL_INSERT_SUPPLIER, new_supplier_data);
			
			//Update tampilan pada Spinner "Penjual"
			list_data_supplier.add(str_nama_toko);			
			spinner_supplier.setAdapter(generateAdapter(list_data_supplier));
			spinner_supplier.setSelection(list_data_supplier.indexOf(str_nama_toko));
			
			
		}
		
		Toast.makeText(AddDataBarang.this, "Nama Toko : " + new_supplier_data, Toast.LENGTH_LONG).show();
		
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		// Menangani negative action saat membuka dialog add supplier
		return;
	}
	
	//TODO Utk akses data di server MySQL
	private void jsonObjectAccess(String URL, Map<String, String> data_to_send) {			
		
		showProgressDialog();		
		
		Log.i(TAG, "JSONObjectAccess, URL : " + URL );
		
		
		CustonJsonObjectRequest addDataBarangjsonObjReq = new CustonJsonObjectRequest(
				Method.POST,
				URL, 
				data_to_send, 
				new Response.Listener<JSONObject>() {
	
					@Override
					public void onResponse(JSONObject response) {
					
					Log.i(TAG, "Response JSONObject : " + response.toString());	
						
					// Olah respon data dari server berdasarkan URL yang dimasukkan	
					// Memeriksa header "msg" dari server, apakah gagal atau berhasil
					if(!response.isNull(AppsConstanta.JSON_HEADER_MESSAGE) ) {

						try {
							
							String message = response.getString(AppsConstanta.JSON_HEADER_MESSAGE);
							
							if(message.equals(AppsConstanta.MESSAGE_SUCCESS)) {
								//Lakukan jika berhasil input data ke MySQL database
								showAlertDialog_formClearing("Data Berhasil Ditambah", "Data berhasil di tambah ke penyimpanan pusat.\nKosongkan formulir?");
								
							} else {
								//Lakukan jika gagal input data ke MySQL database
								showAlertDialog("Tambah Data Gagal", "Proses penambahan data barang gagal. Periksa kembali form, pastikan sudah di isi dengan baik");
							}
							
						} catch (JSONException e) {
							e.printStackTrace();
						}
						
					} else if(!response.isNull(AppsConstanta.JSON_HEADER_SUPPLIER) && !response.isNull(AppsConstanta.JSON_HEADER_BRAND)) {
						//TODO Jika response data berisi header SUPPLIER dan BRAND, maka proses utk dimasukkan ke Spinner
						parseJSONData(response);
					} 
						
						
					hideProgressDialog();
					
					}
				}, new Response.ErrorListener() {	
					@Override
					public void onErrorResponse(VolleyError error) {
						
						VolleyLog.e(TAG, "Error: " + error.toString());
						Log.e(TAG, "Error Listener, getLocalizedMessage : " + error.getLocalizedMessage());
						Log.e(TAG, "Error Listener, getMessage : " + error.getMessage());
						Log.e(TAG, "Error Listener, getStackTrace : " + error.getStackTrace().toString());
						error.printStackTrace();
						
						hideProgressDialog();
						
					}
				}){};
	
		// Adding request to request queue
		AppsController.getInstance().addToRequestQueue(addDataBarangjsonObjReq, tag_add_data_barang);		
		
		} 
	
	
	public void parseJSONData(JSONObject responseJsonObject) {
		
			Log.i(TAG, "Parse JSON Data : " + responseJsonObject.toString());
			//TODO Olah JSONObject data, jika pemanggilan Volley normal
	    	try {
	    		
	    		JSONObject jsonResponse = new JSONObject(responseJsonObject.toString());    		
	    		JSONArray jArray_SupplierData = jsonResponse.getJSONArray(AppsConstanta.JSON_HEADER_SUPPLIER);
	    		JSONArray jArray_BrandData = jsonResponse.getJSONArray(AppsConstanta.JSON_HEADER_BRAND);
	    		
				//TODO: Untuk mengambil data dari JSONArray Supplier
	    		int jArray_SupplierDataLength = jArray_SupplierData.length();
				for (int i = 0; i < jArray_SupplierDataLength; i++ ) {
					
					JSONObject jsonObject = jArray_SupplierData.getJSONObject(i);					
					list_data_supplier.add(jsonObject.getString(Supplier.NAMA_TOKO));
									
				}
				
				//TODO: Untuk mengambil data dari JSONArray Brand
	    		int jArray_BrandDataLength = jArray_BrandData.length();
				for (int i = 0; i < jArray_BrandDataLength; i++ ) {
					
					JSONObject jsonObject = jArray_BrandData.getJSONObject(i);
					list_data_brand.add(jsonObject.getString(Brand.NAMA_MEREK));
									
				}
				
				spinner_supplier.setAdapter(generateAdapter(list_data_supplier));
				autoComp_product_brand.setAdapter(generateAdapter(list_data_brand));
				
				Log.i(TAG, "JSON Response : " + jsonResponse.toString());
				
				
			} catch (Exception e) {
				Log.e(TAG, "Fail : Tidak dapat mengambil data JSON. Message : " + e.getMessage());
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

		
		//Alert jika input data barang tidak berhasil
		private void showAlertDialog(String title, String message) {		
			
	    	new AlertDialog.Builder(AddDataBarang.this)
	    	.setTitle(title)
	    	.setMessage(message)
	    	.setNeutralButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//
				}
				
			}).show();
	    	
	    }
		
		//Alert jika input data barang  berhasil di tambah ke server
		//Tanya pengguna apakah form dibersihkan, atau dibiarkan saja (karena data n+1 mungkin mirip)
		private void showAlertDialog_formClearing(String title, String message) {		
			
	    	new AlertDialog.Builder(AddDataBarang.this)
	    	.setTitle(title)
	    	.setMessage(message)
	    	.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					clearAddProductForm();
				}
			})
			.setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					editText_product_name.setFocusableInTouchMode(true);
					editText_product_name.requestFocus();
					
				}
			})
			.show();
	    	
	    }
		
		
		//TODO Fungsi untuk menghilangkan text pada EditText, AutoCompleteTextView
		private void clearAddProductForm() {
			
			LinearLayout linearLayout_utama_add_data_barang = (LinearLayout) findViewById(R.id.LinearLayout_utama_add_data_barang);
			
			ArrayList<EditText> list_editText = new ArrayList<EditText>();
			ArrayList<AutoCompleteTextView> list_autoCompleteTextView = new ArrayList<AutoCompleteTextView>();
			
			for(int i = 0; i < linearLayout_utama_add_data_barang.getChildCount(); i++) {
				
				if(linearLayout_utama_add_data_barang.getChildAt(i) instanceof EditText) { 
					list_editText.add( (EditText) linearLayout_utama_add_data_barang.getChildAt(i) );
				}
				else if ( linearLayout_utama_add_data_barang.getChildAt(i) instanceof AutoCompleteTextView ) {
					list_autoCompleteTextView.add((AutoCompleteTextView) linearLayout_utama_add_data_barang.getChildAt(i) );
				} else if ( linearLayout_utama_add_data_barang.getChildAt(i) instanceof LinearLayout ) {
					
					LinearLayout myLayout = (LinearLayout) linearLayout_utama_add_data_barang.getChildAt(i);
					
					for(int j = 0; j < myLayout.getChildCount(); j++) {
						
						if(myLayout.getChildAt(j) instanceof EditText) {
							list_editText.add((EditText) myLayout.getChildAt(j) );
						} else if (myLayout.getChildAt(j) instanceof AutoCompleteTextView) {
							list_autoCompleteTextView.add((AutoCompleteTextView) myLayout.getChildAt(j));
						}
						
					}
					
				}
				
			}
			
			for(int i = 0 ; i < list_editText.size(); i++ ) {
				list_editText.get(i).setText("");
			}
			
			for(int i = 0 ; i < list_autoCompleteTextView.size(); i++ ) {
				list_autoCompleteTextView.get(i).setText("");
			}
			
			spinner_product_category.setSelection(0);
			spinner_supplier.setSelection(0);
			
		}
		
		//TODO Fungsi untuk validasi form add barang
		private boolean validateAddProductForm() {
			
			LinearLayout linearLayout_utama_add_data_barang = (LinearLayout) findViewById(R.id.LinearLayout_utama_add_data_barang);
			
			ArrayList<EditText> list_editText = new ArrayList<EditText>();
			ArrayList<AutoCompleteTextView> list_autoCompleteTextView = new ArrayList<AutoCompleteTextView>();
			
			//Mengambil view dari form
			for(int i = 0; i < linearLayout_utama_add_data_barang.getChildCount(); i++) {
				
				if(linearLayout_utama_add_data_barang.getChildAt(i) instanceof EditText) { 
					list_editText.add( (EditText) linearLayout_utama_add_data_barang.getChildAt(i) );
				}
				else if ( linearLayout_utama_add_data_barang.getChildAt(i) instanceof AutoCompleteTextView ) {
					list_autoCompleteTextView.add((AutoCompleteTextView) linearLayout_utama_add_data_barang.getChildAt(i) );
				} else if ( linearLayout_utama_add_data_barang.getChildAt(i) instanceof LinearLayout ) {
					
					LinearLayout myLayout = (LinearLayout) linearLayout_utama_add_data_barang.getChildAt(i);
					
					for(int j = 0; j < myLayout.getChildCount(); j++) {
						
						if(myLayout.getChildAt(j) instanceof EditText) {
							list_editText.add((EditText) myLayout.getChildAt(j) );
						} else if (myLayout.getChildAt(j) instanceof AutoCompleteTextView) {
							list_autoCompleteTextView.add((AutoCompleteTextView) myLayout.getChildAt(j));
						}
						
					}
					
				}
				
			}
			
			
			//Validasi EditText, apakah kosong
			for(int i = 0 ; i < list_editText.size(); i++ ) {
				
				if( list_editText.get(i).getText().toString().matches("") ) {
					
					list_editText.get(i).setFocusableInTouchMode(true);
					list_editText.get(i).requestFocus();
					
					return false;
					
				} 
				
				
			}
			
			//Validasi AutoComplete 
			for(int i = 0 ; i < list_autoCompleteTextView.size(); i++ ) {
				
				if(list_autoCompleteTextView.get(i).getText().toString().matches("") ) {
					
					list_autoCompleteTextView.get(i).setFocusableInTouchMode(true);
					list_autoCompleteTextView.get(i).requestFocus();					
					
					return false;
					
				} 
				
			}
			
			
			return true;
			
		}
	
}
