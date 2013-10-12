package com.rfduino.examples;

import android.view.View;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListAllExamples extends ListActivity {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        
		
		//Get a list of all the Example activities that we want to be able to launch. 
		setListAdapter(new ArrayAdapter<String>(this,
	            android.R.layout.simple_list_item_1,
	            getResources().getStringArray(R.array.example_activities)
				));
		
		setupListElementListener();
		
	}

	
	private void setupListElementListener() {
        ListView list = (ListView) getListView();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked,int position, long id) {

                Intent i;
				try {
					//Use the XML list in strings.xml to get the Java class name of the example we want to start, and tell Android to start that Activity
					i = new Intent(viewClicked.getContext(), Class.forName("com.rfduino.examples." + getListView().getItemAtPosition(position).toString()));
					startActivity(i);
				} catch (ClassNotFoundException e) {
					// Catch the response if the example doesn't exist yet. 
					e.printStackTrace();
				}
                
            }
        });
    }
	
	
}
