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
	private String DEBUG_TAG;
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
    	if(phoneNumber != null) {
	    	savePhoneNumber();
	    	phoneEntry.setText("");
	    	Log.v(DEBUG_TAG, "before loadList()"); 
	    	loadList();
	    	Log.v(DEBUG_TAG, "After loadList()"); 
    	}
		   
	}
    public void savePhoneNumber()
   	{
    	phone_No.add(phoneNumber);
       	
   		   
   	}
	   
    private void loadList()
    {
    	numberListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, phone_No));
    }
}