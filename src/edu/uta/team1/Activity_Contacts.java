package edu.uta.team1;

import java.util.ArrayList;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.CommonDataKinds.Phone;
	
public class Activity_Contacts extends Activity {
	private String DEBUG_TAG = "Activity_Contacts";
	private static final int CONTACT_PICKER_RESULT = 1001;
	private ArrayList<String> phone_No = new ArrayList<String>();
	private String phoneNumber = "";
	private EditText phoneEntry;
	private ListView numberListView;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        phoneEntry = (EditText) findViewById(R.id.invite_phone);
        numberListView = (ListView) findViewById(R.id.saveContactNumbers);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.contact_list, menu);
        return true;
    }
    
    public void doLaunchContactPicker(View view) {
    	
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);  
        
        startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
       // Log.v(DEBUG_TAG,"inside doLauchContactPicker\n");
    }  
    
    @Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) 
    {  

        super.onActivityResult(requestCode, resultCode, data);
   		String DEBUG_TAG = null;
   	 
   	
   	   if (resultCode == RESULT_OK) 
       {  
       	Log.v(DEBUG_TAG, "resultCode: " + resultCode);
       	Log.v(DEBUG_TAG, "requestCode: " + requestCode);
       	Log.v(DEBUG_TAG, "RESULT_OK: " + RESULT_OK);
           switch (requestCode) {  
           case CONTACT_PICKER_RESULT:  
               Cursor cursor = null;  
               
               try 
               {  
                   Uri result = data.getData();  
                   Log.v(DEBUG_TAG, "Got a contact result: " + result.toString());  
                   // get the contact id from the Uri  
                   String id = result.getLastPathSegment();  
                   // query for everything email  
                   cursor = getContentResolver().query(Phone.CONTENT_URI, null, Phone.CONTACT_ID + "=?", new String[] {id},null);  
                   int phoneIdx = cursor.getColumnIndex(Phone.DATA);  
                   // let's just get the first email  
                   if (cursor.moveToFirst()) 
                   {  
                       phoneNumber = cursor.getString(phoneIdx);  
                       Log.v(DEBUG_TAG, "Got phone: " + phoneNumber);  
                   } else 
                   {  
                       Log.w(DEBUG_TAG, "No results");
                   }
               }
               catch (Exception e) 
               {  
                   Log.e(DEBUG_TAG, "Failed to get phone data", e);  
               } 
               finally 
               {  
                   if (cursor != null) 
                   {  
                       cursor.close();  
                   }  
                     
                   phoneEntry.setText(phoneNumber);  
                   if (phoneNumber.length() == 0) 
                   {  
                       Toast.makeText(this, "No phone found for contact.", Toast.LENGTH_LONG).show();  
                   }  
               }  
               break;  
           }  
       } 
       else 
       {  
           Log.w(DEBUG_TAG, "Warning: activity result not ok");  
       }  
   	  
    }  
    public void addMore(View v)
	{
    	savePhoneNumber();
    	phoneEntry.setText("");
    	loadList();
	}
    
    public void savePhoneNumber()
   	{
    	String numberEntry = phoneEntry.getText().toString(); 
    	if(numberEntry.length() != 0){
    		boolean found = false;
    		for(String number : phone_No){
    			if(number.equals(numberEntry)){
    				found = true;
    			}
    		}
    		if(!found) {
    			phone_No.add(numberEntry);
    		} else {
    			Toast.makeText(getApplicationContext(), "Duplicate number", Toast.LENGTH_SHORT).show();
    		}
    	}
   	}
	   
    private void loadList()
    {
    	numberListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, phone_No));
    }
    
    public void done(View view) {
    	savePhoneNumber();
    	
    	if(phone_No.size() > 0){
    		Intent returnIntent = new Intent();
    	    returnIntent.putExtra("contacts", phone_No);
    	    setResult(RESULT_OK, returnIntent);       
    	} else {
    		setResult(RESULT_CANCELED);
    	}
    	finish();
    }
}