package com.mega;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Style;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class VideoView extends View implements Runnable {

	Paint mPaint = null;

	int width = 720;
	int height = 576;

	byte[] mPixel = new byte[width * height * 4]; 

	ByteBuffer buffer = ByteBuffer.wrap(mPixel);
	Bitmap VideoBit = Bitmap.createBitmap(width, height, Config.ARGB_8888);

	private String PathFileName;
	private boolean isTerminate; 
	static {
		System.loadLibrary("XvidAndroid");
	}
	public native int InitDecoder(int xdim, int ydim);
	public native int StopDecoder();
	public native int Decode(byte[] in, int startpos, int inSize, byte[] out);
	
	public VideoView(Context context) {
		super(context);
		
		LinearLayout layout = new LinearLayout(context);   //线性布局方式     
		layout.setOrientation( LinearLayout.VERTICAL ); //控件对其方式为垂直排列     
		layout.setBackgroundColor( 0xff00ffff );        //设置布局板的一个特殊颜色，这可以检验我们会话时候是否有地方颜色不正确！     
		 
		TextView tv = new TextView(context);
		tv.setText("您好 1234567890");
		tv.setVisibility(View.VISIBLE);
		
		mPaint = new Paint();  
	}

	public void playVideo(String file) {
		PathFileName = file;
		isTerminate = false;
		new Thread(this).start(); 
	}
	
	public void exitVideo() {
		isTerminate = true;
	}
	
	protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);       	
        VideoBit.copyPixelsFromBuffer(buffer);
        canvas.drawBitmap(VideoBit, 0, 0, null); 
        
        mPaint.setStyle(Style.FILL); //设置填充  
        canvas.drawRect(10, 10, 100, 100, mPaint); //绘制矩形  
          
        mPaint.setColor(Color.BLUE);  
        canvas.drawText("我是被画出来的", 10, 120, mPaint);          
    }

	
	public void run() {
		FileInputStream fileIS = null;
		
		int BUFFER_SIZE = 1024 * 1024;
		
		byte[] sockBuf = new byte[BUFFER_SIZE];

		try {
			fileIS = new FileInputStream(PathFileName);
		} catch (IOException e) {
			return;
		}

		boolean eofFile = false;
		int usefulBytes = 0;
		try {
			usefulBytes = fileIS.read(sockBuf, 0, BUFFER_SIZE);
			eofFile = usefulBytes < BUFFER_SIZE;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		InitDecoder(width, height);
		int usedBytes = 0;
		
		do {
			if (!eofFile && usefulBytes < BUFFER_SIZE / 2) {
				if (usefulBytes > 0) {
					System.arraycopy(sockBuf, BUFFER_SIZE - usefulBytes, sockBuf, 0, usefulBytes);
				}
				try {
					int len = fileIS.read(sockBuf, usefulBytes, BUFFER_SIZE - usefulBytes);
					if (len > 0) {
						usefulBytes += len;
					} else {
						eofFile = true;
					}
					usedBytes = 0;
				} catch (IOException e) {  
					e.printStackTrace();
				}
			}
			int curUsedBytes = 0;
			try {
				curUsedBytes = this.Decode(sockBuf, usedBytes, usefulBytes, mPixel);
				Thread.sleep(15);
			} catch(Throwable e) {
				e.printStackTrace();
			}
			if (curUsedBytes > 0) { //绘制图片
				postInvalidate();
			} else { 
				break;
			}
			usedBytes += curUsedBytes;
			usefulBytes -= curUsedBytes;
		} while(usefulBytes > 0 && !isTerminate);
		
		try {
			if (fileIS != null)
				fileIS.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		StopDecoder();
	}
}