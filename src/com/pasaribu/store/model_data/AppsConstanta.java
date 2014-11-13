/**
 * 
 */
package com.pasaribu.store.model_data;

/**
 * Kelas ini berfungsi untuk menyimpan seluruh konstanta yang digunakan pada aplikasi.
 * Sehingga variable pada clas lain dapat di ambil dari kelas ini, secara statis.
 * @author Anwar Pasaribu
 *
 */
public class AppsConstanta {
	
	//Header utk setiap JSON Object, respon dari SERVER MySQL
	public static String JSON_HEADER_BARANG = "BARANG";
	public static String JSON_HEADER_DATA_SIZE = "DATA_SIZE";
	public static String JSON_HEADER_LAST_INSERTED_ID = "LAST_INSERTED_ID";
	
	//

	//Informasi Supplier / Penjual
	public static String 	KEY_NAMA_TOKO = "nama_toko",
			KEY_ALAMAT_TOKO = "alamat_toko",
			KEY_KONTAK_TOKO = "kontak_toko";
	
	public static String LAST_INSERTED_ID = "last_inserted_id";
	
	//Informasi akses database mysql seperti URL_DATA. 
	public static String URL_DATA 	= "http://192.168.56.5/pasaribu_store/function/getDataBarang.php"; 
	public static String URL_SUPPLIER 	= "http://192.168.56.5/pasaribu_store/function/insert_supplier.php"; 
	
	public AppsConstanta() {
		// TODO Auto-generated constructor stub
	}

}
