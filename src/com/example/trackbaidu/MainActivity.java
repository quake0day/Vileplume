package com.example.trackbaidu;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.baidu.location.BDLocationListener;

import com.baidu.location.LocationClient;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends ListActivity {
	
	// Server ip
	public String SERVER_IP_ADDR = "202.120.38.222";
	public String APPNAME = "test";
	public String USERNAME = "test";
	
	private GeoDataSource datasource;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// set mysqlite
		datasource = new GeoDataSource(this);
		datasource.open();
		List<GeoData> values = datasource.getAllGeoData();
		
		
		//ArrayAdapter <GeoData> adapter = new ArrayAdapter<GeoData>(this, android.R.layout.simple_list_item_1,values);
		//setListAdapter(adapter);
		Button myButton1 = (Button) findViewById(R.id.button1);
		final Activity main_activity = this;
		myButton1.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v){
				startActivity(new Intent(main_activity, AppPreferences.class));
			}
		});
		

		startService(new Intent(this, GPSService.class));
		


	}
	public void onClick(View view) throws ParseException, InterruptedException{
		//ArrayAdapter<GeoData> adapter = (ArrayAdapter<GeoData>) getListAdapter();
		GeoData gd = null;
		switch(view.getId()){
		case R.id.add:
			ConnectivityManager mag = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = mag.getActiveNetworkInfo();
			if(info == null){
				Toast.makeText(getApplicationContext(), "Network not avaliable", Toast.LENGTH_SHORT).show();
			}
			else{
				//relayPostToServer(datasource.getAllGeoData());
				startService(new Intent(this, PostGeoData.class));
				Toast.makeText(getApplicationContext(), "Finished..", Toast.LENGTH_SHORT).show();
			}
			//adapter.add(gd);
			break;

		}
		//adapter.notifyDataSetChanged();
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	protected void onResume(){
		datasource.open();
		super.onResume();
	}
	
	@Override 
	protected void onDestroy(){
		super.onDestroy();
		if(datasource != null){
			datasource.close();
		}
	}
	
	


}
