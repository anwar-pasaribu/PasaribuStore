package com.pasaribu.store;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.pasaribu.store.control.AppsController;
import com.pasaribu.store.control.CustonJsonObjectRequest;
import com.pasaribu.store.model_data.AppsConstanta;
import com.pasaribu.store.model_data.Barang;

public class EditDataBarang extends Activity 
	implements 	OnItemSelectedListener, 
				OnClickListener,
				DialogAddSupplier.DialogAddSupplierListener{
	
	final String TAG = AddDataBarang.class.getSimpleName();
	private String tag_add_data_barang = "proses_data_barang"; //Tag proses Volley, 
	
	private ProgressDialog pDialog;
	private AppsController aController;
	
	//Form 
	
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
	
	private int id_barang;
	private int list_barang_index;
	private Barang active_productData;
	
	public EditDataBarang() {}

	//private DialogAddSupplier dialodAddSupplier;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_data_barang);
		
		aController = (AppsController) getApplicationContext();
		id_barang 	= getIntent().getExtras().getInt(Barang.ID_BARANG);
		list_barang_index = getIntent().getExtras().getInt("list_barang_index");
		
		active_productData = aController.getBrangById(id_barang);
		
		intializeWidget();
		
		//Progress Dialog Initialization
		//Inisialisasi Progress Dialog Box
		pDialog = new ProgressDialog(EditDataBarang.this);
		pDialog.setMessage("Loading...");
		pDialog.setCancelable(false);
		
		autoComp_product_brand.setThreshold(2);
		autoComp_product_unit.setThreshold(2);
		
		//Data di ambil dari Application Controller
		populateFormData();
		
		//Memberikan Click Listener pada view
		spinner_product_category.setOnItemSelectedListener(this);
		spinner_supplier.setOnItemSelectedListener(this);
		btn_add_supplier.setOnClickListener(this);		
		
		list_data_product_unit.add("Kotak");
		list_data_product_unit.add("Lusin");
		list_data_product_unit.add("Unit");
		list_data_product_unit.add("Kaleng");
		list_data_product_unit.add("Botol");
		list_data_product_unit.add("Buah");
		list_data_product_unit.add("Plastik");	
		
		autoComp_product_unit.setAdapter(generateAdapter(list_data_product_unit));
		
		setFormContents();
		
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
	
	private void setFormContents() {
		TextView edit_data_title = (TextView) findViewById(R.id.title_edit_data_barang);
		edit_data_title.setText("Edit Barang ID : " + id_barang + ", Index List : " + list_barang_index);
		String product_brand = aController.getBrandName(active_productData.getId_merek());
		String store_name = aController.getSupplierNameById(active_productData.getId_penjual());
		
		//Mengambil index product_category dan supplier
		int index_category_spinner = aController.getProductCategoryListIndexByName(active_productData.getKategori_barang());
		int index_supplier_spinner = aController.getSupplierListIndexByName(store_name);
				
		editText_product_name.setText(active_productData.getNama_barang());
		autoComp_product_brand.setText( product_brand );
		editText_product_price.setText(String.valueOf(active_productData.getHarga_barang()));
		editText_product_stock.setText(String.valueOf(active_productData.getStok_barang()));
		autoComp_product_unit.setText(active_productData.getSatuan_barang());
		
		//Spinner di kontrol pada method populateSpinner()
		
		editText_product_description.setText(active_productData.getDeskripsi_barang());
		
		Log.i(TAG, "Index Category : " + index_category_spinner + " Index Supplier : " + index_supplier_spinner);
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
		
		spinner_product_category.setSelection(list_data_category.indexOf(active_productData.getKategori_barang()));
		spinner_supplier.setSelection( list_data_supplier.indexOf(aController.getSupplierNameById(active_productData.getId_penjual())) );
		
		Log.i(TAG, "Memperoleh Spinner data done. Ukuran Data Brand : " + list_size[0] + " dan Supplier : " + list_size[1]);
	}

	
	
	private ArrayAdapter<String> generateAdapter (List<String> list_data) {		
		ArrayAdapter<String> adapter = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item, list_data);
		adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);		
		return adapter;		
	}
	

	
	/**
	 * Untuk mengisi for dengan data barang yang sedang aktif
	 */
	private void getFormData() {
		
		String product_name 	= editText_product_name.getText().toString();		
		String product_brand 	= autoComp_product_brand.getText().toString(); 
		String product_price 	= editText_product_price.getText().toString();
		String product_stock 	= editText_product_stock.getText().toString();
		String product_unit 	= autoComp_product_unit.getText().toString();
		String product_category = spinner_product_category.getSelectedItem().toString();
		String product_supplier = spinner_supplier.getSelectedItem().toString();
		String product_description = editText_product_description.getText().toString();
		
		/* get product_brand id from nama_merek(product_brand), to send to mysql database
		*  Jika merek tidak didapatkan, maka String (teks) merek yang akan di kirim
		*  Untuk selanjutnya di proses oleh server
		*/
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
		final int RADIX = 10;
		
		
		Map<String, String> data_barang_to_edit = new HashMap<String, String>();
		//Data utk id_barang AUTOINCREMENT di server
		
		data_barang_to_edit.put(Barang.ID_USER, "1"); //TODO Guna - User Aktif diasumsikan!
		
		//periksa perubahan data yang dibuat sehingga data yg di kirim hanya yang perubahan
		if(isInteger(product_brand, RADIX)) {
			if(Integer.parseInt(product_brand) != active_productData.getId_merek()) {
				data_barang_to_edit.put(Barang.ID_MEREK, product_brand);
			}
		}
		
		if(isInteger(product_supplier, RADIX)) {
			if(Integer.parseInt(product_supplier) != active_productData.getId_penjual())
				data_barang_to_edit.put(Barang.ID_PENJUAL, product_supplier);
		}
		
		if(active_productData.getId_gambar() != 0) 
			data_barang_to_edit.put(Barang.ID_GAMBAR, "0");
		
		if(!active_productData.getNama_barang().equals(product_name))
			data_barang_to_edit.put(Barang.NAMA_BARANG, product_name);
		
		if(active_productData.getStok_barang() != Integer.parseInt(product_stock))
			data_barang_to_edit.put(Barang.STOK_BARANG, product_stock);
		
		if(!active_productData.getSatuan_barang().equals(product_unit))
			data_barang_to_edit.put(Barang.SATUAN_BARANG, product_unit);
		
		if(active_productData.getHarga_barang() != Integer.parseInt(product_price))
			data_barang_to_edit.put(Barang.HARGA_BARANG, product_price);
		
		if(!active_productData.getTgl_stok_barang().equals(tanggal_mysql))
			data_barang_to_edit.put(Barang.TGL_HARGA_STOK_BARANG, tanggal_mysql);
		
		if(!active_productData.getKode_barang().matches("KODE"))
			data_barang_to_edit.put(Barang.KODE_BARANG, "KODE");
		
		if(!active_productData.getLokasi_barang().matches("LOKASI"))
			data_barang_to_edit.put(Barang.LOKASI_BARANG, "LOKASI");
		
		if(!active_productData.getKategori_barang().equals(product_category))
			data_barang_to_edit.put(Barang.KATEGORI_BARANG, product_category);
		
		if(!active_productData.getDeskripsi_barang().equals(product_description))
			data_barang_to_edit.put(Barang.DESKRIPSI_BARANG, product_description);
		
		
//		data_barang_to_edit.put(Barang.ID_MEREK, product_brand);
//		data_barang_to_edit.put(Barang.ID_PENJUAL, product_supplier);
//		data_barang_to_edit.put(Barang.ID_GAMBAR, "0"); //TODO Ingat, utk sekarang gambar masih kosong
//		data_barang_to_edit.put(Barang.NAMA_BARANG, product_name);
//		data_barang_to_edit.put(Barang.STOK_BARANG, product_stock + "");
//		data_barang_to_edit.put(Barang.SATUAN_BARANG, product_unit);
//		data_barang_to_edit.put(Barang.HARGA_BARANG, product_price);
//		data_barang_to_edit.put(Barang.TGL_HARGA_STOK_BARANG, tanggal_mysql);
//		data_barang_to_edit.put(Barang.KODE_BARANG, "KODE");
//		data_barang_to_edit.put(Barang.LOKASI_BARANG, "LOKASI");
//		data_barang_to_edit.put(Barang.KATEGORI_BARANG, product_category);
//		data_barang_to_edit.put(Barang.DESKRIPSI_BARANG, product_description);
//		data_barang_to_edit.put(Barang.FAVORITE, "0"); //Default status favorite utk Barang Adalah 0
		
		Log.i(TAG, "Data yang akan di kirim : " + data_barang_to_edit.toString());
		
		//Periksa perubahan yg terjadi pada data
		//detectDataChanges(data_barang_to_edit);
		
	}

	private static boolean isInteger(String s, int radix) {
		
		if(s.isEmpty()) return false;
		for(int i = 0; i < s.length(); i++) {
			if(i == 0 && s.charAt(i) == '-' ) {
				if(s.length() == 1) return false;
				else continue;
			}
			if(Character.digit(s.charAt(i), radix) < 0) return false;
		}
		
		return true;
		
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
			if( validateProductForm() ) {
				
				if(aController.isNetworkAvailable()) {
					
					getFormData(); 
					
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
			
			Intent setting_intent = new Intent(this, Setting.class);
			startActivity(setting_intent);

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
		
		Toast.makeText(this, "Nama Toko : " + new_supplier_data, Toast.LENGTH_LONG).show();
		
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
								showAlertDialog("Data Berhasil Diubah", "Data berhasil diubah ke penyimpanan pusat.");
								
							} else {
								//Lakukan jika gagal input data ke MySQL database
								showAlertDialog("Tidak Bisa Mengubah Data", "Proses ubah data barang gagal. Periksa kembali form, pastikan sudah di isi dengan baik");
							}
							
						} catch (JSONException e) {
							e.printStackTrace();
						}
						
					} 
						
						
					hideProgressDialog();
					
					}
				}, new Response.ErrorListener() {	
					@Override
					public void onErrorResponse(VolleyError error) {
						
						VolleyLog.e(TAG, "Error: " + error.toString());
						Log.e(TAG, "Error Listener, getStackTrace : " + error.getStackTrace().toString());
						error.printStackTrace();
						
						hideProgressDialog();
						
					}
				}){};
	
		// Adding request to request queue
		AppsController.getInstance().addToRequestQueue(addDataBarangjsonObjReq, tag_add_data_barang);		
		
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
		
    	new AlertDialog.Builder(EditDataBarang.this)
    	.setTitle(title)
    	.setMessage(message)
    	.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//
			}
			
		}).show();
    	
    }
		
	//Fungsi untuk validasi form add barang
	private boolean validateProductForm() {
		
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
	// ValidateProductForm - END
}