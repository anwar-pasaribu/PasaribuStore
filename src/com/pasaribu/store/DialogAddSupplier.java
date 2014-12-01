package com.pasaribu.store;

import java.util.HashMap;
import java.util.Map;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import com.pasaribu.store.model_data.AppsConstanta;

public class DialogAddSupplier extends DialogFragment {
	
	private EditText editText_supplier_name, editText_supplier_address, editText_supplier_contact;
	private DialogAddSupplierListener mListener;
	
	private Map<String, String> data_to_send = new HashMap<String, String>();
	
	
	
	public interface DialogAddSupplierListener {
		 public void onDialogPositiveClick(DialogFragment dialog, Map<String, String> new_supplier_data);
	     public void onDialogNegativeClick(DialogFragment dialog);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		try {
			mListener = (DialogAddSupplierListener) activity;
		} catch (ClassCastException e) {
			
			throw new ClassCastException(activity.toString() + " Harus implementasikan DialogAddSupplier Listener");
			
		}
	}

	
	
	@SuppressLint("InflateParams")
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());		
		
	    View dialogAddSupplier = inflater.inflate(R.layout.add_data_supplier, null, false);	    
		editText_supplier_name = (EditText) dialogAddSupplier.findViewById(R.id.editText_supplier_name);
		editText_supplier_address = (EditText) dialogAddSupplier.findViewById(R.id.editText_supplier_address);
		editText_supplier_contact = (EditText) dialogAddSupplier.findViewById(R.id.editText_supplier_contact);
		

	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    builder.setView( dialogAddSupplier )
	    // Add action buttons
	           .setPositiveButton(R.string.btn_add, new DialogInterface.OnClickListener() {
	        	   
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   
	            	   String nama_toko = editText_supplier_name.getText().toString();
	            	   String alamat_toko = editText_supplier_address.getText().toString();
	            	   String kontak_toko = editText_supplier_contact.getText().toString();
	            	   
	            	   
	            	   data_to_send.put(AppsConstanta.KEY_NAMA_TOKO, nama_toko);
	            	   data_to_send.put(AppsConstanta.KEY_ALAMAT_TOKO, alamat_toko);
	            	   data_to_send.put(AppsConstanta.KEY_KONTAK_TOKO, kontak_toko);
	            	   
	            	   
	            	   //Memberikan fitur fositif klik pada AddBarang Activity sekaligus mengirim data dari form.
	            	   mListener.onDialogPositiveClick(DialogAddSupplier.this, data_to_send);
	            	   
	               }				
	           })
	           .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	            	 //Memberikan fitur negatif klik pada AddBarang Activity
	            	   mListener.onDialogNegativeClick(DialogAddSupplier.this);
	               }
	           });   
	    
	    return builder.create();
		
	}
	
	
	
}
