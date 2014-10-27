package com.wyy.myhealth.ui.fooddetails;

import java.util.ArrayList;
import java.util.List;

import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wyy.myhealth.R;
import com.wyy.myhealth.analytics.UmenAnalyticsUtility;
import com.wyy.myhealth.app.WyyApplication;
import com.wyy.myhealth.bean.Comment;
import com.wyy.myhealth.http.AsyncHttpResponseHandler;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.ui.absfragment.utils.TimeUtils;
import com.wyy.myhealth.ui.baseactivity.BaseListActivity;
import com.wyy.myhealth.ui.fooddetails.FoodCommentAdapter.AdapterListener;
import com.wyy.myhealth.utils.BingLog;
import com.wyy.myhealth.utils.InputUtlity;

public class FoodCommentInfoActivity extends BaseListActivity implements
		AdapterListener {

	private static final String TAG = FoodCommentInfoActivity.class
			.getSimpleName();

	private List<Comment> comments = new ArrayList<>();

	private FoodCommentAdapter adapter;
	// 评论View
	private View sendView;
	// 评论编辑
	private EditText sendEditText;
	// 评论按钮
	private Button sendButton;

	private int position = 0;

	private String content = "";

	@SuppressWarnings("unchecked")
	@Override
	protected void onInitView() {
		// TODO Auto-generated method stub
		context = this;
		sendView = findViewById(R.id.send_v);
		initSendView(sendView);
		try {
			List<Comment> mcomments = (List<Comment>) getIntent()
					.getSerializableExtra("comment");
			TimeUtils.getCOmmentTime(mcomments);
			arrangeComment(mcomments);
			if (mcomments.size() == 0) {
				findViewById(R.id.no_comment_txt).setVisibility(View.VISIBLE);
			}
		} catch (Exception e) {
			// TODO: handle exception
			findViewById(R.id.no_comment_txt).setVisibility(View.VISIBLE);
		}

	}

	@Override
	protected void onInitActionBar() {
		// TODO Auto-generated method stub
		super.onInitActionBar();
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle(R.string.commentinfo);
	}

	@Override
	public void comment(int position) {
		// TODO Auto-generated method stub
		this.position = position;
		if (!sendView.isShown()) {
			sendView.setVisibility(View.VISIBLE);
			sendEditText.requestFocus();
			InputUtlity.showInputWindow(context, sendEditText);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		UmenAnalyticsUtility.onResume(context);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		UmenAnalyticsUtility.onPause(context);
	}
	
	
	private void initSendView(View v) {
		sendButton = (Button) v.findViewById(R.id.send_msg_btn);
		sendEditText = (EditText) v.findViewById(R.id.send_msg_editText);
		sendButton.setOnClickListener(listener);
	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {

			case R.id.send_msg_btn:
				replyComment();
				break;

			default:
				break;
			}
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (sendView.isShown()) {
				sendView.setVisibility(View.GONE);
				return true;
			}

		}

		return super.onKeyDown(keyCode, event);
	}

	private void replyComment() {
		sendEditText.setError(null);
		content = sendEditText.getText().toString();
		if (TextUtils.isEmpty(content)) {
			sendEditText.setError(getString(R.string.nullcontentnotice));
			sendEditText.requestFocus();
			return;
		}

		HealthHttpClient.postComment(WyyApplication.getInfo().getId(), comments
				.get(position).getId(), content, comments.get(position)
				.getFoodsid(), handler);

	}

	private AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			super.onStart();

			Comment comment = new Comment();
			comment.setUsername(WyyApplication.getInfo().getUsername());
			comment.setContent(content);

			comments.get(position).getComment().add(comment);
			adapter.notifyDataSetChanged();

			sendEditText.setText("");
			sendView.setVisibility(View.GONE);
			sendButton.setEnabled(true);
			sendButton.setText(R.string.send);
			sendButton.setBackgroundColor(getResources().getColor(
					R.color.transparent));
		}

		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			super.onFinish();

		}

		@Override
		public void onSuccess(String content) {
			// TODO Auto-generated method stub
			super.onSuccess(content);
			BingLog.i(TAG, "评论:" + content);
			Toast.makeText(context, R.string.comment_success_,
					Toast.LENGTH_LONG).show();

		}

		@Override
		public void onFailure(Throwable error, String content) {
			// TODO Auto-generated method stub
			super.onFailure(error, content);
			Toast.makeText(context, R.string.comment_faliure, Toast.LENGTH_LONG)
					.show();
		}

	};

	private synchronized void arrangeComment(List<Comment> list) {
		int length = list.size();
		for (int i = 0; i < length; i++) {
			Comment comment = list.get(i);
			BingLog.i(TAG, "评论ID:" + comment.getCommentid());
			if (comment.getCommentid() == -1) {
				BingLog.i(TAG, "增加ID:" + comment.getId());
				comments.add(comment);
			}
		}

		int clength = comments.size();
		for (int k = 0; k < length; k++) {
			Comment comment = list.get(k);
			BingLog.i(TAG, "评论:" + "i:" + k);
			for (int j = 0; j < clength; j++) {
				BingLog.i(TAG, "评论:" + "i:" + k + "j:" + j);
				if (comments.get(j).getId().equals("" + comment.getCommentid())) {
					comments.get(j).comment.add(comment);
					BingLog.i(TAG, "评论:" + comment);
				}

				BingLog.i(TAG, "评论:" + "i:" + k + "j:" + j);

			}
		}

		BingLog.i(TAG, "评论数量:" + comments.size());

		adapter = new FoodCommentAdapter(comments, context);
		baseListV.setAdapter(adapter);
		adapter.setListener(this);

	}

}
