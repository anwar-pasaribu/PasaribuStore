package com.pasaribu.store.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

public class DisplayGui {
	
	private String TAG = "DisplayGUI";
		
	private Context mainContext;
	
	private ListHomeAdapter customListHome;
	
	public DisplayGui() {}

	public DisplayGui(Context ctx) {
		
		this.setMainContext(ctx);
		
		
		Log.i(TAG, "Constructor Dikerjakan");
	}

	/* MENAMPILKAN TOAST
	*  Menampilkan Toast Durasi pendek pada context main
	*/
	
	public void displayToast(String data) {
    	Toast.makeText(getMainContext(), data, Toast.LENGTH_SHORT).show();
    }
	
	/**
	 * <strong>MENAMPILKAN DIALOG ALERT</strong>
	 * <p>Alert Dialog dengan tombol OK, biasa digunakan untuk pesan penting utk user.
	 * @param title Judul pesan yang akan ditampilkan
	 * @param message Isi pesan dalam badan dialog box
	 */
	
	public void showAlertDialog(String title, String message) {		
		
    	new AlertDialog.Builder(getMainContext())
    	.setTitle(title)
    	.setMessage(message)
    	.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				displayToast("OK Setting Selesai");
			}
		}).show();
    	
		Log.i(TAG, "Pemanggilan showAlertDialog\nTitle : " + title + " \nMessage : " + message);
    }

	
	/////////////////////////////////////////////////////////////////////////////
	//////SHOW LIST HOME////////
	public void showListHome() {
		
		
		
	}
	
	////////////////////////////////////////////////////////////////////////////
	// BAGIAN SETTER dan GETTER ///////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	
	
	
	public Context getMainContext() {
		return mainContext;
	}

	public void setMainContext(Context mainContext) {
		this.mainContext = mainContext;
	}
	
}
