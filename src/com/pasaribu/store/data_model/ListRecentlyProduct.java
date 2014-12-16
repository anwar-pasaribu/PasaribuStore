package com.pasaribu.store.data_model;

public class ListRecentlyProduct {

	private String gambar_barang;
	private String nama_barang;
	private int stok_barang;
	
	public ListRecentlyProduct(String gambar_barang, String nama_barang,
			int stok_barang) {
		super();
		this.gambar_barang = gambar_barang;
		this.nama_barang = nama_barang;
		this.stok_barang = stok_barang;
	}

	public String getGambar_barang() {
		return gambar_barang;
	}

	public void setGambar_barang(String gambar_barang) {
		this.gambar_barang = gambar_barang;
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
	
	
	
}
