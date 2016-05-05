package com.water.jxwz.customview;


import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.water.jxwz.R;
import com.water.jxwz.utils.UIUtils;

public class RefreshLayout extends SwipeRefreshLayout {

	private final int mTouchSlop;
	private ListView mListView;
	private OnLoadListener mOnLoadListener;

	private float firstTouchY;
	private float lastTouchY;

	private boolean isLoading = false;

	private View footerLayout;
	private TextView textMore;
	private ProgressBar progressBar;

	public RefreshLayout(Context context) {
		this(context, null);
	}

	public RefreshLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
	}

	// set the child view of RefreshLayout
	public void setChildView(ListView mListView) {
		footerLayout = UIUtils.inflate(R.layout.listview_footer);
		textMore = (TextView) footerLayout.findViewById(R.id.text_more);
		progressBar = (ProgressBar) footerLayout
				.findViewById(R.id.load_progress_bar);
		textMore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loadData();
			}
		});
		mListView.addFooterView(footerLayout);
		this.mListView = mListView;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			firstTouchY = event.getRawY();
			break;

		case MotionEvent.ACTION_UP:
			lastTouchY = event.getRawY();
			if (canLoadMore()) {
				loadData();
			}
			break;
		default:
			break;
		}

		return super.dispatchTouchEvent(event);
	}

	private boolean canLoadMore() {
		return isBottom() && !isLoading && isPullingUp();
	}

	private boolean isBottom() {
		if (mListView.getCount() > 0) {
			if (mListView.getLastVisiblePosition() == mListView.getAdapter()
					.getCount() - 1
					&& mListView.getChildAt(mListView.getChildCount() - 1)
							.getBottom() <= mListView.getHeight()) {
				return true;
			}
		}
		return false;
	}

	private boolean isPullingUp() {
		return (firstTouchY - lastTouchY) >= mTouchSlop;
	}

	private void loadData() {
		if (mOnLoadListener != null) {
			setLoading(true);
		}
	}

	public void setLoading(boolean loading) {
		if (mListView == null)
			return;
		isLoading = loading;
		if (loading) {
			if (isRefreshing()) {
				return;
			}
			mListView.setSelection(mListView.getAdapter().getCount() - 1);
			textMore.setVisibility(View.GONE);
			progressBar.setVisibility(View.VISIBLE);
			mOnLoadListener.onLoad();
		} else {
			firstTouchY = 0;
			lastTouchY = 0;
			textMore.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
		}
	}

	public boolean isLoading() {
		return isLoading;
	}

	public void setOnLoadListener(OnLoadListener loadListener) {
		mOnLoadListener = loadListener;
	}

	public interface OnLoadListener {
		public void onLoad();
	}
}