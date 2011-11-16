package org.nypl.mover;

//import android.app.Activity;
import android.os.Bundle;

import com.phonegap.*;

public class MoverActivity extends DroidGap {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        super.loadUrl("file:///android_asset/www/indexmga.html");
    }
    
}