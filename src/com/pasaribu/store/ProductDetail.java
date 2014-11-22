package com.pasaribu.store;

import java.util.ArrayList;

import com.pasaribu.store.control.AppsController;
import com.pasaribu.store.model_data.Barang;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * ProductDetail.java berguna untuk menampilkan data lengkap (semua informasi).
 * Berdasarkan <strong>id_barang</strong> yang di kirim dari Home.java melalui 
 * metode putExtra.
 * @author Anwar Pasaribu
 *
 */
public class ProductDetail extends Activity {

	private final String TAG = ProductDetail.class.getSimpleName();
	
	private AppsController aController;
	
	private TextView title_add_data_barang;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_detail);
		
		aController = (AppsController) getApplicationContext();
		
		//Ambil ID Barang dari variable Itent
		int id_barang = getIntent().getExtras().getInt(Barang.ID_BARANG);
		title_add_data_barang = (TextView) findViewById(R.id.title_add_data_barang);
		title_add_data_barang.setText("ID Barang Aktif : " + id_barang);
		
	}

	//TODO Fungsi untuk mengisi textView secara otomatis.
	private void getAllTextView() {
		
		LinearLayout linearLayout_product_detail = (LinearLayout) findViewById(R.id.LinearLayout_utama_add_data_barang);
		
		ArrayList<EditText> list_editText = new ArrayList<EditText>();
		
		for(int i = 0; i < linearLayout_product_detail.getChildCount(); i++) {
			
			if(linearLayout_product_detail.getChildAt(i) instanceof EditText) { 
				list_editText.add( (EditText) linearLayout_product_detail.getChildAt(i) );
			}			
			
		}
		
		for(int i = 0 ; i < list_editText.size(); i++ ) {
			list_editText.get(i).setText("");
		}				
		
		
	}
	
	
	
	
	
	//Action Bar -start- 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.product_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	//Action Bar -end- 
}
