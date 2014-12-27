package com.pasaribu.store;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.pasaribu.store.adapter.ListHomeAdapter;
import com.pasaribu.store.adapter.ListHomeAdapter.ListHomeAdapterListener;
import com.pasaribu.store.control.AppsController;
import com.pasaribu.store.data_model.Barang;

public class Home extends Fragment implements ListHomeAdapterListener{	
	
	protected static final String TAG = Home.class.getSimpleName();
	
	private AppsController aController;
	private ListHomeAdapter adapterListHome;	

	//Used Widget 
	private ListView list_home, list_recently;	
	private ProgressBar progressBar_listRecently, progressBar_listHome;
	
	//Keperluan paginating list
	int start_from = 0;
	int limit = 10;
	boolean loadingMore = false ;
	
	private View viewHome = null;

	//Menyimpan posisi listView
	private int lastViewedPosition;
	private int topOffset;
	private final String KEY_LASTVIEWEDPOS = "lastViewedPosition";
	private final String KEY_TOPOFFSET = "topOffset";
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {		
		Log.i(TAG, "onCreateView Home!");
		
		viewHome = inflater.inflate(R.layout.activity_home, container, false);		
		
		//Application Controller, berisi data ter-share utk semua activity.
		aController = (AppsController) getActivity().getApplicationContext();
		
		//Adapter utk list home
		adapterListHome = new ListHomeAdapter(getActivity(), aController.getAllBarangList());
		
		//Initilize widget
		initializeWidget();
		
		//Sementara Utk list recently
		populateRecentlyList();
		
		//Untuk mengisi list_home dengan data
		populateListDataBarang();
		
		//TODO Sementara utk menghilangkan progressBar
		progressBar_listHome.setVisibility(View.GONE);
		progressBar_listRecently.setVisibility(View.GONE);
				
		return viewHome;
	}




	/**
	 * Untuk mengaktifkan fitur klik pada item di list recently
	 * 
	 */
	private void listRecentlyClick() {
		
		list_recently.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String list_content = parent.getAdapter().getItem(position).toString().trim();
				
				Toast.makeText(getActivity(), "List Recently : " + list_content, Toast.LENGTH_SHORT).show();
				if(position == 0 || list_content.matches("Daftar IP")) {
					//Test tambah data list Home
					
					int rand_num = 0 + (int) (Math.random() * ( (100 - 0) + 1) ); //Men-generate random number 1-100
					adapterListHome.add(new Barang("Data Added", rand_num));
					adapterListHome.notifyDataSetChanged();
				}
			}
		});
	}
	
	
	
	
	/**
	 * Inisialisasi seluruh widget pada Home.java
	 */
	private void initializeWidget() {

		list_home = (ListView) viewHome.findViewById(R.id.list_home);
		list_recently = (ListView) viewHome.findViewById(R.id.list_recently);
		
		progressBar_listRecently = (ProgressBar) viewHome.findViewById(R.id.progressBar_listRecently);
		progressBar_listHome = (ProgressBar) viewHome.findViewById(R.id.progressBar_listHome);
	}




	@Override
	public void onDestroy() {
		super.onDestroy();
		
		Log.d(TAG, "onDestroy called");
	}




	@Override
	public void onPause() {
		super.onPause();
		Log.v(TAG, "onPause called");
		
		lastViewedPosition = list_home.getFirstVisiblePosition();
		View v = list_home.getChildAt(0);
		topOffset = (v==null) ? 0 : v.getTop();
		
		aController.listHome_lastViewedPosition = lastViewedPosition;
		aController.listHome_topOffset = topOffset;
		
		Log.i(TAG, "onPause - lastViewedPosition:" + lastViewedPosition + ", topOffset:" + topOffset);
		
	}




	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG, "onResume called");
		
		list_home.setSelectionFromTop(lastViewedPosition, topOffset);
		
		Log.i(TAG, "onResume - lastViewedPosition:" + lastViewedPosition + ", topOffset:" + topOffset);
		
	}




	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		
		lastViewedPosition = list_home.getFirstVisiblePosition();
		View v = list_home.getChildAt(0);
		topOffset = (v==null) ? 0 : v.getTop();
		
		savedInstanceState.putInt(KEY_LASTVIEWEDPOS, lastViewedPosition);
		savedInstanceState.putInt(KEY_TOPOFFSET, topOffset);
		
		Log.i(TAG, "savedInstanceState - lastViewedPosition:" + lastViewedPosition + ", topOffset:" + topOffset);
		super.onSaveInstanceState(savedInstanceState);
	}




	@Override
	public void onStart() {
		super.onStart();
		Log.v(TAG, "onStart called");
	}




	/**
	 * <strong>Mengisi List Home / List Utama</strong>
	 * <p>Menangani List Utama pada halaman Home. Dalam method sudah terdapat akses data
	 * ke MySQL database melalui GetCloudData.java sesuai URL dan data yang akan di request.</p>
	 */
	private void populateListDataBarang()  {	
		
//		int data_size = aController.getBarangArrayListSize();
		int data_size = aController.getBarangArrayListSize();
		
		Log.i(TAG, "Data barang size on populate: " + data_size);		
		
		if (data_size > 0) {			
				
			
			adapterListHome.setCallBack(this);
			
			list_home.setAdapter(adapterListHome);
			list_home.setTextFilterEnabled(true); //new entry 
			list_home.setSelectionFromTop(aController.listHome_lastViewedPosition, aController.listHome_topOffset); //on Christmas Day!
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
						
						Log.v(TAG, "firstVisibleItem : " + firstVisibleItem + ", visibleItemCount : " + visibleItemCount);
						//Aksi ambil database jika ada internet
						if(aController.isNetworkAvailable())
							Log.i(TAG, "Ambil data selanjutnya, karena scroll sudah di akhir list");
							//requestAllDataBarangJSONObject();							
							
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
	

	private void populateRecentlyList() {		
		//Inisialisasi fokus				
		list_recently.setItemsCanFocus(false);
		list_recently.setFocusable(false);
		list_recently.setFocusableInTouchMode(false);
		listRecentlyClick();
				
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
		})
		.show();
		
	}

	

	@Override
	public void deleteDataBarangSuccess(boolean status) {
		// Lakukan jika proses hapus data berhasil
		if(status) {
			//requestAllDataBarangJSONObject();
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

	
}
