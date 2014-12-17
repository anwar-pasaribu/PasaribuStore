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
import android.widget.Toast;

import com.pasaribu.store.adapter.CustomListHome;
import com.pasaribu.store.adapter.CustomListHome.CustomListHomeListener;
import com.pasaribu.store.control.AppsController;
import com.pasaribu.store.data_model.Barang;

public class Home extends Fragment implements CustomListHomeListener{	
	
	protected static final String TAG = Home.class.getSimpleName();
	
	private ArrayList<Barang> barang_data_full = new ArrayList<Barang>();;
	private ListView list_home, list_recently;	
	private CustomListHome adapterListHome;	
	private AppsController aController;
	
	//Keperluan paginating list
	int start_from = 0;
	int limit = 10;
	boolean loadingMore = false ;
	
	private View viewHome = null;	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i(TAG, "onCreateView Home");
		
		viewHome = inflater.inflate(R.layout.activity_home, container, false);		
		
		//Application Controller, berisi data ter-share utk semua activity.
		aController = (AppsController) getActivity().getApplicationContext();		
		
		//List Home inisialization
		list_home = (ListView) viewHome.findViewById(R.id.list_home);
		barang_data_full.add(new Barang("Data Pertama", 1));
		
		Log.i(TAG, "Barang size : " + aController.getBarangArrayListSize());
				
			
		
		//Inisialisasi List Recently
		list_recently = (ListView) viewHome.findViewById(R.id.list_recently);		
		list_recently.setItemsCanFocus(false);
		list_recently.setFocusable(false);
		list_recently.setFocusableInTouchMode(false);
		list_recently.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String list_content = parent.getAdapter().getItem(position).toString().trim();
				
				Toast.makeText(getActivity(), "List Recently : " + list_content, Toast.LENGTH_SHORT).show();
				if(position == 0 || list_content.matches("Daftar IP")) {
					//Test tambah data list Home
					
					int rand_num = 0 + (int) (Math.random() * ( (100 - 0) + 1) );
					adapterListHome.add(new Barang("Data Added", rand_num));
					adapterListHome.notifyDataSetChanged();
				}
			}
		});
		
		//Sementara Utk list recently
		populateRecentlyList();
		
				
		//Untuk mengisi list_home dengan data
		adapterListHome = new CustomListHome(getActivity(), aController.getAllBarangList());
		populateListDataBarang();
		Log.d(TAG, "populateListDataBarang called");
		
		return viewHome;
	}
	
	
	/**
	 * <strong>Mengisi List Home / List Utama</strong>
	 * <p>Menangani List Utama pada halaman Home. Dalam method sudah terdapat akses data
	 * ke MySQL database melalui GetCloudData.java sesuai URL dan data yang akan di request.</p>
	 */
	private void populateListDataBarang()  {	
		
//		int data_size = aController.getBarangArrayListSize();
		int data_size = barang_data_full.size() == 0 ? 1 : aController.getBarangArrayListSize();
		
		Log.i(TAG, "Data barang size on populate: " + data_size);		
		
		if (data_size > 0) {			
				
			
			adapterListHome.setCallBack(this);
			
			list_home.setAdapter(adapterListHome);
			list_home.setTextFilterEnabled(true); //new entry 
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
						Log.v(TAG, "Start Data : " + start_from + ", Limit : " + limit);
						
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
		}).show();
		
	}

	

	@Override
	public void deleteDataBarangSuccess(boolean status) {
		// TODO Lakukan jika proses hapus data berhasil
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
