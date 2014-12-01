package com.pasaribu.store.control;

import java.util.Calendar;

public class Helper {

	public Helper() {
	}
	
	public static String getMySQLDateFormat() {
		
		//Membuat string tanggal sekarang
		Calendar now = Calendar.getInstance();
		int month = now.get(Calendar.MONTH) + 1;
		int day = now.get(Calendar.DAY_OF_MONTH);
		int year = now.get(Calendar.YEAR);
		
		return year+"-"+month+"-"+day;
		
	}
	
	public static String getIndonesianDate(String mysql_date_format) {
		
		Calendar now = Calendar.getInstance();
		int nowYear = now.get(Calendar.YEAR);
		
		String[] date_string = mysql_date_format.split("-");
		
		String tahun = date_string[0];
		String bulan = getMonthName(date_string[1]);
		String tgl	 = date_string[2];
		
		if(Integer.parseInt(tahun) == nowYear)
			tahun = "";
		
		return tgl + " " + bulan + " " + tahun;
	}

	private static String getMonthName(String substring) {
		
		switch (Integer.parseInt(substring)) {
		case 1:
			return "Jan";
		case 2:
			return "Feb";
		case 3:
			return "Mar";
		case 4:
			return "Apr";
		case 5:
			return "Mei";
		case 6:
			return "Jun";
		case 7:
			return "Jul";
		case 8:
			return "Agust";
		case 9:
			return "Sept";
		case 10:
			return "Okt";
		case 11:
			return "Nov";
		case 12:
			return "Des";
		default:
			break;
		}
		
		return null;
	}

}
