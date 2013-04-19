package com.example.trackbaidu;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.database.CursorJoiner.Result;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class AsyncHttpPost extends AsyncTask<String, String, String> {
	private HashMap<String,String> mData = null; // post data
	private GeoData geoData = null;
	private GeoDataSource datasource;
	
	
	public AsyncHttpPost(HashMap<String, String> data,GeoData geoData){
		this.mData = data;
		this.geoData = geoData;
	}

	@Override
	protected String doInBackground(String... params) {
    	// create new http post
		byte[] result = null;
		String str = "";
		
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(params[0]);
		
		try{
			ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
			Iterator<String> it = mData.keySet().iterator();
			while(it.hasNext()){
				String key = it.next();
				nameValuePair.add(new BasicNameValuePair(key,mData.get(key)));
			}
			
			post.setEntity(new UrlEncodedFormEntity(nameValuePair,"UTF-8"));
			HttpResponse httpResponse = client.execute(post);
			StatusLine statusLine = httpResponse.getStatusLine();
			
			if(statusLine.getStatusCode() == HttpURLConnection.HTTP_OK){
				result = EntityUtils.toByteArray(httpResponse.getEntity());
				str = new String(result,"UTF-8");
				Log.d("PostRES",str);
				datasource = new GeoDataSource(SharedValue.getInstance());
				datasource.open();
				datasource.updateGeoData(geoData);
				
			}
		}
		catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
		catch(Exception e){
			e.printStackTrace();
		}

		return str;
	}
	private Toast toast = null;
    
    private void showTextToast(String msg) {
        if (toast == null) {
            toast = Toast.makeText(SharedValue.getInstance(), msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }
	@Override
	protected void onPostExecute(String result){
		if(result.contains("success")){
			showTextToast("Posting....");
		}
		else{
			toast.cancel();
		}
	}

}
