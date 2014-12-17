package com.pasaribu.store;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.Request.Method;
import com.google.gson.Gson;
import com.pasaribu.store.adapter.DisplayGui;
import com.pasaribu.store.adapter.TabPagerAdapter;
import com.pasaribu.store.control.AppsController;
import com.pasaribu.store.data_model.AppsConstanta;
import com.pasaribu.store.data_model.Barang;
import com.pasaribu.store.data_model.Brand;
import com.pasaribu.store.data_model.Supplier;
import com.pasaribu.store.volley.CustonJsonObjectRequest;

public class Main extends FragmentActivity {
	
	protected static final String TAG = Main.class.getSimpleName();
	private String tag_json_obj = "jobj_data_home_req";	
	private ProgressDialog pDialog;
	
	private ViewPager Tab;
    private TabPagerAdapter TabAdapter;
	private ActionBar actionBar;
	
	private DisplayGui displayGui;
	private AppsController aController;
	private CustonJsonObjectRequest dataBarang_jsonObjReq;
	
	//Keperluan paginating list
	int start_from = 0;
	int limit = 50;
	boolean loadingMore = false ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	Log.i(TAG, "Main Creation Bundle!");
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //Inisialisasi Progress Dialog Box
  		pDialog = new ProgressDialog(this);
  		pDialog.setMessage("Loading...");
  		pDialog.setCancelable(false);
        
        //Application Controller, berisi data ter-share utk semua activity.
      	aController = (AppsController) getApplicationContext();	
        
        //TODO Inisialisasi Kelas yang akan digunakan pada main class        
        displayGui = new DisplayGui(this); //Kelas untuk menampilkan GUI pada aplikasi                
        TabAdapter = new TabPagerAdapter(getSupportFragmentManager());        
        Tab = (ViewPager) findViewById(R.id.main_pager);
        
        //Mengambil data list_home dari MySQL Database (Online)	
  		if(aController.isNetworkAvailable()) {
  			requestAllDataBarangJSONObject();
  			Log.d(TAG, "Main requestAllDataBarangJSONObject called");
  		} else {
  			
  			getCacheData();
  			Log.d(TAG, "Main getCacheData called");
  			
  		}   
        	
    
    }
    //end of onCreate

	/**
	 * 
	 */
	private void populateTabPager() {
		Tab.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                       
                    	actionBar = getActionBar();
                    	actionBar.setSelectedNavigationItem(position);                    
                    }
                });
        
        Tab.setAdapter(TabAdapter);
        
        actionBar = getActionBar();
        
        //Enable Tabs on Action Bar
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        ActionBar.TabListener tabListener = new ActionBar.TabListener(){
        	
        	//tab yang tidak di seleksi lagi, klik tab lain
        	@Override
			public void onTabUnselected(android.app.ActionBar.Tab tab,
					FragmentTransaction ft) {
				// TODO lakukan jika tab unselected
        		
        		Log.i(TAG, tab.getPosition() + " Position (UnSelected)");
				restoreIconAwal(tab);
			}
        	
        	//Klik lagi pada tab yang sama
			@Override
			public void onTabReselected(android.app.ActionBar.Tab tab,
					FragmentTransaction ft) {
				
				Log.i(TAG, tab.getPosition() + " Position (Reselected)");
				
			}

			@Override
			public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
	          
	            Tab.setCurrentItem(tab.getPosition());
	            
	            Log.i(TAG, tab.getPosition() + " Position (Selected)");
	            setTabIconAktif(tab);
	            
	        }

			
			private void setTabIconAktif(android.app.ActionBar.Tab tab) {
				
				int posisi = tab.getPosition();
        		
        		switch (posisi) {
				case 0:
					tab.setIcon(R.drawable.action_button_home_active);
					break;
				case 1:
					tab.setIcon(R.drawable.action_button_belanja_active);
					break;
				case 2:
					tab.setIcon(R.drawable.action_button_favorite_active);
					break;

				default:
					displayGui.displayToast("ERROR, Selected!!!");
					break;
				}
				
			}
			
			private void restoreIconAwal(android.app.ActionBar.Tab tab) {
				
        		int posisi = tab.getPosition();
        		
        		switch (posisi) {
				case 0:
					tab.setIcon(R.drawable.action_button_home_normal);
					break;
				case 1:
					tab.setIcon(R.drawable.action_button_belanja_normal);
					break;
				case 2:
					tab.setIcon(R.drawable.action_button_favorite_normal);
					break;

				default:
					displayGui.displayToast("ERROR, Unselected!!!");
					break;
				}
				
			}

			
			
        };
			
			android.app.ActionBar.Tab menu_home = actionBar.newTab()
					.setIcon(R.drawable.action_button_home_normal)
					.setTabListener(tabListener);
			
			//Add New Tab
			actionBar.addTab(menu_home);
			actionBar.addTab(actionBar.newTab().setIcon(R.drawable.action_button_belanja_normal).setTabListener(tabListener));
			actionBar.addTab(actionBar.newTab().setIcon(R.drawable.action_button_favorite_normal).setTabListener(tabListener));
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
								
								Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

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

	/**
	 * Inti dari pengolahan data. Untuk mengolah data dan disimpan pada Apps Controller
	 * @param responseJsonObject
	 */
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

				} else if(aController.getBarangArrayListSize() != jArray_dataBarang.length()) {
					Log.d(TAG, "Jumlah data on parse: " + data_barang_size_new);

					//JSONObject jsonObject = null;	  	

					for (i = 0; i < jArray_dataBarang.length(); i++) {

						start_from++; //untuk memperbarui index data dimulai dari mana

						//jsonObject = jArray_dataBarang.getJSONObject(i);
						//aController.addToListBarangFull(aController.createBarangFromJSONObject(jsonObject));

						String barangInfo = jArray_dataBarang.getJSONObject(i).toString();
						Barang barang = gson.fromJson(barangInfo, Barang.class); //Dengan gson, mengubah json menjadi data Barang
						
						Log.d(TAG, "Data barang ke- " + i + " :" + barangInfo);
						
						aController.addToListBarang(barang);
						
					}
					
					loadingMore = false;
					

				} else {
					Log.w(TAG, "Tidak ada perubahan data yang dilakukan karena data lokal sama dengan data yang didapatkan dari jaringan.");
				}

				//TODO: Untuk mengambil data dari JSONArray Supplier
				int jArray_SupplierDataLength = jArray_SupplierData.length();
				if (aController.getList_supplier().size() != jArray_SupplierDataLength) {

					aController.clearAllSupplierList();

					for (i = 0; i < jArray_SupplierDataLength; i++) {
						JSONObject jsonObject = jArray_SupplierData.getJSONObject(i);
						
						Log.v(TAG, "Nama toko : " + jsonObject.getString(Supplier.NAMA_TOKO));
						
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
						JSONObject jsonObject = jArray_BrandData.getJSONObject(i);
						
						Log.v(TAG, "Nama brand : " + jsonObject.getString(Brand.NAMA_MEREK));
						
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
    	
    	//Setelah data di ambil semua, kemudian tab dipanggil
    	populateTabPager();
    	
	}
	/////////////////////Request JSON Object - END///////////////////////////////////////////////
    
	public void showAlertDialog(String title, String message) {		
	
		new AlertDialog.Builder(this)
		.setTitle(title)
		.setMessage(message)
		.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				return;
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
    
    
    
    
    
    
    
    
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.v(TAG, "onCreateOptionsMenu Main Called");
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.v(TAG, "onOptionsItemSelected Main Called");
		
		int id = item.getItemId();
		
		switch (id) {
		case R.id.option_add_barang:
			
			//Membuka activity AddDataBarang tanpa data yang dikirimkan
			Intent i = new Intent(getBaseContext(), AddDataBarang.class);
			startActivity(i);
			
			break;
		case R.id.option_setting:
			
			//Membuka activity AddDataBarang tanpa data yang dikirimkan
			Intent intent_setting = new Intent(getBaseContext(), Setting.class);
			startActivity(intent_setting);

			break;
		default:
			break;
		}
		
		
		return super.onOptionsItemSelected(item);
	}
    


    
}
