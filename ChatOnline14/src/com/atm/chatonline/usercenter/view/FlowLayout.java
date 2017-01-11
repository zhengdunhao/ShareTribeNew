package com.atm.chatonline.usercenter.view;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class FlowLayout extends ViewGroup {

	//�洢���е�view
	private List<List<View>> mAllView=new ArrayList<List<View>>();
	private List<Integer> mLineHeight=new ArrayList<Integer>();

	public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO �Զ����ɵĹ��캯�����
	}

	public FlowLayout(Context context, AttributeSet attrs) {
		this(context, attrs,0);
		// TODO �Զ����ɵĹ��캯�����
	}

	public FlowLayout(Context context) {
		this(context,null);
		// TODO �Զ����ɵĹ��캯�����
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO �Զ����ɵķ������
		//�����Լ��Ŀ�͸ߣ���ȡ����ģʽ�Ͳ���ֵ
		int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
		int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
		int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
		int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
		//�����wrap_content
		int width=0;
		int height=0;
		//ÿһ�еĿ�͸�
		int lineWidth=0;
		int lineHeight=0;
		int cCount =getChildCount();//�õ�������view�ĸ���������ʽ�����������view�ĸ���
		for(int i=0;i<cCount;i++){
			//���ĳ����view
			View child=getChildAt(i);
			//������view�Ŀ�͸�
			measureChild(child, widthMeasureSpec,heightMeasureSpec );
			MarginLayoutParams lp=(MarginLayoutParams) child.getLayoutParams();
			int childWidth=child.getMeasuredWidth()+lp.leftMargin+lp.rightMargin;
			int childHeight=child.getMeasuredHeight()+lp.topMargin+lp.bottomMargin;
			if(childWidth+lineWidth>sizeWidth-getPaddingLeft()-getPaddingRight()){
				width=Math.max(width, lineWidth);
				lineWidth=childWidth;
				height=+lineHeight;
				lineHeight=childHeight;
				
			}else{
				lineWidth+=childWidth;
				lineHeight=Math.max(lineHeight, childHeight);
				
			}
			if(i==cCount-1){
				width=Math.max(width, lineWidth);
				height+=lineHeight;
			}
		}
		setMeasuredDimension(modeWidth==MeasureSpec.EXACTLY?sizeWidth:width+getPaddingLeft()+getPaddingRight()
				, modeHeight==MeasureSpec.EXACTLY?sizeHeight:height+getPaddingTop()+getPaddingBottom());
		
		
	}
	
	
	
	@SuppressLint("DrawAllocation")
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		mAllView.clear();
		mLineHeight.clear();
		int width=getWidth();
		int lineWidth=0;
		int lineHeight=0;
		List<View> lineViews=new ArrayList<View>();
		int cCount=getChildCount();
		for(int i=0;i<cCount;i++){
			View child=getChildAt(i);
			MarginLayoutParams lp=(MarginLayoutParams) child.getLayoutParams();
			int childWidth=child.getMeasuredWidth();
			int childHeight=child.getMeasuredHeight();
			if(childWidth+lineWidth+lp.leftMargin+lp.rightMargin>width-getPaddingLeft()-getPaddingRight()){
				mLineHeight.add(lineHeight);
				mAllView.add(lineViews);
				lineWidth=0;
				lineHeight=childHeight+lp.topMargin+lp.bottomMargin;
				lineViews=new ArrayList<View>();
			}
			
			lineWidth+=childWidth+lp.rightMargin+lp.leftMargin;
			lineHeight=Math.max(lineHeight, childHeight+lp.topMargin+lp.bottomMargin);
			lineViews.add(child);
			
		}
		
		mLineHeight.add(lineHeight);
		mAllView.add(lineViews);
		int left=getPaddingLeft();
		int top=getPaddingTop();
		int lineNum=mAllView.size();
		for(int i=0;i<lineNum;i++){
			lineViews=mAllView.get(i);
			lineHeight=mLineHeight.get(i);
			for(int j=0;j<lineViews.size();j++){
				
				View child=lineViews.get(j);
				if(child.getVisibility()==View.GONE){
					continue;
				}
				MarginLayoutParams lp=(MarginLayoutParams) child.getLayoutParams();
				int lc=left+lp.leftMargin;
				int tc=top+lp.topMargin;
				int rc=lc+child.getMeasuredWidth();
				int bc=tc+child.getMeasuredHeight();
				child.layout(lc, tc, rc, bc);
				left+=child.getMeasuredWidth()+lp.leftMargin+lp.rightMargin;
				
			}
			left=getPaddingLeft();
			top+=lineHeight;
		}
	}

	
	//�뵱ǰlayout���Ӧ��layoutparams
	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		// TODO �Զ����ɵķ������
		return new MarginLayoutParams(getContext(), attrs);
	}

}
