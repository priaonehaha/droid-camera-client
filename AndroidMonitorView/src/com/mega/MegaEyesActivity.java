package com.mega;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsoluteLayout;
import android.widget.LinearLayout;

class MonitorThread implements Runnable
{
	@Override
	public void run() 
	{
		do
		{
			Log.d("", "test");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		while(true);
	}
	
}

public class MegaEyesActivity extends Activity {  
    /** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		
		//VideoView videoView = (VideoView)findViewById(R.id.videoView);
		//videoView.playVideo("/sdcard/testXvid.avi");		
		VideoView vv = new VideoView(this); 
		//setContentView(vv); 
		
		vv.bringToFront();
		this.addContentView(vv, new AbsoluteLayout.LayoutParams(300, 300, 300, 300));
		
		//vv.playVideo("/sdcard/testXvid.avi");
		vv.playVideo("/sdcard/Video/s.es");
		
		//new Thread(new MonitorThread()).start();
		
	}
	
	/*
	protected void onPause () {
		vv.exitVideo();
		super.onPause();
	}
	*/
}