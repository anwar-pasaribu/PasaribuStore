package com.pasaribu.store.control;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.pasaribu.store.model_data.Barang;

public class GetCloudData extends AsyncTask<String, Integer, List<Barang> > {

	//Kumpulan tag utk keperluan debugging
	private String TAG_GET = "Get_cloud_data";
	private String TAG_PROCESS_ERROR = "GetCloudData_ERROR";
	private String TAG_PROCESS_RUNNING = "GetCloudData_RUNNING";
	
	private List<Barang> dataBarang = new ArrayList<Barang>();
	
	private String URL_DATA 	= "http://192.168.56.5/pasaribu_store/function/getDataBarang.php"; //Hanya utk kebutuhan belajar
	private String JSON_HEADER 	= "barang"; //jenis kepala json
	
	
	public GetCloudData() {
		Log.i(TAG_GET, "Memulai Constructor GetCloudData ");
		
	}

	@Override
	protected List<Barang> doInBackground(String... params) {
		// TODO Mengambil Data Barang pada Database MySQL
		
		
		String 		result 	= "";			//Menyimpan JSON String data yang diambil dari MySQL Database    	
    	InputStream isr 	= null;
    	    	
    	
    	try {
			HttpClient httpClient 	= new DefaultHttpClient();
			HttpPost httpPost		= new HttpPost(URL_DATA);
			HttpResponse response	= httpClient.execute(httpPost);
			HttpEntity entity		= response.getEntity();
			isr						= entity.getContent();
			
		} catch (Exception e) {
			Log.e(TAG_PROCESS_ERROR, "Fail 1 : Tidak Bisa Proses Http");
			
		}
    	
    	//TODO: Mengubah respon dari proses http menjadi string
    	try {
			BufferedReader reader 	= new BufferedReader(new InputStreamReader(isr, "iso-8859-1"), 8);
			StringBuilder sb		= new StringBuilder();
			String line 			= null;
			
			while ( (line = reader.readLine()) != null ) {
				sb.append(line + "\n");
			}
			
			isr.close();
			
			result = sb.toString();
			
		} catch (Exception e) {
			Log.e(TAG_PROCESS_ERROR, "Fail 2 : Tidak bisa mengubah respon menjadi string");	
		}
    	
    	//TODO: mengambil data dari JSON
    	try {
    		JSONObject jsonResponse = new JSONObject(result);
			//JSONArray jArray 		= new JSONArray(result);
    		JSONArray jArray 		= jsonResponse.getJSONArray(JSON_HEADER);
			
			//TODO: Untuk mengambil data dari JSONArray
			for (int i = 0; i < jArray.length(); i++ ) {
				
				JSONObject jsonObject = jArray.getJSONObject(i);
				
				//Menyimpan data ke Data Structure dataBarang (ke dalam List<Barang>)
				dataBarang.add(new Barang(
						jsonObject.getInt(Barang.ID_BARANG), 
						jsonObject.getInt(Barang.ID_MEREK), 
						jsonObject.getInt(Barang.ID_PENJUAL), 
						jsonObject.getInt(Barang.ID_GAMBAR), 
						jsonObject.getString(Barang.NAMA_BARANG), 
						jsonObject.getInt(Barang.STOK_BARANG), 
						jsonObject.getString(Barang.SATUAN_BARANG), 
						jsonObject.getInt(Barang.HARGA_BARANG), 
						jsonObject.getString(Barang.TGL_HARGA_STOK_BARANG), 
						jsonObject.getString(Barang.KODE_BARANG), 
						jsonObject.getString(Barang.LOKASI_BARANG), 
						jsonObject.getString(Barang.KATEGORI_BARANG), 
						jsonObject.getString(Barang.DESKRIPSI_BARANG), 
						jsonObject.getInt(Barang.FAVORITE) 
						));
				
				publishProgress(i);
								
			}

			
		} catch (Exception e) {
			Log.e(TAG_PROCESS_ERROR, "Fail 3 : Tidak dapat mengambil data JSON");
		}
    	
		return dataBarang;		
		
	}
	
	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		
		Log.i(TAG_PROCESS_RUNNING, "Data : " + values[0]);
		
	}

	@Override
	protected void onPostExecute(List<Barang> result) {
		// TODO Hasil proses background
		this.dataBarang = result;
		super.onPostExecute(result);
	}
	
	
	

}
