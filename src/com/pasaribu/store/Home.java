package com.pasaribu.store;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pasaribu.store.control.AppsController;
import com.pasaribu.store.control.GetCloudData;
import com.pasaribu.store.model_data.Barang;
import com.pasaribu.store.model_data.ListRecentlyProduct;
import com.pasaribu.store.view.CustomListHome;

public class Home extends Fragment {
	
	private List<Barang> DataBarang_home = new ArrayList<Barang>();
	private List<ListRecentlyProduct> DataBarang_recently = new ArrayList<ListRecentlyProduct>();
	
	private ListView list_home, list_recently;
	
	private GetCloudData getCloudData;
	private CustomListHome customListHome;
	
	//Application Controller utk menyimpan data
	private AppsController aController;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View android = inflater.inflate(R.layout.activity_home, container, false);
		
		aController = (AppsController) getActivity().getApplicationContext();
		
		getCloudData = new GetCloudData(); //Kelas utk memperoleh data utk ditampilkan di Home
		
		list_home = (ListView) android.findViewById(R.id.list_home);
		list_recently = (ListView) android.findViewById(R.id.list_recently);
		
		populateListDataBarang();
		
		return android;
	}
	
	public void populateListDataBarang() {
		
		//Menjalankan proses pengambilan data
		//Hanya bisa dipanggil sekali (ERROR jika tidak)
        getCloudData.execute(); 
        
        //Memperoleh data dari proses asynctask
	    try {
	    	//TODO Menyalin data "barang" full ke variable aController agar bisa di akses pada activity yang lain
			this.DataBarang_home = getCloudData.get(); //Mengambil data hasil computasi asynctask			
			int data_size = DataBarang_home.size();
			for(int i = 0; i < data_size; i++) {
				aController.setBarang(DataBarang_home.get(i));
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} finally {
			Toast.makeText(
					getActivity(), 
					"Ukuran Data Barang : " + aController.getBarangArrayListSize(), 
					Toast.LENGTH_LONG
					).show();
		}
	    
	    
	    customListHome = new CustomListHome(getActivity(), aController.getAllBarangList());
	    list_home.setAdapter(customListHome);
	    
	}
	
}
