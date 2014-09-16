package com.wyy.myhealth.ui.shaiyishai;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wyy.myhealth.R;
import com.wyy.myhealth.app.PreferencesFoodsInfo;
import com.wyy.myhealth.app.WyyApplication;
import com.wyy.myhealth.bean.CommentBean;
import com.wyy.myhealth.bean.ListDataBead;
import com.wyy.myhealth.bean.MoodaFoodBean;
import com.wyy.myhealth.contants.ConstantS;
import com.wyy.myhealth.db.utils.ShaiDatebaseUtils;
import com.wyy.myhealth.http.AsyncHttpResponseHandler;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.support.collect.CollectUtils;
import com.wyy.myhealth.ui.absfragment.ListBaseFragment;
import com.wyy.myhealth.ui.absfragment.adapter.ShaiYiSaiAdapter2.ShaiItemOnclickListener;
import com.wyy.myhealth.ui.absfragment.utils.TimeUtility;
import com.wyy.myhealth.ui.customview.BingListView.IXListViewListener;
import com.wyy.myhealth.ui.fooddetails.FoodDetailsActivity;
import com.wyy.myhealth.ui.photoPager.PhotoPagerActivity;
import com.wyy.myhealth.utils.BingLog;
import com.wyy.myhealth.utils.InputUtlity;

public class ShaiyishaiFragment extends ListBaseFragment implements
		OnRefreshListener, IXListViewListener, OnItemClickListener,
		ShaiItemOnclickListener {

	// 数据库ID
	private int _id = 0;
	// 评论View
	private View sendView;
	// 评论编辑
	private EditText sendEditText;
	// 评论按钮
	private Button sendButton;
	// 美食ID
	private String shaiFoodsid = "";
	// 心情ID
	private String shaimoodsid = "";

	private MoodaFoodBean adversie;

	private String key = "";
	

	public void setKey(String key) {
		this.key = key;
		thList2.clear();
		mAdapter2.notifyDataSetChanged();
	}

	public View getSendView() {
		return sendView;
	}

	@Override
	protected void initView(View v) {
		// TODO Auto-generated method stub
		super.initView(v);
		sendView = v.findViewById(R.id.send_v);
		mRefreshLayout.setOnRefreshListener(this);
		mListView.setXListViewListener(this);
		initSendView(sendView);
		mAdapter2.setListener(this);
		mListView.setOnItemClickListener(this);
	}

	@Override
	protected void onGetLastData() {
		// TODO Auto-generated method stub
		super.onGetLastData();
		getLastStatus();
	}

	@SuppressWarnings("unchecked")
	private void getLastStatus() {
		ShaiDatebaseUtils shaiDatebaseUtils = new ShaiDatebaseUtils(
				getActivity());
		lastDataBeads = (List<ListDataBead>) shaiDatebaseUtils.queryData();
		if (null != lastDataBeads) {
			if (lastDataBeads.size() > 0) {
				json = lastDataBeads.get(0).getJsondata();
				postion = lastDataBeads.get(0).getPostion();
				_id = lastDataBeads.get(0).getPostion();
			}

		}
		shaiDatebaseUtils.close();

		if (TextUtils.isEmpty(json)) {
			reshShayiSai("0", limit);
		} else {
			try {
				reshParseJson(new JSONObject(json));
			} catch (Exception e) {
				// TODO: handle exception
			}

			reshShayiSai("0", limit);

		}

	}

	@Override
	protected void saveJsontoDb(String json, int postion) {
		// TODO Auto-generated method stub
		super.saveJsontoDb(json, postion);

		if (TextUtils.isEmpty(json)) {
			return;
		}
		ShaiDatebaseUtils shaiDatebaseUtils = new ShaiDatebaseUtils(
				getActivity());
		long m;
		if (lastDataBeads.size() == 0) {
			m = shaiDatebaseUtils.insert(json, postion);
			BingLog.i(TAG, "插入:" + m);
		} else {
			m = shaiDatebaseUtils.update(json, postion, 1);
			if (m == 0) {
				shaiDatebaseUtils.update(json, postion, _id);
			}
			BingLog.i(TAG, "更新:" + m);
		}

		shaiDatebaseUtils.close();

	}

	@Override
	protected void reshShayiSai(String first, String limit) {
		// TODO Auto-generated method stub
		super.reshShayiSai(first, limit);
		// HealthHttpClient.doHttpGetShayiSai(first, limit, reshHandler);
		if (adversie!=null&&thList2.size()>1) {
			thList2.remove(2);
			this.first=thList2.size();
		}
		HealthHttpClient.aired20(WyyApplication.getInfo().getId(), first,
				limit, key, reshHandler2);
	}

	@Override
	protected void getLoadMore(String first, String limit) {
		// TODO Auto-generated method stub
		super.getLoadMore(first, limit);
		// HealthHttpClient.doHttpGetShayiSai(first, limit, parseHandler);
		HealthHttpClient.aired20(WyyApplication.getInfo().getId(), first,
				limit, key, loadMoreHandler);
	}

	@Override
	public void onUserPicClick(int position) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCommentClick(int position) {
		// TODO Auto-generated method stub

		this.postion = position;
		if (thList2.get(position).getType() == ConstantS.TYPE_FOOD) {
			shaiFoodsid = thList2.get(position).getId();
			shaimoodsid = "";

		} else if (thList2.get(position).getType() == ConstantS.TYPE_MOOD) {
			shaimoodsid = thList2.get(position).getId();
			shaiFoodsid = "";

		}

		sendView.setVisibility(View.VISIBLE);
		sendEditText.requestFocus();
		InputUtlity.showInputWindow(getActivity(), sendEditText);
	}

	@Override
	public void onCollectClick(int position) {
		// TODO Auto-generated method stub

		if (thList2.get(position).getType() == ConstantS.TYPE_FOOD) {
			String foodsid = thList2.get(position).getId();
			CollectUtils.collectFood(foodsid, getActivity());

		} else if (thList2.get(position).getType() == ConstantS.TYPE_MOOD) {
			String moodsid = thList2.get(position).getId();
			CollectUtils.postMoodCollect(moodsid, getActivity());
		}
	}

	@Override
	public void onPicClick(int listPostino, int picPostion) {
		// TODO Auto-generated method stub
		try {
			if (thList2.get(listPostino).getImg() != null) {
				List<String> list = thList2.get(listPostino).getImg();
				Intent intent = new Intent();
				intent.putStringArrayListExtra("imgurls", (ArrayList<String>) list);
				intent.putExtra("postion", picPostion);
				intent.setClass(getActivity(), PhotoPagerActivity.class);
				startActivity(intent);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
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
				if (!TextUtils.isEmpty(shaiFoodsid)) {
					sendComment(shaiFoodsid);
				} else if (!TextUtils.isEmpty(shaimoodsid)) {
					sendMoodComment(shaimoodsid);
				}

				break;

			default:
				break;
			}
		}
	};

	/**
	 * 发送评论
	 * 
	 * @param foodsid
	 *            食物ID
	 */
	private void sendComment(String foodsid) {
		if (TextUtils.isEmpty(sendEditText.getText().toString())) {
			Toast.makeText(getActivity(), R.string.nullcontentnotice,
					Toast.LENGTH_LONG).show();
			return;
		}
		HealthHttpClient.doHttpPostComment(foodsid, sendEditText.getText()
				.toString(), "5", WyyApplication.getInfo().getId(),
				commentHandler);
	}

	/**
	 * 发送评论
	 * 
	 * @param foodsid
	 *            食物ID
	 */
	private void sendMoodComment(String moodsid) {
		if (TextUtils.isEmpty(sendEditText.getText().toString())) {
			Toast.makeText(getActivity(), R.string.nullcontentnotice,
					Toast.LENGTH_LONG).show();
			return;
		}
		HealthHttpClient.postMoodComment(WyyApplication.getInfo().getId(),
				moodsid, sendEditText.getText().toString(), commentHandler);
	}

	private AsyncHttpResponseHandler commentHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			super.onStart();

			CommentBean commentBean = new CommentBean();
			commentBean.setContent(sendEditText.getText().toString());
			commentBean.setUser(WyyApplication.getInfo());
			thList2.get(postion).getComment().add(commentBean);
			mAdapter2.notifyDataSetChanged();

			// Map<String, Object> map = thList.get(postion);
			// String lastcommentString = map.get("comment") + "";
			// if (!TextUtils.isEmpty("" + map.get("comment"))) {
			// lastcommentString = lastcommentString + "\n";
			// }
			//
			// thList.get(postion).put(
			// "comment",
			// lastcommentString + WyyApplication.getInfo().getUsername()
			// + ":" + sendEditText.getText().toString());
			//
			// mAdapter.notifyDataSetChanged();

			// sendButton.setEnabled(false);
			// sendButton.setBackgroundColor(getResources().getColor(
			// R.color.home_txt));

			sendEditText.setText("");
			sendView.setVisibility(View.GONE);
			shaiFoodsid = "";
			shaimoodsid = "";

		}

		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			super.onFinish();
			// sendButton.setEnabled(true);
			// sendButton.setText(R.string.send);
			// sendButton.setBackgroundColor(getResources().getColor(
			// R.color.transparent));
		}

		@Override
		public void onSuccess(String content) {
			// TODO Auto-generated method stub
			super.onSuccess(content);
			BingLog.i(TAG, "返回:" + content);
			if (null==getActivity()) {
				return;
			}
			Toast.makeText(getActivity(), R.string.comment_success_,
					Toast.LENGTH_LONG).show();
			// sendEditText.setText("");
			// sendView.setVisibility(View.GONE);
			// shaiFoodsid = "";
			// shaimoodsid = "";
		}

		@Override
		public void onFailure(Throwable error, String content) {
			// TODO Auto-generated method stub
			super.onFailure(error, content);
			Toast.makeText(getActivity(), R.string.comment_faliure,
					Toast.LENGTH_LONG).show();
		}

	};

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		if (!loadflag) {
			reshShayiSai("0", limit);
		}
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		if (!loadflag) {
			if (adversie != null) {
				thList2.remove(2);
				first = thList2.size();
			}
			getLoadMore("" + first, limit);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if (thList2.get(position).getType() == ConstantS.TYPE_FOOD) {
			PreferencesFoodsInfo.setfoodId(getActivity(), thList2.get(position)
					.getId());
			startActivity(new Intent(getActivity(), FoodDetailsActivity.class));
		}

	}

	public void addAdversie(MoodaFoodBean mBean) {
		BingLog.i(TAG, "添加:"+mBean);
		adversie = mBean;
		try {
			thList2.add(2, mBean);
			mAdapter2.notifyDataSetChanged();
		} catch (Exception e) {
			// TODO: handle exception
		}
		

	}

	@Override
	protected void addGg() {
		// TODO Auto-generated method stub
		super.addGg();
		if (adversie != null&&thList2.size()>1) {
			thList2.add(2, adversie);
		}

	}

	@Override
	protected void arrangeDayMonth() {
		// TODO Auto-generated method stub
		super.arrangeDayMonth();
		int length = thList2.size();
		for (int i = 0; i < length; i++) {
			thList2.get(i).setCn_time(
					TimeUtility.getListTime(thList2.get(i).getCreatetime()));
		}
	}
}
