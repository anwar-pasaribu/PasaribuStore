package com.pasaribu.store.data_model;

public class ListHomeProduct {
	
	private int id_barang;
	private String gambar_barang;
	private int favorite;
	private String nama_barang;
	private int stok_barang;
	private String tgl_harga_stok_barang;
	private String kategori_barang;
	
	public ListHomeProduct(int id_barang, String gambar_barang, int favorite,
			String nama_barang, int stok_barang, String tgl_harga_stok_barang,
			String kategori_barang) {
		super();
		this.id_barang = id_barang;
		this.gambar_barang = gambar_barang;
		this.favorite = favorite;
		this.nama_barang = nama_barang;
		this.stok_barang = stok_barang;
		this.tgl_harga_stok_barang = tgl_harga_stok_barang;
		this.kategori_barang = kategori_barang;
	}

	public int getId_barang() {
		return id_barang;
	}

	public void setId_barang(int id_barang) {
		this.id_barang = id_barang;
	}

	public String getGambar_barang() {
		return gambar_barang;
	}

	public void setGambar_barang(String gambar_barang) {
		this.gambar_barang = gambar_barang;
	}

	public int getFavorite() {
		return favorite;
	}

	public void setFavorite(int favorite) {
		this.favorite = favorite;
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

	public String getTgl_harga_stok_barang() {
		return tgl_harga_stok_barang;
	}

	public void setTgl_harga_stok_barang(String tgl_harga_stok_barang) {
		this.tgl_harga_stok_barang = tgl_harga_stok_barang;
	}

	public String getKategori_barang() {
		return kategori_barang;
	}

	public void setKategori_barang(String kategori_barang) {
		this.kategori_barang = kategori_barang;
	}
	
	

}
