package com.pasaribu.store.model_data;

public class Barang {
	
	private int 	id_barang,
					id_merek,
					id_penjual,
					id_gambar;
	private String 	nama_barang;
	private int 	stok_barang;
	private String 	satuan_barang;
	private int 	harga_barang;
	private String 	tgl_harga_stok_barang,
					kode_barang,
					lokasi_barang,
					kategori_barang,
					deskripsi_barang;
	private int 	favorite;
	
	
	//FIELD DATABASE
	public static String ID_BARANG = "id_barang";
	public static String ID_MEREK = "id_merek";
	public static String ID_PENJUAL = "id_penjual";
	public static String ID_GAMBAR = "id_gambar";
	public static String NAMA_BARANG = "nama_barang";
	public static String STOK_BARANG = "stok_barang";
	public static String SATUAN_BARANG = "satuan_barang";
	public static String HARGA_BARANG = "harga_barang";
	public static String TGL_HARGA_STOK_BARANG = "tgl_harga_stok_barang";
	public static String KODE_BARANG = "kode_barang";
	public static String LOKASI_BARANG = "lokasi_barang";
	public static String KATEGORI_BARANG = "kategori_barang";
	public static String DESKRIPSI_BARANG = "deskripsi_barang";
	public static String FAVORITE = "favorite";
	
	
	

	public Barang(int id_barang, int id_merek, int id_penjual, int id_gambar,
			String nama_barang, int stok_barang, String satuan_barang,
			int harga_barang, String tgl_stok_barang, String kode_barang,
			String lokasi_barang, String kategori_barang,
			String deskripsi_barang, int favorite) {
		super();
		this.id_barang = id_barang;
		this.id_merek = id_merek;
		this.id_penjual = id_penjual;
		this.id_gambar = id_gambar;
		this.nama_barang = nama_barang;
		this.stok_barang = stok_barang;
		this.satuan_barang = satuan_barang;
		this.harga_barang = harga_barang;
		this.tgl_harga_stok_barang = tgl_stok_barang;
		this.kode_barang = kode_barang;
		this.lokasi_barang = lokasi_barang;
		this.kategori_barang = kategori_barang;
		this.deskripsi_barang = deskripsi_barang;
		this.favorite = favorite;
	}




	public int getId_barang() {
		return id_barang;
	}




	public void setId_barang(int id_barang) {
		this.id_barang = id_barang;
	}




	public int getId_merek() {
		return id_merek;
	}




	public void setId_merek(int id_merek) {
		this.id_merek = id_merek;
	}




	public int getId_penjual() {
		return id_penjual;
	}




	public void setId_penjual(int id_penjual) {
		this.id_penjual = id_penjual;
	}




	public int getId_gambar() {
		return id_gambar;
	}




	public void setId_gambar(int id_gambar) {
		this.id_gambar = id_gambar;
	}




	public String getNama_barang() {
		return nama_barang;
	}




	public void setNama_barang(String nama_barang) {
		this.nama_barang = nama_barang;
	}




	public int getStok_barang() {
		return stok_barang;
	}




	public void setStok_barang(int stok_barang) {
		this.stok_barang = stok_barang;
	}




	public String getSatuan_barang() {
		return satuan_barang;
	}




	public void setSatuan_barang(String satuan_barang) {
		this.satuan_barang = satuan_barang;
	}




	public int getHarga_barang() {
		return harga_barang;
	}




	public void setHarga_barang(int harga_barang) {
		this.harga_barang = harga_barang;
	}




	public String getTgl_stok_barang() {
		return tgl_harga_stok_barang;
	}




	public void setTgl_stok_barang(String tgl_stok_barang) {
		this.tgl_harga_stok_barang = tgl_stok_barang;
	}




	public String getKode_barang() {
		return kode_barang;
	}




	public void setKode_barang(String kode_barang) {
		this.kode_barang = kode_barang;
	}




	public String getLokasi_barang() {
		return lokasi_barang;
	}




	public void setLokasi_barang(String lokasi_barang) {
		this.lokasi_barang = lokasi_barang;
	}




	public String getKategori_barang() {
		return kategori_barang;
	}




	public void setKategori_barang(String kategori_barang) {
		this.kategori_barang = kategori_barang;
	}




	public String getDeskripsi_barang() {
		return deskripsi_barang;
	}




	public void setDeskripsi_barang(String deskripsi_barang) {
		this.deskripsi_barang = deskripsi_barang;
	}




	public int getFavorite() {
		return favorite;
	}




	public void setFavorite(int favorite) {
		this.favorite = favorite;
	}


}
