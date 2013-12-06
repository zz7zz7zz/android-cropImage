package com.open.crop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

public class CropImageUI extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);	
		
		int index=getIntent().getIntExtra("index", 1);
		if(index==1)
		{
			cropImage1();
		}
		else if(index==2)
		{
			cropImage2();
		}
		else if(index==3)
		{
			cropImage3();
		}
		else if(index==4)
		{
			cropImage4();
		}
	}
	
	private void cropImage1()
	{
		setContentView(R.layout.fragment_cropimage);
		final CropImageView mCropImage=(CropImageView)findViewById(R.id.cropImg);
		mCropImage.setDrawable(getResources().getDrawable(R.drawable.precrop),300,300);
		
		findViewById(R.id.save).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new Thread(new Runnable(){

					@Override
					public void run() {
						FileUtil.writeImage(mCropImage.getCropImage(), FileUtil.SDCARD_PAHT+"/crop.png", 100);
						
						Intent mIntent=new Intent();
						mIntent.putExtra("cropImagePath", FileUtil.SDCARD_PAHT+"/crop.png");
						setResult(RESULT_OK, mIntent);
						finish();
					}
				}).start();
			}
		});
	}
	
	private void cropImage2()
	{
		setContentView(R.layout.fragment_cropimage2);
		final CropImageView2 mCropImage=(CropImageView2)findViewById(R.id.cropImg);
		mCropImage.setDrawable(getResources().getDrawable(R.drawable.precrop),300,300);
		findViewById(R.id.save).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new Thread(new Runnable(){

					@Override
					public void run() {
						FileUtil.writeImage(mCropImage.getCropImage(), FileUtil.SDCARD_PAHT+"/crop.png", 100);
						Intent mIntent=new Intent();
						mIntent.putExtra("cropImagePath", FileUtil.SDCARD_PAHT+"/crop.png");
						setResult(RESULT_OK, mIntent);
						finish();
					}
				}).start();
			}
		});
	}
	
	private void cropImage3()
	{
		setContentView(R.layout.fragment_cropimage3);
		final CropImageView3 mCropImage=(CropImageView3)findViewById(R.id.cropImg);
		mCropImage.setDrawable(getResources().getDrawable(R.drawable.precrop),300,300);
		findViewById(R.id.save).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new Thread(new Runnable(){

					@Override
					public void run() {
						FileUtil.writeImage(mCropImage.getCropImage(), FileUtil.SDCARD_PAHT+"/crop.png", 100);
						Intent mIntent=new Intent();
						mIntent.putExtra("cropImagePath", FileUtil.SDCARD_PAHT+"/crop.png");
						setResult(RESULT_OK, mIntent);
						finish();
					}
				}).start();
			}
		});
	}
	
	private void cropImage4()
	{
		setContentView(R.layout.fragment_cropimage4);
		final CropImageView4 mCropImage=(CropImageView4)findViewById(R.id.cropImg);
		mCropImage.setDrawable(getResources().getDrawable(R.drawable.precrop),300,300);
		findViewById(R.id.save).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new Thread(new Runnable(){

					@Override
					public void run() {
						FileUtil.writeImage(mCropImage.getCropImage(), FileUtil.SDCARD_PAHT+"/crop.png", 100);
						Intent mIntent=new Intent();
						mIntent.putExtra("cropImagePath", FileUtil.SDCARD_PAHT+"/crop.png");
						setResult(RESULT_OK, mIntent);
						finish();
					}
				}).start();
			}
		});
	}
	
}
