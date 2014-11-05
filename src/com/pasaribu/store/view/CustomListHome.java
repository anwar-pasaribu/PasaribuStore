package com.pasaribu.store.view;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.opengl.Visibility;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pasaribu.store.model_data.Barang;
import com.pasaribu.store.R;

public class CustomListHome extends ArrayAdapter<Barang> {
	
	private Context 		context;
	private List<Barang>	dataBarangHome;
	
	private final static int ROW_LAYOUT = R.layout.list_item_home;
	
	public CustomListHome(Context context, List<Barang> data) {
		super(context, ROW_LAYOUT, data);
		
		this.context = context;
		this.dataBarangHome = data;
		
	}

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

		String text_product_stock_price = "";
		
		View 			itemView = convertView;
		LayoutInflater 	inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewHolder 		holder;
		
		if(itemView == null) {
			
			holder = new ViewHolder();
			itemView = inflater.inflate(ROW_LAYOUT, parent, false);
			
			//holder.layout_item_home = (LinearLayout) itemView.findViewById(R.id.layout_item_home);
		
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
		
		Barang dataAktif = dataBarangHome.get(position);
		
		//Format teks utk text_product_stock_date TextView
		text_product_stock_price = 	dataAktif.getStok_barang() 
									+ " " + dataAktif.getSatuan_barang() 
									+ " - " + dataAktif.getTgl_stok_barang(); //TODO Buat fungsi pengubah tgl MySQL ke format indonesia
				
		holder.text_product_name.setText(dataAktif.getNama_barang());
		holder.text_product_price.setText("Rp " + dataAktif.getHarga_barang());
		holder.text_product_stock_date.setText( text_product_stock_price );
		holder.text_product_category.setText(dataAktif.getKategori_barang());
		
		//Pengaturan utk image_product dan image_status_favorite
		//TODO Gunakan metode async atau libary third party utk loading gambar
		//Utk sementara image_product biarkan gambar asli dulu
		//////////////////////////////////////////////////////////////////////
		
		//Proses utk image_status_favorite
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
	
	
	}
	

