package com.example.helloandroid;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

public class HelloAndroid extends Activity {
	private static final String TAG = HelloAndroid.class.getSimpleName();
	private static final String AUTHCODE = "cos333";

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    // called when login button is clicked
    public void doLogin(View v) {
    	final String loginurl = "http://webscript.princeton.edu/~pcao/cos333/dologin.php";
    	
    	final EditText netidEdit = (EditText) findViewById(R.id.netidEntry);
        final EditText passwordEdit = (EditText) findViewById(R.id.passwordEntry);
        String netidIn = netidEdit.getText().toString();
        String pwordIn = passwordEdit.getText().toString();
        
        LoginViaPHP task = new LoginViaPHP();
		task.execute(new String[] { loginurl, netidIn, pwordIn });
    }

    private class LoginViaPHP extends AsyncTask<String, String, String> {
    	@Override
    	// check login credentials and returns true if login successful
    	protected String doInBackground(String... params) {
    		String loginurl;
    		String netidIn;
    		String pwordIn;
    		String auth;
    		// get url/login/password from params
    		try {
	    		loginurl = params[0];
	    		netidIn = params[1];
	    		pwordIn = params[2];
    		} catch (Exception e) {
    			e.printStackTrace();
    			return "error";
    		}
    		//System.out.println("attempting to login with: " + netidIn + ", " + pwordIn);
    		
    		// set up login/password to be posted to PHP
    		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    		nameValuePairs.add(new BasicNameValuePair("netid", netidIn));
    		nameValuePairs.add(new BasicNameValuePair("pword", pwordIn));
    		nameValuePairs.add(new BasicNameValuePair("auth", AUTHCODE));
    		
    		InputStream content;
    		
    		// try getting http response
    		try {
    			// TODO: check for https functionality
    	        HttpClient httpclient = new DefaultHttpClient();
    	        HttpPost httppost = new HttpPost(loginurl);	        
    	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
    	        HttpResponse response = httpclient.execute(httppost);
    	        HttpEntity entity = response.getEntity();
    	        content = entity.getContent();
    	    } catch(Exception e){
    	        Log.e("log_tag","Error in internet connection " + e.toString());
    	        return "error";
    	    }
    		//System.out.println("post successful");
    		
    		// try reading http response
    		String output = "";
    		try {
    			BufferedReader reader = new BufferedReader(new InputStreamReader(content,"iso-8859-1"), 8);
    	        String line;
    	        while((line = reader.readLine()) != null){
    	            output += line;
    	        }
    	        content.close();
    		} catch(Exception e){
    	        Log.e("log_tag", "Error converting result " + e.toString());
    	        return "error";
    	    }
    		System.out.println(output);
    		return output; // ensure non-null
    	}
    	@Override
    	// process result of login query (true or false)
    	protected void onPostExecute(String result) {
    		final String loginsuccess = "yes"; // output from PHP to match
    		if (result.equals(loginsuccess)) {
    			setContentView(R.layout.loggedin);
    		} else {
    			final TextView loginstatustxt = (TextView) findViewById(R.id.loginstatustxt);
    			loginstatustxt.setText("Login failed! Please try again or register.");
    			loginstatustxt.setTextColor(Color.RED);
    		}
    	}
    }

    private class RegisterViaPHP extends AsyncTask<String, String, String> {
    	protected String doInBackground(String... params) {
    		String registerurl;
    		String netidIn;
    		String pwordIn;
    		String emailIn;
    		
    		// get url/login/password from params
    		try {
	    		registerurl = params[0];
	    		netidIn = params[1];
	    		pwordIn = params[2];
	    		emailIn = params[3];
    		} catch (Exception e) {
    			e.printStackTrace();
    			return "error";
    		}
    		System.out.println("attempting to register with: " + netidIn + ", " + pwordIn + ", " + emailIn);
    		
    		// set up login/password to be posted to PHP
    		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    		nameValuePairs.add(new BasicNameValuePair("netid", netidIn));
    		nameValuePairs.add(new BasicNameValuePair("pword", pwordIn));
    		nameValuePairs.add(new BasicNameValuePair("email", emailIn));
    		nameValuePairs.add(new BasicNameValuePair("auth", AUTHCODE));
    		
    		InputStream content;
    		
    		// try getting http response
    		try {
    			// TODO: check for https functionality
    	        HttpClient httpclient = new DefaultHttpClient();
    	        HttpPost httppost = new HttpPost(registerurl);	        
    	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
    	        HttpResponse response = httpclient.execute(httppost);
    	        HttpEntity entity = response.getEntity();
    	        content = entity.getContent();
    	    } catch(Exception e){
    	        Log.e("log_tag","Error in internet connection " + e.toString());
    	        return "error";
    	    }
    		System.out.println("post successful");
    		
    		// try reading http response
    		String output = "";
    		try {
    			BufferedReader reader = new BufferedReader(new InputStreamReader(content,"iso-8859-1"), 8);
    	        String line;
    	        while((line = reader.readLine()) != null){
    	            output += line;
    	        }
    	        content.close();
    		} catch(Exception e){
    	        Log.e("log_tag", "Error converting result " + e.toString());
    	        return "error";
    	    }
    		Log.e("log_tag", "output: " + output);
    		return output;
    	}
    	protected void onPostExecute(String result) {
    		final String registersuccess = "yes";
    		if (result.equals(registersuccess)) {
    			setContentView(R.layout.loggedin);
    		} else {
    			final TextView registerstatustxt = (TextView) findViewById(R.id.registerstatustxt);
    			registerstatustxt.setText("Registration failed! Username already exists.");
    			registerstatustxt.setTextColor(Color.RED);
    		}
    	}
    }
    // called when register button is clicked
    public void doRegister(View v) {
    	final String registerurl = "http://webscript.princeton.edu/~pcao/cos333/doregister.php";
    	
    	final EditText netidEdit = (EditText) findViewById(R.id.netidEntry);
        final EditText passwordEdit = (EditText) findViewById(R.id.passwordEntry);
        final EditText confirmEdit = (EditText) findViewById(R.id.confirmEntry);
        final EditText emailEdit = (EditText) findViewById(R.id.emailEntry);
        String netidIn = netidEdit.getText().toString();
        String pwordIn = passwordEdit.getText().toString();
        String confirmIn = confirmEdit.getText().toString();
        String emailIn = emailEdit.getText().toString();
        
        if (!pwordIn.equals(confirmIn)) { // check that passwords match
        	final TextView registerstatustxt = (TextView) findViewById(R.id.registerstatustxt);
        	registerstatustxt.setText("Passwords do not match!");
        	registerstatustxt.setTextColor(Color.RED);
        	return;
        }
        RegisterViaPHP task = new RegisterViaPHP();
		task.execute(new String[] { registerurl, netidIn, pwordIn, emailIn });
    }
    
    public void gotoregister(View v) {
    	final EditText netidFromLogin = (EditText) findViewById(R.id.netidEntry);
        final EditText passwordFromLogin = (EditText) findViewById(R.id.passwordEntry);
        String netidIn = netidFromLogin.getText().toString();
        String pwordIn = passwordFromLogin.getText().toString();
    	setContentView(R.layout.register);
    	final EditText netidFromRegister = (EditText) findViewById(R.id.netidEntry);
        final EditText passwordFromRegister = (EditText) findViewById(R.id.passwordEntry);
        netidFromRegister.setText(netidIn);
        passwordFromRegister.setText(pwordIn);
    }
    
    public void backtologin(View v) {
    	setContentView(R.layout.main);
    }
    
    protected void onDestroy() {
    	Log.d(TAG, "destroying...");
    	super.onDestroy();
    }
    protected void onStop() {
    	Log.d(TAG, "stopping...");
    	super.onStop();
    }
}