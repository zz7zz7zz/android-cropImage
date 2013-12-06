package com.open.crop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * 底图不变，浮层缩放
 * @author yanglonghui
 *
 */
public class CropImageView3 extends View {
	
	//单点触摸的时候
	private float oldX=0;
	private float oldY=0;
	
	//状态
	private final int STATUS_Touch_SINGLE=1;//单点
	private final int STATUS_TOUCH_MULTI_START=2;//多点开始
	private final int STATUS_TOUCH_MULTI_TOUCHING=3;//多点拖拽中
	
	private int mStatus=STATUS_Touch_SINGLE;
	
	//默认的裁剪图片宽度与高度
	private final int defaultCropWidth=300;
	private final int defaultCropHeight=300;
	private int cropWidth=defaultCropWidth;
	private int cropHeight=defaultCropHeight;
	
	private final int EDGE_LT=1;//左上
	private final int EDGE_RT=2;//右上
	private final int EDGE_LB=3;//左下
	private final int EDGE_RB=4;//右下
	private final int EDGE_MOVE_IN=5;//里面移动
	private final int EDGE_MOVE_OUT=6;//外面移动
	private final int EDGE_NONE=7;//外面移动
	
	public int currentEdge=EDGE_NONE;
	
	protected float oriRationWH=0;//原始宽高比率
	protected final float maxZoomOut=5.0f;//最大扩大到多少倍
	protected final float minZoomIn=0.333333f;//最小缩小到多少倍
	
	protected Drawable mDrawable;//原图
	protected FloatDrawable mFloatDrawable;//浮层
	protected Rect mDrawableSrc = new Rect();
	protected Rect mDrawableDst = new Rect();
	protected Rect mDrawableFloat = new Rect();//浮层选择框，就是头像选择框
	protected boolean isFrist=true;
	private boolean isTouchInSquare=true;
	
	protected Context mContext;
    
	public CropImageView3(Context context) {
		super(context);
		init(context);
	}

	public CropImageView3(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public CropImageView3(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
			
	}
	
	private void init(Context context)
	{
		this.mContext=context;
		try {  
            if(android.os.Build.VERSION.SDK_INT>=11)  
            {  
            	this.setLayerType(LAYER_TYPE_SOFTWARE, null);  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
		mFloatDrawable=new FloatDrawable(context);//头像选择框
	}

	public void setDrawable(Drawable mDrawable,int cropWidth,int cropHeight)
	{
		this.mDrawable=mDrawable;
		this.cropWidth=cropWidth;
		this.cropHeight=cropHeight;
		this.isFrist=true;
		invalidate();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		if(event.getPointerCount()>1)
		{
			if(mStatus==STATUS_Touch_SINGLE)
			{
				mStatus=STATUS_TOUCH_MULTI_START;
			}
			else if(mStatus==STATUS_TOUCH_MULTI_START)
			{
				mStatus=STATUS_TOUCH_MULTI_TOUCHING;
			}
		}
		else
		{
			if(mStatus==STATUS_TOUCH_MULTI_START||mStatus==STATUS_TOUCH_MULTI_TOUCHING)
			{
				oldX=event.getX();
				oldY=event.getY();
			}
			
			mStatus=STATUS_Touch_SINGLE;
		}
		
//Log.v("count currentTouch"+currentTouch, "-------");	

		switch(event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				oldX = event.getX();
				oldY = event.getY();
				currentEdge=getTouchEdge((int)oldX, (int)oldY);
				isTouchInSquare=mDrawableFloat.contains((int)event.getX(), (int)event.getY());
Log.v("currentEdge："+currentEdge, "-------");
	            break;
	            
			case MotionEvent.ACTION_UP:
				checkBounds();
	            break;
	            
			case MotionEvent.ACTION_POINTER_1_DOWN:
				break;
				
			case MotionEvent.ACTION_POINTER_UP:
				currentEdge=EDGE_NONE;
				break;
	            
			case MotionEvent.ACTION_MOVE:
				if(mStatus==STATUS_TOUCH_MULTI_TOUCHING)
				{
					
				}
				else if(mStatus==STATUS_Touch_SINGLE)
				{
					int dx=(int)(event.getX()-oldX);
					int dy=(int)(event.getY()-oldY);
					
					oldX=event.getX();
					oldY=event.getY();
					
					if(!(dx==0&&dy==0))
					{
						switch(currentEdge)
						{
								case EDGE_LT:
									mDrawableFloat.set(mDrawableFloat.left+dx, mDrawableFloat.top+dy, mDrawableFloat.right, mDrawableFloat.bottom);
									break;
									
								case EDGE_RT:
									mDrawableFloat.set(mDrawableFloat.left, mDrawableFloat.top+dy, mDrawableFloat.right+dx, mDrawableFloat.bottom);
									break;
									
								case EDGE_LB:
									mDrawableFloat.set(mDrawableFloat.left+dx, mDrawableFloat.top, mDrawableFloat.right, mDrawableFloat.bottom+dy);
									break;
									
								case EDGE_RB:
									mDrawableFloat.set(mDrawableFloat.left, mDrawableFloat.top, mDrawableFloat.right+dx, mDrawableFloat.bottom+dy);
									break;
									
								case EDGE_MOVE_IN:
									if(isTouchInSquare)
									{
										mDrawableFloat.offset((int)dx, (int)dy);
									}
									break;

								case EDGE_MOVE_OUT:
									break;
						}
						mDrawableFloat.sort();
						invalidate();
					}
				}
	            break;
		}
		
		return true;
	}

    public int getTouchEdge(int eventX,int eventY)
    {
    	if(mFloatDrawable.getBounds().left<=eventX&&eventX<(mFloatDrawable.getBounds().left+mFloatDrawable.getCirleWidth())
    			&&mFloatDrawable.getBounds().top<=eventY&&eventY<(mFloatDrawable.getBounds().top+mFloatDrawable.getCirleHeight()))
    	{
    		return EDGE_LT;//左上
    	}
    	else  if((mFloatDrawable.getBounds().right-mFloatDrawable.getCirleWidth())<=eventX&&eventX<mFloatDrawable.getBounds().right
    			&&mFloatDrawable.getBounds().top<=eventY&&eventY<(mFloatDrawable.getBounds().top+mFloatDrawable.getCirleHeight()))
    	{
    		return EDGE_RT;//右上
    	}
    	else  if(mFloatDrawable.getBounds().left<=eventX&&eventX<(mFloatDrawable.getBounds().left+mFloatDrawable.getCirleWidth())
    			&&(mFloatDrawable.getBounds().bottom-mFloatDrawable.getCirleHeight())<=eventY&&eventY<mFloatDrawable.getBounds().bottom)
    	{
    		return EDGE_LB;//左下
    	}
    	else  if((mFloatDrawable.getBounds().right-mFloatDrawable.getCirleWidth())<=eventX&&eventX<mFloatDrawable.getBounds().right
    			&&(mFloatDrawable.getBounds().bottom-mFloatDrawable.getCirleHeight())<=eventY&&eventY<mFloatDrawable.getBounds().bottom)
    	{
    		return EDGE_RB;//右下
    	}
    	else if(mFloatDrawable.getBounds().contains(eventX,eventY))
    	{
    		return EDGE_MOVE_IN;//里面移动
    	}
    	return EDGE_MOVE_OUT;
    }
    
	@Override
	protected void onDraw(Canvas canvas) {
//		super.onDraw(canvas);
		if (mDrawable == null) {
            return; // couldn't resolve the URI
        }

        if (mDrawable.getIntrinsicWidth() == 0 || mDrawable.getIntrinsicHeight() == 0) {
            return;     // nothing to draw (empty bounds)
        }
        
        configureBounds();
        
		mDrawable.draw(canvas);
        canvas.save();
        canvas.clipRect(mDrawableFloat, Region.Op.DIFFERENCE);
        canvas.drawColor(Color.parseColor("#a0000000"));
        canvas.restore();
        mFloatDrawable.draw(canvas);
	}
	
	
	protected void configureBounds()
	{
		if(isFrist)
		{
			oriRationWH=((float)mDrawable.getIntrinsicWidth())/((float)mDrawable.getIntrinsicHeight());
			
			final float scale = mContext.getResources().getDisplayMetrics().density;
			int w=Math.min(getWidth(), (int)(mDrawable.getIntrinsicWidth()*scale+0.5f));
			int h=(int) (w/oriRationWH);
			
			int left = (getWidth()-w)/2;
			int top = (getHeight()-h)/2;
			int right=left+w;
			int bottom=top+h;
			
			mDrawableSrc.set(left,top,right,bottom);
			mDrawableDst.set(mDrawableSrc);
			
			int floatWidth=dipTopx(mContext, cropWidth);
			int floatHeight=dipTopx(mContext, cropHeight);
			int floatLeft=(getWidth()-floatWidth)/2;
			int floatTop = (getHeight()-floatHeight)/2;
			mDrawableFloat.set(floatLeft, floatTop,floatLeft+floatWidth, floatTop+floatHeight);
			
	        isFrist=false;
		}
        
		mDrawable.setBounds(mDrawableDst);
		mFloatDrawable.setBounds(mDrawableFloat);
	}
	
	protected void checkBounds()
	{
		int newLeft = mDrawableFloat.left;
		int newTop = mDrawableFloat.top;
		
		boolean isChange=false;
		if(mDrawableFloat.left<getLeft())
		{
			newLeft=getLeft();
			isChange=true;
		}
		
		if(mDrawableFloat.top<getTop())
		{
			newTop=getTop();
			isChange=true;
		}
		
		if(mDrawableFloat.right>getRight())
		{
			newLeft=getRight()-mDrawableFloat.width();
			isChange=true;
		}

		if(mDrawableFloat.bottom>getBottom())
		{
			newTop=getBottom()-mDrawableFloat.height();
			isChange=true;
		}
		
		mDrawableFloat.offsetTo(newLeft, newTop);
		if(isChange)
		{
			invalidate();
		}
	}
	
	public Bitmap getCropImage()
	{
		Bitmap tmpBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.RGB_565);
		Canvas canvas = new Canvas(tmpBitmap);
		mDrawable.draw(canvas);

		Matrix matrix=new Matrix();
		float scale=(float)(mDrawableSrc.width())/(float)(mDrawableDst.width());
		matrix.postScale(scale, scale);
		
	    Bitmap ret = Bitmap.createBitmap(tmpBitmap, mDrawableFloat.left, mDrawableFloat.top, mDrawableFloat.width(), mDrawableFloat.height(), matrix, true);
	    tmpBitmap.recycle();
	    tmpBitmap=null;
	    
		return ret;
	}
    
    public int dipTopx(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
       return (int) (dpValue * scale + 0.5f);  
    }
}
