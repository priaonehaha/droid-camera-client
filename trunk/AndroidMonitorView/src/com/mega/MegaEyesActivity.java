package com.mega;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

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
		//setContentView(R.layout.main);
		
		
		
		//VideoView videoView = (VideoView)findViewById(R.id.videoView);
		//videoView.playVideo("/sdcard/testXvid.avi");		
		VideoView vv = new VideoView(this);
		setContentView(vv); 
		//vv.playVideo("/sdcard/testXvid.avi");
		vv.playVideo("/sdcard/Video/a7.avi");
		
		//new Thread(new MonitorThread()).start();
		
	}
	
	/*
	protected void onPause () {
		vv.exitVideo();
		super.onPause();
	}
	*/
}