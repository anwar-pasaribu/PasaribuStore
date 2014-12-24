package com.pasaribu.store.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pasaribu.store.R;
import com.pasaribu.store.R.color;
import com.pasaribu.store.data_model.Supplier;

public class SupplierListAdapter 
	extends ArrayAdapter<Supplier>{
	
	private final String TAG = SupplierListAdapter.class.getSimpleName();
	
	private Context context;
	
	//Array List (Data Supplier)
	private ArrayList<Supplier> data_supplier;
	
	private int listItemLayout;
	

	public SupplierListAdapter(Context context, int listItemLayout, ArrayList<Supplier> data_supplier) {
		super(context, listItemLayout, data_supplier);
		
		this.context = context;
		this.listItemLayout = listItemLayout;
		this.data_supplier = new ArrayList<Supplier>();
		this.data_supplier.addAll(data_supplier);	
	
	}


	@Override
	public void clear() {
		data_supplier.clear();
	}


	@Override
	public void add(Supplier object) {
		
		this.data_supplier.add(object);
		
	}


	@Override
	public Supplier getItem(int position) {
		// TODO Auto-generated method stub
		return super.getItem(position);
	}



	@Override
	public long getItemId(int position) {
		//Mengirim id penjual pada setiap item di list
		return data_supplier.get(position).getId_penjual();
		
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.v(TAG, "Position : " + position);
		
		View itemView = convertView;
		LayoutInflater 	inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewHolder holder = new ViewHolder();
		
		//Data supplier aktif
		Supplier supplierAktif = data_supplier.get(position);
		
		if(itemView == null) {
			itemView = inflater.inflate(this.listItemLayout, parent, false);
		}
		
		holder.text_supplierName = (TextView) itemView.findViewById(R.id.text_supplierName);
		holder.text_shoppingItemNumber = (TextView) itemView.findViewById(R.id.text_shoppingItemNumber);
		
		holder.text_supplierName.setText(supplierAktif.getNama_toko());
		holder.text_shoppingItemNumber.setText("100 Item");
		
		itemView.setTag(holder);
				
		return itemView;
	}
	
	
	
	private static class ViewHolder {
		
		//Widget  utk header
		protected TextView  text_supplierName,
							text_shoppingItemNumber;
		
		
	}	

}
