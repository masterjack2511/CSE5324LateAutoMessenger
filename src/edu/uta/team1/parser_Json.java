package edu.uta.team1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class parser_Json {

public static JSONObject getJSONfromURL(String url){

    //initialize
    InputStream is = null;
    String result = "";
    JSONObject jArray = null;

    //http post
    try{
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();
        is = entity.getContent();

    }catch(Exception e){
        Log.e("log_tag", "Error in http connection "+e.toString());
    }

    //convert response to string
    try{
        BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        is.close();
        result=sb.toString();
    }catch(Exception e){
        Log.e("log_tag", "Error converting result "+e.toString());
    }

    //try parse the string to a JSON object
    try{
            jArray = new JSONObject(result);
    }catch(JSONException e){
        Log.e("log_tag", "Error parsing data "+e.toString());
    }

    return jArray;
}

 public static InputStream retrieveStream(String url) {

        DefaultHttpClient client = new DefaultHttpClient(); 

        HttpGet getRequest = new HttpGet(url);

        try {

           HttpResponse getResponse = client.execute(getRequest);
           final int statusCode = getResponse.getStatusLine().getStatusCode();

           if (statusCode != HttpStatus.SC_OK) { 

              return null;
           }

           HttpEntity getResponseEntity = getResponse.getEntity();
           return getResponseEntity.getContent();

        } 
        catch (IOException e) {
           getRequest.abort();

        }

        return null;

     }
}