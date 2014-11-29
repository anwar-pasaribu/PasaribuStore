package com.pasaribu.store.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.pasaribu.store.EditDataBarang;
import com.pasaribu.store.R;
import com.pasaribu.store.control.AppsController;
import com.pasaribu.store.control.CustonJsonObjectRequest;
import com.pasaribu.store.model_data.AppsConstanta;
import com.pasaribu.store.model_data.Barang;

public class CustomListHome 
	extends ArrayAdapter<Barang> 
	implements View.OnClickListener {
	
	private final String TAG = CustomListHome.class.getSimpleName();
	
	private Context 		context;
	private List<Barang>	dataBarangHome;
	private String tag_delete_barang = "tag_delete_single_product";	
	private final static int ROW_LAYOUT = R.layout.list_item_home;
	private CustomListHomeListener custListHomeListener;
	
	private int position = 0;
	
	
	public CustomListHome(Context context, List<Barang> data) {
		super(context, ROW_LAYOUT, data);
		this.context = context;
		this.dataBarangHome = data;
		
	}
	
	//////////////////////////////////////////////////////////////////////
	//Membuat Interface Callback utk dapat digunakan oleh
	//Home.java
	public void setCallBack(CustomListHomeListener custListHomeListener) {
		this.custListHomeListener = custListHomeListener;
	}
	
	public interface CustomListHomeListener {
		public void deleteDataBarangSuccess(boolean status);
	}
	///////////////////////////////////////////////////////////////////////
	
	

	@Override
	public int getCount() {
		return dataBarangHome.size();
	}
	
	@Override
    //get the data of an item from a specific position
    //i represents the position of the item in the list
    public Barang getItem(int i) {
        return null;
    }
 
    @Override
    //get the position id of the item from the list
    public long getItemId(int i) {
        return 0;
    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		this.position = position;	
		String text_product_stock_price = "";
		
		View 			itemView = convertView;
		LayoutInflater 	inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewHolder 		holder;
		
		Barang dataAktif = dataBarangHome.get(position);
		
		if(itemView == null) {
			
			holder = new ViewHolder();
			itemView = inflater.inflate(ROW_LAYOUT, parent, false);			
		
			holder.text_product_name = (TextView) itemView.findViewById(R.id.text_product_name); 
			holder.text_product_price = (TextView) itemView.findViewById(R.id.text_product_price);
			holder.text_product_stock_date = (TextView) itemView.findViewById(R.id.text_product_stock_date);
			holder.text_product_category = (TextView) itemView.findViewById(R.id.text_product_category);
			
			holder.image_product = (ImageView)itemView.findViewById(R.id.image_product);
			holder.image_status_favorite = (ImageView)itemView.findViewById(R.id.image_status_favorite);
			
			holder.btn_more_option = (ImageButton) itemView.findViewById(R.id.btn_more_option); //Over flow button
			
			
			itemView.setTag(holder);
			
		} else {
			holder = (ViewHolder) itemView.getTag();
		}
		
		
		
		//Format teks utk text_product_stock_date TextView
		text_product_stock_price = 	dataAktif.getStok_barang() 
									+ " " + dataAktif.getSatuan_barang() 
									+ " - " + dataAktif.getTgl_stok_barang(); //TODO Buat fungsi pengubah tgl MySQL ke format indonesia
				
		holder.text_product_name.setText(dataAktif.getNama_barang());
		holder.text_product_price.setText("Rp " + dataAktif.getHarga_barang());
		holder.text_product_stock_date.setText( text_product_stock_price );
		holder.text_product_category.setText(dataAktif.getKategori_barang());
		
		//Membuat tag imageButton berisi informasi data barang
		holder.btn_more_option.setTag(dataAktif);
		
		//Menghilangkan fokus saat list di load
		holder.btn_more_option.setFocusable(false);
		holder.btn_more_option.setFocusableInTouchMode(false);
		//Memberikan click listener pada tombol
		holder.btn_more_option.setOnClickListener(this);
		
		//Pengaturan utk image_product dan image_status_favorite
		//TODO Gunakan metode async atau libary third party utk loading gambar
		//Utk sementara image_product biarkan gambar asli dulu
		//////////////////////////////////////////////////////////////////////
		
		//Proses utk image_status_favorite, ditampilkan atau tidak
		if(dataAktif.getFavorite() == 0) {
			holder.image_status_favorite.setVisibility(View.INVISIBLE);
		} else {
			holder.image_status_favorite.setVisibility(View.VISIBLE);
		}
		
		
		return itemView;
	}
	
	//Statik class utk mempercepat performa
	//apabila list sudah besar
	private static class ViewHolder {
		
		//Layout View Item
		//protected LinearLayout layout_item_home;
		
		protected TextView 	text_product_name,
							text_product_price,
							text_product_stock_date,
							text_product_category;
		protected ImageView image_product,
							image_status_favorite;
		protected ImageButton btn_more_option;
		
	}

	@Override
	public void onClick(final View v) {
		// Menangani click listener pada tombol overflow
		
		switch (v.getId()) {
		case R.id.btn_more_option:
			
			PopupMenu popup_overflow = new PopupMenu(getContext(), v);
			popup_overflow.getMenuInflater().inflate(R.menu.overflow_home_list, popup_overflow.getMenu());
			popup_overflow.show();
			popup_overflow.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
				
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					
					switch (item.getItemId()) {
					case R.id.option_overflow_edit:
						//TODO Remind : Lakukan jika overflow item edit di pilih
						Barang dataToEdit = (Barang) v.getTag();
						int id_barang = dataToEdit.getId_barang();
						
						Intent intentEditDataBarang = new Intent(context, EditDataBarang.class);
						intentEditDataBarang.putExtra(Barang.ID_BARANG, id_barang);
						intentEditDataBarang.putExtra("list_barang_index", position);
						context.startActivity(intentEditDataBarang);
						
						break;
						
					case R.id.option_overflow_delete:
						//TODO Remind : Hapus data ini dari list, dan dari kedua database (MySQL, SQLite)
						Barang dataToDelete = (Barang) v.getTag();
						showAlertDialogDeleteBarang(
								"Hapus Data", 
								"Data \"" + dataToDelete.getNama_barang() + "\" akan di hapus ?", 
								dataToDelete.getId_barang() 
								);
						break;
						
					default:
						break;
					}
					//Item overflow switch -end-
					
					return true;
				}
			});
			
			break;

		default:
			break;
		}
		//View onClick switch -end-
		
	}
	
	private void showAlertDialogDeleteBarang(String title, String message, final int id_barang) {		
		
		new AlertDialog.Builder(getContext())
		.setTitle(title)
		.setMessage(message)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//Hapus data dari MySQL database sesuai dengan id_barang request
				Map<String, String> id_dataToDelete = new HashMap<String, String>();
				id_dataToDelete.put("id_barang", id_barang + ""); 
				requestDeleteDataBarang(id_dataToDelete);
				
			}
		})
		.setNegativeButton("BATAL", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				return;
				
			}
		})
		.show();
		
	}
	
	private void requestDeleteDataBarang(final Map<String, String> data_request) {			
		
		CustonJsonObjectRequest reqDeleteDataBarang = new CustonJsonObjectRequest(
				Method.POST,
				AppsConstanta.URL_DELETE_PRODUCT, 
				data_request, 
				new Response.Listener<JSONObject>() {
	
					@Override
					public void onResponse(JSONObject response) {						
						
						if( response.isNull(AppsConstanta.JSON_HEADER_MESSAGE) ) {
							//Jika gagal delete data dari MySQL Database
							custListHomeListener.deleteDataBarangSuccess(false);
							showAlertDialogDeleteBarang(
									"Gagal Hapus Data", 
									"\nTidak bisa menghapus data. Ulangi hapus data ? ", 
									Integer.getInteger(data_request.get("id_barang")) 
									);
						} else {						
							//Jika berhasil hapus data, Callback ditangani oleh Home.java
							custListHomeListener.deleteDataBarangSuccess(true);
						}
						
					}

					
				}, new Response.ErrorListener() {	
					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.d(TAG, "Error: " + error.toString());
						Log.e(TAG, tag_delete_barang + " - Error Response : " + error.toString());
						
					}
				}){};
	
		// Adding request to request queue
		AppsController.getInstance().addToRequestQueue(reqDeleteDataBarang, tag_delete_barang );
		
		
		}
	
	
}
	

