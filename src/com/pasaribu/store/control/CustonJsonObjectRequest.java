package com.pasaribu.store.control;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

/**
 * Kelas Modifikasi extended dari Request, utk mendukung mengirim Map<String, String> ke server
 * @author Anwar Pasaribu
 *
 */
public class CustonJsonObjectRequest extends Request<JSONObject> {
	
	private Listener<JSONObject> listener;
	private Map<String, String> params;
	
	private Priority low_priority = Priority.LOW;

	public CustonJsonObjectRequest(
			String url,
			Map<String, String> params,
			Listener<JSONObject> responseListener,
			ErrorListener errorListener) {
		
		super(Method.GET, url, errorListener);
		this.listener = responseListener;
		this.params = params;
	}
	
	public CustonJsonObjectRequest(
			int method,
			String url,
			Map<String, String> params,
			Listener<JSONObject> responseListener,
			ErrorListener errorListener) {
		
		super(method, url, errorListener);
		this.listener = responseListener;
		this.params = params;
	}
	
	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		return params;
	}

	@Override
	protected void deliverResponse(JSONObject response) {
		listener.onResponse(response);
	}

	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
		
		try {
			
			String jsonstring = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			return Response.success(new JSONObject(jsonstring), HttpHeaderParser.parseCacheHeaders(response));
		
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JSONException je) {
			return Response.error(new ParseError(je));
		}
	
	}

	@Override
	public com.android.volley.Request.Priority getPriority() {
		return low_priority;
	}
	
	public void setPriority(Priority priority) {
		this.low_priority = priority;
	}

	

}
