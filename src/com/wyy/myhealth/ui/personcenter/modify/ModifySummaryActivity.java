package com.wyy.myhealth.ui.personcenter.modify;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.wyy.myhealth.R;
import com.wyy.myhealth.app.WyyApplication;
import com.wyy.myhealth.bean.PersonalInfo;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.ui.baseactivity.SubmitActivity;

public class ModifySummaryActivity extends SubmitActivity {

	private EditText summary_edit;
	private PersonalInfo info;
	private String summary;

	@Override
	protected void onInitActionBar() {
		// TODO Auto-generated method stub
		super.onInitActionBar();
		getSupportActionBar().setTitle(R.string.remarks);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_summary);
		initView();
	}

	private void initView() {
		context = this;
		summary_edit=(EditText)findViewById(R.id.summary_editText);
		initData();
	}

	private void initData() {
		info = WyyApplication.getInfo();
		summary_edit.setText("" + info.getSummary());
	}

	@Override
	protected void submitMsg() {
		// TODO Auto-generated method stub

		summary_edit.setError(null);
		summary = summary_edit.getText().toString();

		if (TextUtils.isEmpty(summary)) {
			summary_edit.setError(getString(R.string.nullcontentnotice));
			summary_edit.requestFocus();
			return;
		}

	HealthHttpClient
				.doHttpFinishPersonInfoForSummary(info, summary, new ModifyHandler(
						context));

	}

}
