package com.pasaribu.store.control;

import java.util.Comparator;

import com.pasaribu.store.data_model.Barang;

/**
 * Untuk mengurutkan list Barang berdasarkan Nama Barang (Product Name). <br>
 * Pengurutan secara Ascending A-Z.
 * @author Anwar Pasaribu
 * @version 1.0
 *
 */
public class SortBarangByName implements Comparator<Barang>{

	private String SORT_METHOD;  
	
	/**
	 * Menentukan bagaimana data diurutkan. Hal ini didapat dari constructor 
	 * yang menerima argument tipe sortir data : <br>
	 * 1. Ascending (ASC) : Mengurutkan data A-Z, atau <br>
	 * 2. Descending (DESC) Mengurutkan data Z-A.
	 */
	public SortBarangByName(String sort_method) {
		super();
		SORT_METHOD = sort_method;
	}
	
	@Override
	public int compare(Barang brg1, Barang brg2) {
		String nama_barang1 = brg1.getNama_barang();
		String nama_barang2 = brg2.getNama_barang();
		
		return sortingMethod( nama_barang1.compareToIgnoreCase(nama_barang2) );
	}
	
	
	private int sortingMethod(int value) {
		
		if(SORT_METHOD.matches("DESC")) {
			return -(value);
		} else {
			return value;
		}
		
	}

}
