package com.pasaribu.store.data_model;

public class Brand {
	
	private int id_merek;
	private String nama_merek;
	private String logo_merek;
	private String deskripsi_merek;
	
	public static String ID_MEREK = "id_merek";
	public static String NAMA_MEREK = "nama_merek";
	public static String LOGO_MEREK = "logo_merek";
	public static String DESKRIPSI_MEREK = "deskripsi_merek";	
	
	public Brand(int id_merek, String nama_merek, String logo_merek,
			String deskripsi_merek) {
		super();
		this.id_merek = id_merek;
		this.nama_merek = nama_merek;
		this.logo_merek = logo_merek;
		this.deskripsi_merek = deskripsi_merek;
	}
	
	public int getId_merek() {
		return id_merek;
	}
	public String getNama_merek() {
		return nama_merek;
	}
	public String getLogo_merek() {
		return logo_merek;
	}
	public String getDeskripsi_merek() {
		return deskripsi_merek;
	}
	public void setId_merek(int id_merek) {
		this.id_merek = id_merek;
	}
	public void setNama_merek(String nama_merek) {
		this.nama_merek = nama_merek;
	}
	public void setLogo_merek(String logo_merek) {
		this.logo_merek = logo_merek;
	}
	public void setDeskripsi_merek(String deskripsi_merek) {
		this.deskripsi_merek = deskripsi_merek;
	}
	
	

}
