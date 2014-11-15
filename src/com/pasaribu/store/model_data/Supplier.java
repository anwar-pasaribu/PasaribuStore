package com.pasaribu.store.model_data;

public class Supplier {
	
	private int id_penjual;
	private String nama_penjual;
	private String nama_toko;
	private String alamat_toko;
	private String geolocation;
	private String kontak_toko;
	private String email_toko;
	
	//FIELD DATABASE UTK TABLE SUPPLIER
	public static String ID_PENJUAL = "id_penjual";
	public static String NAMA_PENJUAL = "nama_penjual";
	public static String NAMA_TOKO = "nama_toko";
	public static String ALAMAT_TOKO = "alamat_toko";
	public static String GEOLOCATION = "geolocation";
	public static String KONTAK_TOKO = "kontak_toko";
	public static String EMAIL_TOKO = "email_toko";
	
	
	public Supplier(int id_penjual, String nama_penjual, String nama_toko,
			String alamat_penjual, String geolocation, String kontak_penjual,
			String email_penjual) {
		super();
		this.id_penjual = id_penjual;
		this.nama_penjual = nama_penjual;
		this.nama_toko = nama_toko;
		this.alamat_toko = alamat_penjual;
		this.geolocation = geolocation;
		this.kontak_toko = kontak_penjual;
		this.email_toko = email_penjual;
	}
	
	public int getId_penjual() {
		return id_penjual;
	}
	public void setId_penjual(int id_penjual) {
		this.id_penjual = id_penjual;
	}
	public String getNama_penjual() {
		return nama_penjual;
	}
	public void setNama_penjual(String nama_penjual) {
		this.nama_penjual = nama_penjual;
	}

	public String getNama_toko() {
		return nama_toko;
	}

	public String getAlamat_toko() {
		return alamat_toko;
	}

	public String getGeolocation() {
		return geolocation;
	}

	public String getKontak_toko() {
		return kontak_toko;
	}

	public String getEmail_toko() {
		return email_toko;
	}

	public void setNama_toko(String nama_toko) {
		this.nama_toko = nama_toko;
	}

	public void setAlamat_toko(String alamat_toko) {
		this.alamat_toko = alamat_toko;
	}

	public void setGeolocation(String geolocation) {
		this.geolocation = geolocation;
	}

	public void setKontak_toko(String kontak_toko) {
		this.kontak_toko = kontak_toko;
	}

	public void setEmail_toko(String email_toko) {
		this.email_toko = email_toko;
	}
	
	
	
	
	

}
