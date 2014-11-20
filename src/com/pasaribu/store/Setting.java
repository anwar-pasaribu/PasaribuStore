package com.pasaribu.store;

import com.pasaribu.store.control.AppsController;
import com.pasaribu.store.model_data.AppsConstanta;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

public class Setting extends Activity implements TextWatcher{

	private EditText editText_ipAddress;
	private TextView lbl_ipAddress, textView_url;
	
	AppsController aController;
	AppsConstanta aConstanta;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		aController = (AppsController) getApplicationContext();
		aConstanta = new AppsConstanta();
		
		
		editText_ipAddress = (EditText) findViewById(R.id.editText_ipAdress);
		lbl_ipAddress = (TextView) findViewById(R.id.lbl_ipAddress);
		textView_url = (TextView) findViewById(R.id.textView_tampilanUrl);
		
		editText_ipAddress.addTextChangedListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.setting, menu);
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

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		aConstanta.setManualIpAddress(s.toString());
		
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Mengubah nilai ip address	
		aConstanta.setManualIpAddress(s.toString());
		textView_url.setText(AppsConstanta.URL_DATA);
	}
}
