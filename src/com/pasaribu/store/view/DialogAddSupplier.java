package com.pasaribu.store.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import com.pasaribu.store.R;

public class DialogAddSupplier extends DialogFragment {
	
	public interface DialogAddSupplierListener {
		 public void onDialogPositiveClick(DialogFragment dialog);
	     public void onDialogNegativeClick(DialogFragment dialog);
	}

	
	DialogAddSupplierListener mListener;
	
	

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		try {
			mListener = (DialogAddSupplierListener) activity;
		} catch (ClassCastException e) {
			
			throw new ClassCastException(activity.toString() + " Harus implementasikan DialogAddSupplier Listener");
			
		}
	}



	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();

	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    builder.setView(inflater.inflate(R.layout.add_data_supplier, null))
	    // Add action buttons
	           .setPositiveButton(R.string.btn_add, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   //Memberikan fitur fositif klik pada AddBarang Activity
	            	   mListener.onDialogPositiveClick(DialogAddSupplier.this);
	            	   
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
