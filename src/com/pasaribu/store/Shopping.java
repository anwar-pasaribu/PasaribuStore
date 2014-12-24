package com.pasaribu.store;

import java.util.ArrayList;
import java.util.List;

import com.pasaribu.store.adapter.ListShoppingAdapter;
import com.pasaribu.store.adapter.SupplierListAdapter;
import com.pasaribu.store.control.AppsController;
import com.pasaribu.store.data_model.Barang;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class Shopping extends Fragment {
	private final String TAG = Shopping.class.getSimpleName();
	
	//Widget utk di tampilkan
	private View viewShopping; //view yg akan dikembalikan ke Main.java
	private ListView lv_listSupplier, lv_listShopping;
	private Button btn_addShoppingItem, btn_cartStatus;
	
	//Application Controller, utk memperoleh data bersama
	private AppsController aController;
	
	//Adapter utk listView
	private SupplierListAdapter adapterListSupplier;
	private ListShoppingAdapter adapterListShopping;

	//Data temporer utk list shopping
	private List<Barang> vShoppingData = new ArrayList<Barang>();
	private int selectedSupplierId = 0;
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		Log.i(TAG, "onCreateView");
		
		//Inflate layout utk dikembalikan ke main activity
		viewShopping = inflater.inflate(R.layout.activity_shopping, container, false);
        
		//Application Controller, berisi data ter-share utk semua activity.
		aController = (AppsController) getActivity().getApplicationContext();
		
        //Inisialisasi seluruh widget
		initilizeWidget();
		
		//Mengisi data pada listView Supplier
		//Mengirm data ke adapter list supplier
		adapterListSupplier = new SupplierListAdapter(getActivity(), R.layout.list_item_supplier, aController.getList_supplier());
		populateLvSupplier();
		
		
		//Mengisi data pada listView Shopping
		adapterListShopping = new ListShoppingAdapter(getActivity(), vShoppingData);
		fetchSupplierBasedShoppingItem(getFirstSupplierId());
		populateLvShopping();
		
        
        return viewShopping;
	}
	
	
	private int getFirstSupplierId() {
		
		if(aController.getList_supplier().size() > 0)
			return aController.getList_supplier().get(0).getId_penjual();
		else
			return 0;
		
	}
	
	


	/**
	 * Menampilkan data shopping pada list view
	 */
	private void populateLvShopping() {
		
		Log.v(TAG, "populateLvShopping, ukuran data shopping : " + vShoppingData.size());
		
		if(vShoppingData.size() > 0) {
			lv_listShopping.setAdapter(adapterListShopping);
			lv_listShopping.setTextFilterEnabled(true);
		}
		
		
	}
	


	/**
	 * Membuat/mengisi data vShoppingData dengan data barang
	 * berdasarkan toko yang dipilih (Default list pertama).
	 */
	private void fetchSupplierBasedShoppingItem(int supplierId) {
		
		Log.v(TAG, "fetchSupplierBasedShoppingItem, id : " + supplierId + ", adapter Size : " + vShoppingData.size());		
		Toast.makeText(getActivity(), "Data " + aController.getSupplierNameById(supplierId), Toast.LENGTH_LONG).show();;
		
		int sizeAllBarang = aController.getBarangArrayListSize();
		
		if( selectedSupplierId != supplierId &&  sizeAllBarang > 0) {
			
			adapterListShopping.clear();;
			vShoppingData.clear();
			
			Log.i(TAG, "adapter count : " + adapterListShopping.getCount() );
			Log.i(TAG, "shopping data size : " + vShoppingData.size() );
			
			if(vShoppingData.size() == 0 && adapterListShopping.isEmpty()) {
				
				adapterListShopping.notifyDataSetChanged();
				
				for(int i = 0; i <sizeAllBarang; i++) {
					if(aController.getAllBarangList().get(i).getId_penjual() == supplierId) {					
						Barang temp_barang = aController.getAllBarangList().get(i);
						vShoppingData.add(temp_barang);
						adapterListShopping.add(temp_barang);
					}
				}
				
			} else {
				Log.e(TAG, "Data belum dikosongkan");
			}
			
			adapterListShopping.notifyDataSetChanged();
		}
		
		selectedSupplierId = supplierId;
		
	}
	

	/**
	 * Mengisi data pada ListView Supplier. 
	 */
	private void populateLvSupplier() {
		Log.v(TAG, "populateLvSupplier");
		
		lv_listSupplier.setSelector(R.drawable.list_secondary_background);
		lv_listSupplier.setAdapter(adapterListSupplier);
		lv_listSupplier.setTextFilterEnabled(true);
		lv_listSupplier.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Log.i(TAG, "Position : " + position + " selected, with id penjual: " + id);

				fetchSupplierBasedShoppingItem((int)id);
				
			}
		});
	}

	private void initilizeWidget() {
		Log.v(TAG, "Inisialisasi Widget");
		
		lv_listSupplier = (ListView) viewShopping.findViewById(R.id.list_supplier);
		lv_listShopping = (ListView) viewShopping.findViewById(R.id.list_shopping);
		
		btn_addShoppingItem = (Button) viewShopping.findViewById(R.id.btn_addShoppingItem);
		btn_cartStatus = (Button) viewShopping.findViewById(R.id.btn_cartStatus);
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "onDestroy");
		
	}
	
	
	@Override
	public void onPause() {
		super.onPause();
		Log.i(TAG, "onPause");
	}
	
	
	@Override
	public void onStop() {
		super.onStop();
		Log.i(TAG, "onStop");
	}	
	
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		Log.i(TAG, "onDestroyView");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i(TAG, "onResume");
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		Log.i(TAG, "onSaveInstanceState");
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.i(TAG, "onStart");
	}
}
