package com.wyy.myhealth.ui.customview;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Scroller;
import android.widget.AbsListView.OnScrollListener;

public class BingListView extends ListView implements OnScrollListener {

	private float mLastY = -1; // save event y
	@SuppressWarnings("unused")
	private Scroller mScroller; // used for scroll back
	private OnScrollListener mScrollListener; // user's scroll listener
	private int mTotalItemCount;
	private IXListViewListener mListViewListener;
	
	public interface IXListViewListener {
		public void onLoadMore();
	}
	
	/**
	 * you can listen ListView.OnScrollListener or this one. it will invoke
	 * onXScrolling when header/footer scroll back.
	 */
	public interface OnXScrollListener extends OnScrollListener {
		public void onXScrolling(View view);
	}
	
	public BingListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		initWithContext(context);
	}

	
	private void initWithContext(Context context) {
		mScroller = new Scroller(context, new DecelerateInterpolator());
		// XListView need the scroll event, and it will dispatch the event to
		// user's listener (as a proxy).
		super.setOnScrollListener(this);

	}


	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		if (mScrollListener != null) {
			mScrollListener.onScrollStateChanged(view, scrollState);
		}
	}


	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		mTotalItemCount = totalItemCount;
		if (mScrollListener != null) {
			mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount,
					totalItemCount);
		}
	}
	
	
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mLastY == -1) {
			mLastY = ev.getRawY();
		}

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastY = ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			@SuppressWarnings("unused")
			final float deltaY = ev.getRawY() - mLastY;
			mLastY = ev.getRawY();
			break;
		default:
			mLastY = -1; // reset
			if (getFirstVisiblePosition() == 0) {
			} else if (getLastVisiblePosition() == mTotalItemCount - 1) {
				// invoke load more.
					startLoadMore();
			}
			break;
		}
		try {
			return super.onTouchEvent(ev);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return false;
	}
	
	
	private void startLoadMore() {
		if (mListViewListener != null) {
			mListViewListener.onLoadMore();
		}
	}
	
	public void setXListViewListener(IXListViewListener l) {
		mListViewListener = l;
	}
	
}
