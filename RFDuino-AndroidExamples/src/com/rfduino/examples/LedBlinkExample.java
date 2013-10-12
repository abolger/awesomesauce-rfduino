package com.rfduino.examples;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;

public class LedBlinkExample extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_led_blink_example);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_led_blink_example, menu);
		return true;
	}

	public void onLedCheckboxClicked(View view){
		    // Is the view now checked?
		    boolean checked = ((CheckBox) view).isChecked();
		    
		    // Check which checkbox was clicked
		    switch(view.getId()) {
		        case R.id.checkBoxLedBlink:
		           /* if (checked)
		                // Put some meat on the sandwich
		            else
		                // Remove the meat */
		            break;
		        /*TODO: Multiple possible blink rates
		         * case R.id.checkBoxLedBlinkFaster:
		            break;
		       */
		        
		    }
		
	}
	
}
