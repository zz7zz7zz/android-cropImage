package com.open.crop;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

public class MainUI extends Activity {

	private final int requestCode=100;
	private ImageView retImg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);	
		setContentView(R.layout.mainui);
		
		findViewById(R.id.btn1).setOnClickListener(listener);
		findViewById(R.id.btn2).setOnClickListener(listener);
		findViewById(R.id.btn3).setOnClickListener(listener);
		
		retImg=(ImageView)findViewById(R.id.retImg);
	}
	
	private View.OnClickListener listener=new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent mIntent=new Intent(getBaseContext(), CropImageUI.class);
			int index=1;
			switch(v.getId())
			{
				case R.id.btn1:
						index=1;
						break;
						
				case R.id.btn2:
					index=2;
					break;
					
				case R.id.btn3:
					index=3;
					break;
			}
			mIntent.putExtra("index", index);
			startActivityForResult(mIntent, requestCode);
		}
	};
	@Override
	protected void onActivityResult(int _requestCode, int resultCode, Intent data) {
		if(requestCode==_requestCode&&resultCode==RESULT_OK)
		{
			String path=data.getStringExtra("cropImagePath");
			retImg.setImageDrawable(BitmapDrawable.createFromPath(path));
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
}
