package com.lbins.meetlove.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import com.lbins.meetlove.R;

public class SideBar extends View {
	 private char[] l;  
	    private SectionIndexer sectionIndexter = null;
	    private ListView list;
	    private TextView mDialogText;
	    private final int m_nItemHeight = 54;
	    public SideBar(Context context) {
	        super(context);  
	        init();  
	    }  
	    public SideBar(Context context, AttributeSet attrs) {
	        super(context, attrs);  
	        init();  
	    }
	// 26个字母
	public static String[] b = { "#", "A", "B", "C", "D", "E", "F", "G", "H",
			"I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
			"V", "W", "X", "Y", "Z" };


	    private void init() {  
	        l = new char[] { '#',  'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
	                'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
	    }  
	    public SideBar(Context context, AttributeSet attrs, int defStyle) {
	        super(context, attrs, defStyle); 
	        init();  
	    }  
	    public void setListView(ListView _list) {
	        list = _list;  
	        sectionIndexter = (SectionIndexer) _list.getAdapter();
	    }  
	    public void setTextView(TextView mDialogText) {
	    	this.mDialogText = mDialogText;  
	    }  
	    public boolean onTouchEvent(MotionEvent event) {
	        super.onTouchEvent(event);  
	        int i = (int) event.getY();  
	        int idx = i / m_nItemHeight;  
	        if (idx >= l.length) {  
	            idx = l.length - 1;  
	        } else if (idx < 0) {  
	            idx = 0;  
	        }  
	        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
	        	mDialogText.setVisibility(View.VISIBLE);
	        	mDialogText.setText(""+l[idx]);
	            if (sectionIndexter == null) {
	                sectionIndexter = (SectionIndexer) list.getAdapter();
	            }  
	            int position = sectionIndexter.getPositionForSection(l[idx]);  
	            if (position == -1) {  
	                return true;  
	            }  
	            list.setSelection(position);
	        }else{
	        	mDialogText.setVisibility(View.INVISIBLE);
	        }  
	        return true;  
	    }  
//	    protected void onDraw(Canvas canvas) {
//	        Paint paint = new Paint();
//			paint.setTypeface(Typeface.DEFAULT_BOLD);
//	        paint.setColor(getResources().getColor(R.color.black));
//	        paint.setTextSize(26);
//	        paint.setTextAlign(Paint.Align.CENTER);
//	        float widthCenter = getMeasuredWidth() / 2;
//	        for (int i = 0; i < l.length; i++) {
//	            canvas.drawText(String.valueOf(l[i]), widthCenter, m_nItemHeight + (i * m_nItemHeight), paint);
//	        }
//	        super.onDraw(canvas);
//	    }
	//画笔
	Paint paint = new Paint();
	//选择的值
	int choose = -1;

	/**
	 * 重写这个方法
	 */
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//如果面板处于点击状态就将面板的背景色绘制为灰色
//		if (showBkg) {
//			canvas.drawColor(Color.parseColor("#40000000"));
//		}
		//获得Ｖｉｅｗ的高
		int height = getHeight();
		//获得Ｖｉｅｗ的宽
		int width = getWidth();
		//计算得出每一个字体大概的高度
		int singleHeight = height / b.length;
		for (int i = 0; i < b.length; i++) {
			//设置锯齿
			paint.setAntiAlias(true);
			//设置字体大小
			paint.setTextSize(15);
			//点击的字体和26个字母中的任意一个相等就
			if (i == choose) {
				//绘制点击的字体的颜色为蓝色
				paint.setColor(Color.parseColor("#3399ff"));
				paint.setFakeBoldText(true);
			}
			//得到字体的X坐标
			float xPos = width / 2 - paint.measureText(b[i]) / 2;
			//得到字体的Y坐标
			float yPos = singleHeight * i + singleHeight+3;
			//将字体绘制到面板上
			canvas.drawText(b[i], xPos, yPos, paint);
			//还原画布
			paint.reset();
		}

	}
}
