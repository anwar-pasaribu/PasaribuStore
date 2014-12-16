/**
 * 
 */
package com.pasaribu.store.data_model;

import android.util.Log;

/**
 * Kelas ini berfungsi untuk menyimpan seluruh konstanta yang digunakan pada aplikasi.
 * Sehingga variable pada class lain dapat di ambil dari kelas ini, secara statis.
 * @author Anwar Pasaribu
 *
 */
public class AppsConstanta {
	
	private final String TAG = AppsConstanta.class.getSimpleName();
	
	public static String MANUAL_IP_ADDRESS = "192.168.56.5";
	
	//Header utk setiap JSON Object, respon dari SERVER MySQL
	public static String JSON_HEADER_BARANG = "BARANG";
	public static String JSON_HEADER_SUPPLIER = "SUPPLIER";
	public static String JSON_HEADER_BRAND = "BRAND";
	public static String JSON_HEADER_CATEGORY = "CATEGORY";
	public static String JSON_HEADER_DATA_SIZE = "DATA_SIZE";
	public static String JSON_HEADER_LAST_INSERTED_ID = "LAST_INSERTED_ID";
	public static String JSON_HEADER_QUERY = "QUERY";
	public static String JSON_HEADER_MESSAGE = "msg";
	public static String JSON_HEADER_END_OF_DATA = "END_OF_DATA";
	
	//Isi Pesan dari server
	public static String MESSAGE_SUCCESS = "Berhasil";
	public static String MESSAGE_FAIL = "Gagal";
	

	//Informasi Supplier / Penjual
	public static String 	KEY_NAMA_TOKO = "nama_toko",
			KEY_ALAMAT_TOKO = "alamat_toko",
			KEY_KONTAK_TOKO = "kontak_toko";
	
	public static String LAST_INSERTED_ID = "last_inserted_id";
	
	//Informasi akses database mysql seperti URL_DATA. 
	public static String URL_DATA 				= "http://"+ MANUAL_IP_ADDRESS +"/pasaribu_store/function/get_home_data.php"; 
	public static String URL_INSERT_SUPPLIER 	= "http://"+ MANUAL_IP_ADDRESS +"/pasaribu_store/function/insert_supplier.php"; 
	public static String URL_INSERT_PRODUCT 	= "http://"+ MANUAL_IP_ADDRESS +"/pasaribu_store/function/insert_product.php"; 
	public static String URL_UPDATE_PRODUCT 	= "http://"+ MANUAL_IP_ADDRESS +"/pasaribu_store/function/update_product.php"; 
	public static String URL_DELETE_PRODUCT 	= "http://"+ MANUAL_IP_ADDRESS +"/pasaribu_store/function/delete_product.php"; 
	public static String URL_SUPPLIER_AND_BRAND = "http://"+ MANUAL_IP_ADDRESS +"/pasaribu_store/function/get_supplier_and_brand.php";  //Ambil data supplier
	
	public AppsConstanta() {
		// TODO Auto-generated constructor stub
		Log.i(TAG, "Constructor Apps Constanta Dipanggil");
	}
	
	public void setManualIpAddress(String string_ip_address) {
		MANUAL_IP_ADDRESS = string_ip_address;
	}

}
